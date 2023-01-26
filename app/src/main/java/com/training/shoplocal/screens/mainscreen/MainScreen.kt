package com.training.shoplocal.screens.mainscreen

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.training.shoplocal.*
import com.training.shoplocal.R
import com.training.shoplocal.classes.ComposeView
import com.training.shoplocal.classes.MESSAGE
import com.training.shoplocal.classes.Product
import com.training.shoplocal.classes.SIZE_PORTION
import com.training.shoplocal.classes.fodisplay.OrderDisplay
import com.training.shoplocal.classes.searcher.SearchState
import com.training.shoplocal.dialogs.ShowMessage
import com.training.shoplocal.screens.ScreenRouter
import com.training.shoplocal.screens.appscreen.ShowDataDisplayPanel
import com.training.shoplocal.screens.appscreen.ShowFilterDisplay
import com.training.shoplocal.screens.appscreen.ShowSearchHistory
import com.training.shoplocal.screens.remember.rememberLazyViewState
import com.training.shoplocal.ui.theme.*
import com.training.shoplocal.viewmodel.RepositoryViewModel
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun MainScreen(state: ModalBottomSheetState){
    val scope = rememberCoroutineScope()
    val stateGrid = rememberLazyViewState(ScreenRouter.current.key)
    val context = LocalContext.current
    val viewModel: RepositoryViewModel = viewModel()
    val products: MutableList<Product> by viewModel.products.collectAsState()
    val dataSnackbar: Triple<String, Boolean, MESSAGE> by viewModel.snackbarData.collectAsState()

    var isFocusedSearchTextField by remember {
        mutableStateOf(false)
    }

    val searchState = remember {
        mutableStateOf(SearchState.SEARCH_NONE)
    }

    val textSearch = remember {
        mutableStateOf("")
    }

    val focusManager = LocalFocusManager.current
    fun hideSearchDialog() {
        focusManager.clearFocus()
        isFocusedSearchTextField = false
        viewModel.removeComposeViewStack()
        viewModel.hideBottomNavigation(false)
    }
    fun isSearchMode() = searchState.value != SearchState.SEARCH_NONE

    // Сохраняем значение textSearch перед выбором из списка,
    // если будет нажата кнопка back в режиме списка -
    // textSearch присваиваем старое значение prevSearchText

    var prevStateScroll = remember {
        Pair<Int, Int>(0,0)
    }
    val prevSearchText = remember {
        StringBuilder("")
    }

    var searchScreenDisplayed by remember {
        mutableStateOf(false)
    }

    var filterScreenDisplayed by remember {
        mutableStateOf(false)
    }

    var showFloatingButton by remember {
        mutableStateOf(false)
    }




    /*val showSearchResult = remember {
        derivedStateOf {
            searchState.value == SearchState.SEARCH_RESULT || searchScreenDisplayed
        }
    }*/

    /*log ("prevSearchText $prevSearchText")
    log ("searchScreenDisplayed $searchScreenDisplayed")

    log ("showFloatingButton $showFloatingButton")

    log ("isFocusedSearchTextField $isFocusedSearchTextField")

    log ("searchState  ${searchState.value.name}")*/


    DisposableEffect(Unit) {
        onDispose {
           // isSearchMode = false
            searchScreenDisplayed = false
            isFocusedSearchTextField = false
            searchState.value = SearchState.SEARCH_NONE
        }
    }


    fun findProducts(recognizer: Boolean = false){
/*        if (products.isNotEmpty())
            LaunchedEffect(Unit) {
                scope.launch {
                    stateGrid.scrollToItem(0)
                }
            }*/
        if (textSearch.value.isNotBlank()) {
            if (!searchScreenDisplayed) {
                viewModel.saveScreenProducts(
                    ScreenRouter.current.key,
                    stateGrid.firstVisibleItemIndex
                )
            }
            if (!recognizer)
                hideSearchDialog()
            viewModel.putComposeViewStack(ComposeView.SEARCH)
            viewModel.findProductsRequest(textSearch.value.trim())
            searchState.value = SearchState.SEARCH_RESULT
            searchScreenDisplayed = true
        }
    }


    @Composable
    fun ShowMessageCount(value: Int = 0){
        val animated = remember{ mutableStateOf(value > 0) }


      /*  val timer = remember {
            Timer().apply {
                val task = object : TimerTask() {
                    override fun run() {
                        log("timer task")
                        animated.value = !animated.value
                    }
                }
                scheduleAtFixedRate(task, 5000L, 5000L)
            }
        }*/

        val MAX_SIZE = 32f
        val animate  = remember{ Animatable(0f) }
        val animate1 = remember{ Animatable(0f) }
        val animate2 = remember{ Animatable(0f) }
        val align = remember{ mutableStateOf( Alignment.Center)}
        val count = remember{ mutableStateOf(value)}

            Box(modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {}
                .padding(start = 4.dp)
                .size(32.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                LaunchedEffect(animated.value) {
                    scope.launch {
                        animate.animateTo(
                            targetValue = MAX_SIZE,
                            animationSpec = tween(400)
                        )
                        // ** Анимация Icon
                        val offset = 3f
                        for(i in 1 until 5){
                           val direction = if (i.mod(2) > 0) -1 else 1
                           val delta = direction * offset
                            animate2.animateTo(
                                targetValue = delta,
                                animationSpec = tween(
                                    //delayMillis = 500,
                                durationMillis = 20
                            ))
                        }
                        animate2.animateTo(
                            targetValue = 0f,
                            animationSpec = tween(
                                //delayMillis = 500,
                                durationMillis = 20)
                        )
                        // ** end Анимация Icon
                        align.value = Alignment.TopEnd
                        animate.animateTo(
                            targetValue = 18f,
                            animationSpec = tween(
                                delayMillis = 200,
                                durationMillis = 100
                            )
                        )
                            animate1.animateTo(
                                targetValue = 18f,
                                animationSpec = tween(//delayMillis = 1200,
                                    durationMillis = 200)
                            )
                    }
                }
                if (count.value > 0) {
                    Surface(
                        modifier = Modifier
                            .align(align.value)
                            //.fillMaxWidth()
                            .size(animate.value.dp),
                            shape = CircleShape,
                            color = BgDiscount.copy(alpha = animate.value / MAX_SIZE)
                        ) {}
                }
                Icon(
                    ImageVector.vectorResource(R.drawable.ic_message),
                    contentDescription = null,
                    tint = TextFieldFont,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .offset(x = if (count.value > 0) animate2.value.dp else 0.dp)
                )
                if (count.value > 0) {
                    Box(
                        modifier = Modifier
                            .size(18.dp)//animate1.value.dp)
                            //.height(animate1.value.dp)
                            //.width(animate1.value.dp)
                            .background(
                                color = BgDiscount.copy(alpha = animate1.value / 18),
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = count.value.toString(),
                            color = TextDiscount.copy(alpha = animate1.value / 18),
                            fontSize = 10.sp,
                        )
                    }
                }
            }
    }

    val startLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {it ->
        if (it.resultCode == Activity.RESULT_OK) {
            it.data?.let{ data ->
                (data.extras?.getStringArrayList(RecognizerIntent.EXTRA_RESULTS)).let { matches ->
                    val value = matches?.get(0)
                    if (!value.isNullOrEmpty()) {
                        textSearch.value = value
                        if (searchState.value != SearchState.SEARCH_QUERY)
                            findProducts(true)
                    }
                }
            }
        }
    }



/*
    var isSearchMode by remember {
        mutableStateOf(false)
    }

    var searchScreenDisplayed by remember {
        mutableStateOf(false)
    }


    val isFocusedSearchTextField = remember {
        mutableStateOf(false)
    }

    */
/*val lastSearchQuery = remember {
        mutableStateOf("")
    }*//*


    val searchState = remember {
        mutableStateOf(SearchState.SEARCH_CANCEL)
    }

    //val stateGrid = rememberLazyGridState()
*/



    BackHandler(enabled = isSearchMode()){
        if (isSearchMode()) {
            textSearch.value = ""
            searchState.value = SearchState.SEARCH_NONE
            //isSearchMode = false
            hideSearchDialog()
        }
    }


    val panelHeightPx = with(LocalDensity.current) { 40.dp.roundToPx().toFloat() }
    val panelOffsetHeightPx = remember { mutableStateOf(0f) }
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                val delta = available.y
                val newOffset = panelOffsetHeightPx.value + delta
                panelOffsetHeightPx.value = newOffset.coerceIn(-panelHeightPx, 0f)
                // возвращает значение если оно в интервале, либо мин значение если меньше или макс значение если больше
                return Offset.Zero
            }


        }
    }




 //   log("remember first index ${stateGrid.firstVisibleItemIndex}")
  //  Column(modifier = Modifier.fillMaxWidth()) {
         //   Box() {
             //   ShowDataDisplayPanel(hide = isSearchMode)


    val visibleFAB = remember {
        MutableTransitionState(false)
    }
    @Composable
    fun AnimatedFloatingActionButton(action: () -> Unit){
     // val scope = rememberCoroutineScope()
//     val visible = MutableTransitionState(false)
      visibleFAB.targetState = showFloatingButton
      /*val animate = remember{ Animatable(0f) }
      val scaleButton = if (show) animate.value else 0f
      LaunchedEffect(Unit) {
          scope.launch {
              animate.animateTo(
                  targetValue = scaleButton,
                  animationSpec = tween(500)
              )
          }
      }*/

      androidx.compose.animation.AnimatedVisibility(
          visibleState = visibleFAB,
          enter = scaleIn(
              animationSpec = tween(
                  durationMillis = 200,
                  easing = LinearEasing
              )
          ),
          exit =  scaleOut(
              animationSpec = tween(
                  durationMillis = 100,
                  easing = LinearEasing
              )
          )
      ) {

          FloatingActionButton(//modifier = Modifier
             // .graphicsLayer(scaleX = scaleButton, scaleY = scaleButton),
              backgroundColor = TextFieldBg,// SelectedItem,
              content = {


                  Image(
                      Icons.Filled.ArrowBack,
                      modifier = Modifier.rotate(90f),
                      contentDescription = null,
                      colorFilter = ColorFilter.tint(TextFieldFont)
                  )
              },
              onClick = {action()}
          )
      }
    }

    Scaffold(
        floatingActionButton = {
           AnimatedFloatingActionButton(){
               scope.launch {
                   stateGrid.animateScrollToItem(
                       0
                   )
               }
           }
        },
        topBar = {
            @Composable
        fun BackButton(modifier: Modifier, onClick: () -> Unit){
                Icon(modifier = modifier
                    .padding(end = 8.dp)
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        onClick()
                    },
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = null,
                    tint = TextFieldFont
                )
        }
        TopAppBar(backgroundColor = MaterialTheme.colors.primary,
                    elevation = 0.dp
        ) {
            Row(
                Modifier
                    .padding(horizontal = 4.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                if (filterScreenDisplayed) {
                    BackButton(
                        modifier = Modifier
                        .align(Alignment.CenterVertically)
                    ) {
                        hideSearchDialog()
                        filterScreenDisplayed = false
                    }
                    Text(modifier = Modifier.weight(1f),
                        text = stringResource(R.string.text_filter),
                        color = TextFieldFont,
                        fontSize = 17.sp
                    )
                } else {


                    if (isSearchMode()) {
                        //val scope = rememberCoroutineScope()
                        BackButton(
                            modifier = Modifier
                                .align(Alignment.CenterVertically)
                        ) {
                            hideSearchDialog()
                            if (searchScreenDisplayed) {
                                if (searchState.value == SearchState.SEARCH_QUERY) {
                                    searchState.value = SearchState.SEARCH_RESULT
                                    textSearch.value = prevSearchText.toString()
                                } else {
                                    viewModel.clearResultSearch() // удаляем результаты последнего запроса в БД на сервере
                                    val firstIndex =
                                        viewModel.restoreScreenProducts(ScreenRouter.current.key)
                                    scope.launch {
                                        stateGrid.scrollToItem(
                                            firstIndex
                                        )
                                    }
                                        searchScreenDisplayed = false
                                        searchState.value = SearchState.SEARCH_NONE
                                        textSearch.value = ""
                                }
                            } else {
                              // log("back")
                                searchState.value = SearchState.SEARCH_NONE
                                textSearch.value = ""
                            }
                        }
                    }
                    //**************************************************************************************
                    BasicTextField(
                        modifier = Modifier
                            .onFocusChanged {
                                if (it.isFocused) {
                                    // prevStateScroll = Pair<Int, Int>(0,0)
                                    showFloatingButton = false
                                    viewModel.putComposeViewStack(ComposeView.SEARCH_EDIT)
                                    //isSearchMode = true
                                    //val searchStore: SearchQueryStorageInterface = SearchQueryStorage.getInstance()
                                    //lastSearchQuery.value = ""
                                    /*scope.launch {
                                    stateGrid.scrollToItem(0)
                                }*/
                                    searchState.value = SearchState.SEARCH_QUERY
                                    prevSearchText.clear()
                                    prevSearchText.append(textSearch.value)
                                    //log("prev text search = $prevSearchText")
                                    isFocusedSearchTextField = true
                                    viewModel.hideBottomNavigation()
                                }
                            }
                            .weight(1f)
                            .height(32.dp)
                            .background(color = TextFieldBg, shape = RoundedCornerShape(32.dp)),
                        cursorBrush = SolidColor(TextFieldFont),
                        value = textSearch.value,
                        textStyle = TextStyle(color = TextFieldFont),
                        onValueChange = {
                            textSearch.value = it
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Search,
                            keyboardType = KeyboardType.Text
                        ),
                        keyboardActions = KeyboardActions(
                            onSearch = {
                                findProducts()

                                /*if (products.isNotEmpty())
                                    scope.launch {
                                        stateGrid.scrollToItem(0)
                                    }*/
                                //   hideSearchDialog()

                             /*
                                if (textSearch.value.isNotBlank()) {

                                    if (!searchScreenDisplayed) {
                                        viewModel.saveScreenProducts(
                                            ScreenRouter.current.key,
                                            stateGrid.firstVisibleItemIndex
                                            //stateGrid.firstVisibleItemScrollOffset
                                        )
                                    }

                                    hideSearchDialog()
                                    viewModel.putComposeViewStack(ComposeView.SEARCH)
                                   // log("text search = ${textSearch.value.trim()}")
                                    viewModel.findProductsRequest(textSearch.value.trim())
                                    searchState.value = SearchState.SEARCH_RESULT
                                    searchScreenDisplayed = true
                                    /*if (products.isNotEmpty())
                                    scope.launch {
                                        stateGrid.scrollToItem(0)
                                    }*/
                                }*/
                            }
                        ),
                        decorationBox = { innerTextField ->
                            val error_speechrecognizer =
                                stringResource(id = R.string.text_error_speechrecognizer)
                            TextFieldDefaults.TextFieldDecorationBox(
                                value = "",
                                placeholder = {
                                    if (textSearch.value.isEmpty())
                                        Text(
                                            text = stringResource(id = R.string.text_search),
                                            fontSize = 14.sp,
                                            color = TextFieldFont.copy(alpha = 0.4f)
                                        )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.ic_search),
                                        contentDescription = null
                                    )
                                },
                                trailingIcon = {
                                    val showClearIcon =
                                        textSearch.value.isNotEmpty() && searchState.value == SearchState.SEARCH_QUERY
                                    //  log(searchState.value.name)
                                    val iconSize = if (showClearIcon) 16.dp else 24.dp
                                    Icon(
                                        imageVector = if (showClearIcon)
                                            ImageVector.vectorResource(R.drawable.ic_cancel_bs)
                                        else
                                            ImageVector.vectorResource(R.drawable.ic_microphone),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .clip(CircleShape)
                                            .size(iconSize)
                                            .clickable {
                                                if (showClearIcon) {
                                                    textSearch.value = ""
                                                    //DialogRouter.reset()
                                                    //showSearch = false
                                                } else {
                                                    // Вызвать голосовой ввод
                                                    getSpeechInput(context)?.let { intent ->
                                                        startLauncher.launch(intent)
                                                    } ?: viewModel.showSnackbar(
                                                        error_speechrecognizer,
                                                        type = MESSAGE.ERROR
                                                    )
                                                }
                                            }
                                    )
                                },

                                visualTransformation = VisualTransformation.None,
                                innerTextField = innerTextField,
                                singleLine = true,
                                enabled = true,
                                interactionSource = remember {
                                    MutableInteractionSource()
                                },
                                contentPadding = PaddingValues(0.dp)
                            )
                        })

                    //val interactionSource = remember { MutableInteractionSource() }
                    //  ShowMessageCount(31)

                    //**************************************************************************************
                    if (!isSearchMode())
                    //if (!isFocusedSearchTextField.value)
                        ShowMessageCount(24)

                }
            }
        }
    }) {

        //ShowDataDisplayPanel(hide = isSearchMode)
                /*if (isFocusedSearchTextField.value) {
            ShowSearchHistory(textSearch, lastSearchQuery)
        }*/
                /*AnimatedVisibility(
                    visible = isFocusedSearchTextField.value,
                     enter = fadeIn(),
                    exit  = fadeOut()
                ) {
                    ShowSearchHistory(textSearch, lastSearchQuery)

                }*/

                //ShowDataDisplayPanel(hide = isSearchMode)
        //    }
            //val verticalScrollState = rememberScrollState()
        Box(
                modifier = Modifier
                    //.verticalScroll(verticalScrollState)
                    .padding(it)
                    .nestedScroll(nestedScrollConnection)
                    .fillMaxSize()
                    .background(BgScreenDark),
            ) {

               // val boxWithConstraintsScope = this
                //log("height = ${boxWithConstraintsScope.maxHeight}")*/
               /* var heightConstraints by remember {
                    mutableStateOf(boxWithConstraintsScope.maxHeight)
                }
                LaunchedEffect(key1 = boxWithConstraintsScope.maxHeight) {
                    heightConstraints = boxWithConstraintsScope.maxHeight
                    log("height = $heightConstraints")
                }*/

            /*Column(modifier = Modifier
                .nestedScroll(nestedScrollConnection)
                .fillMaxSize()
                .background(BgScreenDark)) {*/
                //    log ("recompose Grid")

              /*  Box(Modifier
                    .offset { IntOffset(x = 0, y = panelOffsetHeightPx.value.roundToInt()) }
                    .fillMaxWidth()
                    .background(Color.Red)
                    .height(40.dp)
                )*/
                /*ShowDataDisplayPanel(modifier = Modifier
                    .offset { IntOffset(x = 0, y = panelOffsetHeightPx.value.roundToInt()) }
                   *//* .graphicsLayer (


                       translationY =  panelOffsetHeightPx.value
                    )*//*
                    .fillMaxWidth()

                    //.height(40.dp + panelOffsetHeightPx.value.dp)
                    .background(MaterialTheme.colors.primary),
                    hide = isSearchMode)*/


                if (products.isNotEmpty()) {
                   // val verticalScrollState = rememberScrollState()
                    //log("first Index = ${stateGrid.firstVisibleItemIndex}")
                    LazyVerticalGrid(
                        modifier = Modifier
                            .padding(horizontal = 10.dp),
                    //        .verticalScroll(verticalScrollState, true)
                    //        .height(boxWithConstraintsScope.maxHeight),
                        columns = GridCells.Adaptive(minSize = 150.dp),
                        state = stateGrid,
                        //contentPadding = PaddingValues(10.dp),
                        contentPadding = PaddingValues(top = 40.dp),
                        //horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                     //   log(products)
                        items(products, { product -> product.id }) { product ->
                            //log("item${product.id}")
                            // items(products.size, key = {}) { index ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                CardProduct(product, state = state)
                            }
                        }

                      /*  if (restoredStateGrid != null) {
                            stateGrid = LazyGridState(
                                restoredStateGrid!!.firstVisibleItemIndex,
                                restoredStateGrid!!.firstVisibleItemScrollOffset
                            )
                            restoredStateGrid = null
                        }*/
                    }

                    if (!isFocusedSearchTextField) {
                    val changeVisibleStateFAB = remember {
                        derivedStateOf {
                            val firstVisible = stateGrid.layoutInfo.visibleItemsInfo.isNotEmpty() && stateGrid.layoutInfo.visibleItemsInfo.first().index == 0
                            val prevFirstIndex  = prevStateScroll.first
                            val prevFirstOffset = prevStateScroll.second
                            val firstIndex  = stateGrid.firstVisibleItemIndex
                            val firstOffset = stateGrid.firstVisibleItemScrollOffset
                            val upperLimit = firstVisible && firstOffset == 0 && firstIndex == 0
                            /*if (upperLimit)
                                log("upper limit")*/
                            var scrollUp = false
                            if (!upperLimit) {
                                val deltaIndex = firstIndex - prevFirstIndex
                                val deltaOffset  = if (firstIndex > prevFirstIndex)
                                                        0
                                                   else
                                                        firstOffset - prevFirstOffset
                                if (deltaOffset < 0 || deltaIndex < 0) {
                                    /*log ("firstIndex = $firstIndex, prev = $prevFirstIndex")
                                    log ("firstOffset = $firstOffset, prev = $prevFirstOffset")*/
                                    scrollUp = true
                                }
                            }
                            prevStateScroll = firstIndex to firstOffset

                            scrollUp
                        }

                    }
                LaunchedEffect(changeVisibleStateFAB.value) {
                    showFloatingButton = changeVisibleStateFAB.value
                }

                    }
                val nextPart = remember {
                    derivedStateOf {
                        //log("offset ${stateGrid.firstVisibleItemScrollOffset}")
                        //log ("lastIndex = ${stateGrid.layoutInfo.visibleItemsInfo.lastOrNull()?.index}, gridCount = ${stateGrid.layoutInfo.totalItemsCount - 1}")
                        /*val viewOffset = stateGrid.layoutInfo.viewportEndOffset // высота Grid
                        val offset = stateGrid.layoutInfo.visibleItemsInfo.lastOrNull()?.offset?.y ?: 0 // расстояние от верха item до верха grid
                        val height = stateGrid.layoutInfo.visibleItemsInfo.lastOrNull()?.size?.height ?: 0
                        log("view offset = $viewOffset")
                        log("last offset = $offset")
                        log("height = $height")*/
                        //log ("offset = ${viewOffset  - offset}, height $height")
                       /* log ("gridOffset = ${stateGrid.layoutInfo.viewportEndOffset -
                                offset}, heightLast = ${stateGrid.layoutInfo.visibleItemsInfo.last().size.height}")*/
                        val total: Int = products.size / SIZE_PORTION
                        val remains    = products.size % SIZE_PORTION
                        val upload = if (remains > 0)
                                        false else
                                     total > 0

                       viewModel.nextPortionAvailable() && upload &&
                        stateGrid.layoutInfo.visibleItemsInfo.lastOrNull()?.index == stateGrid.layoutInfo.totalItemsCount - 1
                                && stateGrid.isScrollInProgress
                                //&& stateGrid.layoutInfo.visibleItemsInfo.last().offset.y > 0
                                && stateGrid.layoutInfo.viewportEndOffset - stateGrid.layoutInfo.visibleItemsInfo.last().offset.y >= stateGrid.layoutInfo.visibleItemsInfo.last().size.height / 2

                    }
                }

                LaunchedEffect(nextPart.value) {
                    if (nextPart.value) {
                        //log("next portion")
                        viewModel.getNextPortionData(isSearchMode(), textSearch.value.trim())
                    }
                }


                ShowDataDisplayPanel(modifier = Modifier
                    .offset { IntOffset(x = 0, y = panelOffsetHeightPx.value.roundToInt()) }
                    /* .graphicsLayer (


                        translationY =  panelOffsetHeightPx.value
                     )*/
                    .fillMaxWidth()

                    .height(40.dp)
                    .background(MaterialTheme.colors.primary),
                ) {
                    viewModel.hideBottomNavigation()
                    filterScreenDisplayed = true
                    showFloatingButton = false
                    viewModel.putComposeViewStack(ComposeView.FILTER)
                }


                }

                androidx.compose.animation.AnimatedVisibility(
                        visible = isFocusedSearchTextField,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        //if (isFocusedSearchTextField.value) {
                        ShowSearchHistory(textSearch, searchState)//lastSearchQuery)
                    }

            androidx.compose.animation.AnimatedVisibility(
                visible = filterScreenDisplayed,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                ShowFilterDisplay(OrderDisplay.getInstance(), reset = {
                    filterScreenDisplayed = false
                    if (OrderDisplay.resetFilter()) {
                        log("reset filter")
                    }
                }) {filter ->
                    filterScreenDisplayed = false
                    val changedFilterData   = !OrderDisplay.equalsFilterData(filter)
                    val changedViewModeData = !OrderDisplay.equalsFilterViewMode(filter)
                    if (changedFilterData) {
                        log("filter $filter")
                        OrderDisplay.setFilter(filter)
                    }
                }
            }
        }


    }

    if (dataSnackbar.second) {
            ShowMessage(
                message = dataSnackbar.first,
                viewModel = viewModel,
                type = dataSnackbar.third
            )//, type = MESSAGE.WARNING)
        }
  //  }
 }

private fun getSpeechInput(context: Context) : Intent? {
    return if (!SpeechRecognizer.isRecognitionAvailable(context))
        null
    else {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(
            RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH
        )
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getStringResource(R.string.prompt_speechrecognizer))
        intent
    }
}
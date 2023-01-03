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
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.graphicsLayer
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
import com.training.shoplocal.classes.Product
import com.training.shoplocal.classes.searcher.SearchState
import com.training.shoplocal.dialogs.ShowMessage
import com.training.shoplocal.screens.ScreenRouter
import com.training.shoplocal.screens.appscreen.ShowDataDisplayPanel
import com.training.shoplocal.screens.appscreen.ShowSearchHistory
import com.training.shoplocal.screens.remember.rememberLazyViewState
import com.training.shoplocal.ui.theme.*
import com.training.shoplocal.viewmodel.RepositoryViewModel
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.roundToInt


/*@Composable
fun keyboardAsState(): State<Boolean> {
    val isImeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
  //  log(isImeVisible.toString())
    return rememberUpdatedState(isImeVisible)
}*/

/*@OptIn(ExperimentalLayoutApi::class)
fun Modifier.clearFocusOnKeyboardDismiss(): Modifier = composed {
    var isFocused by remember { mutableStateOf(false) }
    var keyboardAppearedSinceLastFocused by remember { mutableStateOf(false) }
    log ("isFocused $isFocused")
    if (isFocused) {
        val imeIsVisible = WindowInsets.isImeVisible
        val focusManager = LocalFocusManager.current
        LaunchedEffect(imeIsVisible) {
            //log(imeIsVisible.toString())
            if (imeIsVisible) {
                keyboardAppearedSinceLastFocused = true
            } else if (keyboardAppearedSinceLastFocused) {
                log("clear focus")
                focusManager.clearFocus()
            }
        }
    }
    onFocusEvent {
        if (isFocused != it.isFocused) {
            isFocused = it.isFocused
            if (isFocused) {
                keyboardAppearedSinceLastFocused = false
            }
        }
    }
}
*/
@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun MainScreen(state: ModalBottomSheetState){

  /*  @Composable
    fun ShowFoundProducts() {

    }*/

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
        val scope = rememberCoroutineScope()
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
    val context = LocalContext.current
    val viewModel: RepositoryViewModel = viewModel()
    val products: MutableList<Product> by viewModel.products.collectAsState()
    val dataSnackbar: Triple<String, Boolean, MESSAGE> by viewModel.snackbarData.collectAsState()
    val textSearch = remember {
        mutableStateOf("")
    }
    val startLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {it ->
        if (it.resultCode == Activity.RESULT_OK) {
            it.data?.let{ data ->
                (data.extras?.getStringArrayList(RecognizerIntent.EXTRA_RESULTS)).let { matches ->
                    val value = matches?.get(0)
                    if (!value.isNullOrEmpty())
                        textSearch.value = value
                }
            }
        }
    }
    var isSearchMode by remember {
        mutableStateOf(false)
    }
    
    val isFocusedSearchTextField = remember {
        mutableStateOf(false)
    }

    /*val lastSearchQuery = remember {
        mutableStateOf("")
    }*/

    val searchState = remember {
        mutableStateOf(SearchState.SEARCH_QUERY)
    }

    //val stateGrid = rememberLazyGridState()

    val focusManager = LocalFocusManager.current
    fun hideSearchDialog() {
        focusManager.clearFocus()
        isFocusedSearchTextField.value = false
        viewModel.hideBottomNavigation(false)
    }

    BackHandler(enabled = isSearchMode){
        if (isSearchMode) {
            textSearch.value = ""
            isSearchMode = false
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
                return Offset.Zero
            }
        }
    }




    val stateGrid = rememberLazyViewState(key = ScreenRouter.current.key)
  //  Column(modifier = Modifier.fillMaxWidth()) {
         //   Box() {
             //   ShowDataDisplayPanel(hide = isSearchMode)


    Scaffold( topBar = {

        TopAppBar(backgroundColor = MaterialTheme.colors.primary,
                    elevation = 0.dp
        ) {
            Row(
                Modifier
                    .padding(horizontal = 4.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {

                //if (isFocusedSearchTextField.value) {
                if (isSearchMode) {
//                      val list = LocalSearchStorage.current?.getQueries() ?: listOf<String>()
                    //  IconButton(onClick = {  }) {
                    Icon(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                            .padding(end = 8.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                isSearchMode = false
                                hideSearchDialog()
//                                if (lastSearchQuery.value.isNotEmpty())
                                if (searchState.value == SearchState.SEARCH_PROCESS)
                                    viewModel.restoreScreenProducts(ScreenRouter.current.key)
                                searchState.value = SearchState.SEARCH_CANCEL
                                textSearch.value = ""
                            },
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = null,
                        tint = TextFieldFont
                    )
                }
                //**************************************************************************************
                BasicTextField(
                    modifier = Modifier
                        .onFocusChanged {
                            if (it.isFocused) {
                                isSearchMode = true
                                //val searchStore: SearchQueryStorageInterface = SearchQueryStorage.getInstance()
                                //lastSearchQuery.value = ""
                                searchState.value = SearchState.SEARCH_QUERY
                                isFocusedSearchTextField.value = true
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
                            //   hideSearchDialog()
                            if (textSearch.value.isNotBlank()) {
                                // viewModel.saveCurrentScreenData(stateGrid)
                                //lastSearchQuery.value = textSearch.value
                                searchState.value = SearchState.SEARCH_PROCESS
                                hideSearchDialog()
                                viewModel.saveScreenProducts(ScreenRouter.current.key)
                                viewModel.findProductsRequest(textSearch.value.trim())
                            }
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
                                val showClearIcon = textSearch.value.isNotEmpty()
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

                                                DialogRouter.reset()
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
                if (!isSearchMode)
                //if (!isFocusedSearchTextField.value)
                    ShowMessageCount(24)

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

        /*ShowDataDisplayPanel(modifier = Modifier
            .offset{ IntOffset(x = 0, y = panelOffsetHeightPx.value.roundToInt()) }
            .fillMaxWidth()
            .height(40.dp)
            .background(MaterialTheme.colors.primary),
            hide = isSearchMode)
*/
            Box(
                modifier = Modifier
                    .padding(it)
                    .nestedScroll(nestedScrollConnection)
                    .fillMaxSize()
                    .background(BgScreenDark),
            ) {

               /* val boxWithConstraintsScope = this
                log("height = ${boxWithConstraintsScope.maxHeight}")*/
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
                    LazyVerticalGrid(
                        modifier = Modifier
                            .padding(horizontal = 10.dp),
                        columns = GridCells.Adaptive(minSize = 150.dp),
                        state = stateGrid,
                        //contentPadding = PaddingValues(10.dp),
                        contentPadding = PaddingValues(top = 40.dp),
                        //horizontalArrangement = Arrangement.SpaceBetween
                    ) {
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
                    }
                }

                val nextPart = remember {
                    derivedStateOf {
                        //log ("lastIndex = ${stateGrid.layoutInfo.visibleItemsInfo.lastOrNull()?.index}, gridCount = ${stateGrid.layoutInfo.totalItemsCount - 1}")
                        /*val viewOffset = stateGrid.layoutInfo.viewportEndOffset
                        val offset = stateGrid.layoutInfo.visibleItemsInfo.lastOrNull()?.offset?.y ?: 0
                        val height = stateGrid.layoutInfo.visibleItemsInfo.lastOrNull()?.size?.height ?: 0
                        log ("offset = ${viewOffset  - offset}, height $height")*/
                       /* log ("gridOffset = ${stateGrid.layoutInfo.viewportEndOffset -
                                offset}, heightLast = ${stateGrid.layoutInfo.visibleItemsInfo.last().size.height}")*/
                        stateGrid.layoutInfo.visibleItemsInfo.lastOrNull()?.index == stateGrid.layoutInfo.totalItemsCount - 1
                                //&& stateGrid.isScrollInProgress
                                && stateGrid.layoutInfo.viewportEndOffset - stateGrid.layoutInfo.visibleItemsInfo.last().offset.y >= stateGrid.layoutInfo.visibleItemsInfo.last().size.height / 2

                    }
                }
                LaunchedEffect(nextPart.value) {

                    //log("deriverd ${nextPart.value}")
                    if (nextPart.value) {
                        //log("end scroll")
                      /*  try {
                            log("last ${stateGrid.layoutInfo.visibleItemsInfo.last().index}")
                        } catch (_: Exception){}*/
                        viewModel.getNextPortionData()
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
                    hide = isSearchMode)
        //    }

                androidx.compose.animation.AnimatedVisibility(
                        visible = isFocusedSearchTextField.value,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        //if (isFocusedSearchTextField.value) {
                        ShowSearchHistory(textSearch, searchState)//lastSearchQuery)
                    }

//}
               /* if (!isSearchMode)
                    ShowDataDisplayPanel()*/



                    // }
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
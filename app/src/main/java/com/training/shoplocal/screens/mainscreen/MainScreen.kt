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
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.training.shoplocal.*
import com.training.shoplocal.R
import com.training.shoplocal.classes.*
import com.training.shoplocal.classes.fodisplay.OrderDisplay
import com.training.shoplocal.classes.fodisplay.VIEW_MODE
import com.training.shoplocal.classes.searcher.SearchState
import com.training.shoplocal.dialogs.ShowMessage
import com.training.shoplocal.dialogs.ShowProgress
import com.training.shoplocal.screens.ScreenRouter
import com.training.shoplocal.screens.appscreen.*
import com.training.shoplocal.screens.mainscreen.composable.*
import com.training.shoplocal.screens.remember.rememberLazyViewState
import com.training.shoplocal.ui.theme.*
import com.training.shoplocal.viewmodel.RepositoryViewModel
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class,
    ExperimentalFoundationApi::class
)
@Composable
fun MainScreen(state: ModalBottomSheetState){
    val viewModel: RepositoryViewModel = viewModel()
    val detailProduct = remember {
        Product()
    }
    val openDialogMessages = remember {
        mutableStateOf(false)
    }
    if (openDialogMessages.value)
        ShowUserMessages(openDialogMessages)
    val progress by viewModel.progressCRUD.collectAsState()
    if (progress)
        ShowProgress()
    val scope = rememberCoroutineScope()
    val stateGrid = rememberLazyViewState(ScreenRouter.current.key)
    val context = LocalContext.current
    val products: MutableList<Product> by viewModel.products.collectAsState()
    val dataSnackbar: Triple<String, Boolean, MESSAGE> by viewModel.snackbarData.collectAsState()
    val localDensity = LocalDensity.current
    val panelHeightPx = with(localDensity) { 40.dp.roundToPx().toFloat() }
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
    val activeContainer by viewModel.activeContainer.collectAsState()

    fun isActiveContainer(value: Container) =
        activeContainer == value

    fun setActiveContainer(value: Container) {
        viewModel.putComposeViewStack(value)
    }

    val focusManager = LocalFocusManager.current
    var isFocusedSearchTextField by remember {
        mutableStateOf(false)
    }
    fun showBottomNavigation() {
        focusManager.clearFocus()
        panelOffsetHeightPx.value = 0f
        isFocusedSearchTextField = false
        viewModel.prevComposeViewStack()
        viewModel.showBottomNavigation()
    }

    val searchState = remember {
        mutableStateOf(SearchState.SEARCH_NONE)
    }

    val textSearch = remember {
        mutableStateOf("")
    }

    var prevStateScroll = remember {
        0 to 0
    }
    val prevSearchText = remember {
        StringBuilder("")
    }

    fun prevContainerSearch(): Boolean {
        val prev = viewModel.getPrevContainer()
        val isPrevSearchResult = prev?.let {
            it == Container.SEARCH
        } ?: false
        return isPrevSearchResult
    }

    fun backSearchMode(){
         if (searchState.value == SearchState.SEARCH_QUERY) {
            if (prevContainerSearch()) {
                searchState.value = SearchState.SEARCH_RESULT
                textSearch.value = prevSearchText.toString()
            } else {
                searchState.value = SearchState.SEARCH_NONE
                textSearch.value = EMPTY_STRING
            }
        } else {
            viewModel.clearResultSearch()
            val firstIndex =
                viewModel.restoreScreenProducts(ScreenRouter.current.key)
            scope.launch {
                stateGrid.scrollToItem(
                    firstIndex
                )
            }
            searchState.value = SearchState.SEARCH_NONE
            textSearch.value = ""
        }
        showBottomNavigation()
    }

    fun actionBack(container: Container){
        when (container) {
            Container.DETAIL -> {
                showBottomNavigation()
            }
            Container.FILTER -> {
                showBottomNavigation()
            }
            Container.SEARCH,
            Container.SEARCH_EDIT -> {
                backSearchMode()
            }
            else -> {
                viewModel.closeApp()
            }
        }
    }

    val isSearchMode by remember {
        derivedStateOf {
            activeContainer == Container.SEARCH_EDIT || activeContainer == Container.SEARCH
        }
    }

    var showFloatingButton by remember {
        mutableStateOf(false)
    }

    DisposableEffect(Unit) {
        onDispose {
            isFocusedSearchTextField = false
            searchState.value = SearchState.SEARCH_NONE
        }
    }

    fun findProducts(query: String, recognizer: Boolean = false){

        val prev = viewModel.getPrevContainer()
        val isPrevDetail = prev?.let {
            it == Container.DETAIL
        } ?: false
            if (!recognizer)
                showBottomNavigation()
            if (!isActiveContainer(Container.SEARCH)) {
                viewModel.saveScreenProducts(
                    ScreenRouter.current.key,
                    stateGrid.firstVisibleItemIndex
                )
            }
            viewModel.startLoadedData()
            viewModel.findProductsRequest(query)
            searchState.value = SearchState.SEARCH_RESULT
            panelOffsetHeightPx.value = 0f
            if (isPrevDetail)
                viewModel.prevComposeViewStack()
            setActiveContainer(Container.SEARCH)
    }

    @Composable
    fun ShowMessageCount(onClick: () -> Unit){
        val count = viewModel.countUnreadMessages.collectAsState()
        val animated = remember{ mutableStateOf(count.value > 0) }
        val MAX_SIZE = 32f
        val animate  = remember{ Animatable(0f) }
        val animate1 = remember{ Animatable(0f) }
        val animate2 = remember{ Animatable(0f) }
            Box(modifier = Modifier
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    onClick()
                }
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
                        val offset = 3f
                        for(i in 1 until 5){
                           val direction = if (i.mod(2) > 0) -1 else 1
                           val delta = direction * offset
                            animate2.animateTo(
                                targetValue = delta,
                                animationSpec = tween(
                                durationMillis = 20
                            ))
                        }
                        animate2.animateTo(
                            targetValue = 0f,
                            animationSpec = tween(
                                durationMillis = 20)
                        )
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
                            .align(Alignment.TopEnd)
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
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            it.data?.let{ data ->
                (data.extras?.getStringArrayList(RecognizerIntent.EXTRA_RESULTS)).let { matches ->
                    val value = matches?.get(0)
                    if (!value.isNullOrEmpty()) {
                        textSearch.value = value
                        if (searchState.value != SearchState.SEARCH_QUERY)
                            findProducts(value,true)
                    }
                }
            }
        }
    }
    BackHandler(enabled = true){
        actionBack(activeContainer)
    }

    val visibleFAB = remember {
        MutableTransitionState(false)
    }
    @Composable
    fun AnimatedFloatingActionButton(action: () -> Unit){
       visibleFAB.targetState = showFloatingButton
       AnimatedVisibility(
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
          FloatingActionButton(
              backgroundColor = PrimaryDark,
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
           AnimatedFloatingActionButton {
               scope.launch {
                   stateGrid.animateScrollToItem(
                       0
                   )
               }
           }
        },
        topBar = {
        TopAppBar(backgroundColor = MaterialTheme.colors.primary,
                    elevation = 0.dp,
        ) {
            Row(
                Modifier
                    .padding(horizontal = 4.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                if (isActiveContainer(Container.FILTER) || isActiveContainer(Container.DETAIL)
                    || isSearchMode
                )   BackButton(
                        modifier = Modifier
                            .align(Alignment.CenterVertically)
                    ) {
                        actionBack(activeContainer)
                    }
                when (activeContainer) {
                    Container.FILTER -> {
                        Text(modifier = Modifier.weight(1f),
                            text = stringResource(R.string.text_filter),
                            color = TextFieldFont,
                            fontSize = 17.sp
                        )
                    }
                    Container.DETAIL,
                    Container.MAIN,
                    Container.SEARCH,
                    Container.SEARCH_EDIT -> {
                        var enableSearch = true
                        if (isActiveContainer(Container.DETAIL)) {
                            if (prevContainerSearch()) {
                                enableSearch = false
                            }
                        }
                        TextFieldSearch(modifier = Modifier.weight(1f), enabled = enableSearch,
                            textSearch = textSearch,
                            onSpeechRecognizer = {
                                val error_speechrecognizer =
                                    getStringResource(id = R.string.text_error_speechrecognizer)
                                getSpeechInput(context)?.let { intent ->
                                    startLauncher.launch(intent)
                                } ?: viewModel.showSnackbar(
                                    error_speechrecognizer,
                                    type = MESSAGE.ERROR
                                )
                            },
                            onFocused = {
                                showFloatingButton = false
                                setActiveContainer(Container.SEARCH_EDIT)
                                searchState.value = SearchState.SEARCH_QUERY
                                prevSearchText.clear()
                                prevSearchText.append(textSearch.value)
                                isFocusedSearchTextField = true
                                viewModel.hideBottomNavigation()
                            }) {
                            findProducts(it)
                        }
                    }
                    else -> {}
                }
                when (activeContainer) {
                    Container.MAIN ->
                        ShowMessageCount {
                        openDialogMessages.value = true
                    }
                    Container.DETAIL -> {
                            Spacer(modifier = Modifier.width(4.dp))
                            ButtonFavorite(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .size(23.dp)
                                    .align(Alignment.CenterVertically),
                                checked = detailProduct.favorite > 0
                            ) {
                                detailProduct.favorite = if (it) 1 else 0
                                viewModel.setProductFavorite(detailProduct)
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                        Image(
                            imageVector = ImageVector.vectorResource(R.drawable.ic_share),
                            contentDescription = null,
                            colorFilter = ColorFilter.tint(ImageFavoriteOff),
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .align(Alignment.CenterVertically)
                                .clickable {

                                    val subject = getStringResource(R.string.text_subjectshare)
                                    val extraText = detailProduct.name
                                    val intent = Intent(Intent.ACTION_SEND)
                                    intent.type = "text/plain"
                                    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
                                    intent.putExtra(Intent.EXTRA_TEXT, extraText)

                                    ContextCompat.startActivity(
                                        context,
                                        Intent.createChooser(intent, getStringResource(id = R.string.text_share)),
                                        null
                                    )
                                }
                        )
                    }
                    else -> {}
                }
            }
        }
    }) {
        Box(
                modifier = Modifier
                    //.verticalScroll(verticalScrollState)
                    .padding(it)
                    .nestedScroll(nestedScrollConnection)
                    .fillMaxSize()
                    .background(BgScreenDark),
            ) {
                if (products.isNotEmpty()) {
                   // val verticalScrollState = rememberScrollState()
                    //log("first Index = ${stateGrid.firstVisibleItemIndex}")
                    //var calcHeight = 0

                    val isShowButtonMore = !isActiveContainer(Container.SEARCH) && !prevContainerSearch()
                    LazyVerticalGrid(
                        modifier = Modifier
                            .padding(horizontal = 10.dp),
                        columns = GridCells.Fixed(if (OrderDisplay.getViewMode() == VIEW_MODE.CARD) 2 else 1),
                        state = stateGrid,
                        contentPadding = PaddingValues(top = 40.dp),
                    ) {
                        items(products, { product -> product.id }) { product ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .animateItemPlacement(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                CardProduct(
                                product, showMoreButton = isShowButtonMore, state = state, modeview = OrderDisplay.getViewMode()){selectedProduct ->
                                    detailProduct.copydata(selectedProduct)
                                    viewModel.hideBottomNavigation()
                                    showFloatingButton = false
                                    setActiveContainer(Container.DETAIL)
                                }
                            }
                            val nextPart = remember {
                                derivedStateOf {
                                    val total: Int = products.size / SIZE_PORTION
                                    val remains    = products.size % SIZE_PORTION
                                    val upload = if (remains > 0) false
                                    else total > 0
                                    upload && stateGrid.layoutInfo.visibleItemsInfo.lastOrNull()?.index == stateGrid.layoutInfo.totalItemsCount - 1
                                            && stateGrid.layoutInfo.viewportEndOffset - stateGrid.layoutInfo.visibleItemsInfo.last().offset.y >= stateGrid.layoutInfo.visibleItemsInfo.last().size.height / 2
                                }
                            }
                            LaunchedEffect(nextPart.value) {
                                if (nextPart.value)
                                    viewModel.getNextPortionData(isSearchMode, textSearch.value.trim())
                            }
                        }
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
                            var scrollUp = false
                            if (!upperLimit) {
                                val deltaIndex = firstIndex - prevFirstIndex
                                val deltaOffset  = if (firstIndex > prevFirstIndex)
                                                        0
                                                   else
                                                        firstOffset - prevFirstOffset
                                if (deltaOffset < 0 || deltaIndex < 0)
                                    scrollUp = true
                            }
                            prevStateScroll = firstIndex to firstOffset
                            scrollUp
                        }
                        }
                        showFloatingButton = changeVisibleStateFAB.value
                    }

                ShowDataDisplayPanel(modifier = Modifier
                    .offset { IntOffset(x = 0, y = panelOffsetHeightPx.value.roundToInt()) }
                    .fillMaxWidth()
                    .height(40.dp)
                    .background(MaterialTheme.colors.primary),
                ) {index ->
                    val SHOW_FILTER  = 1
                    val CHANGE_ORDER = 0
                    if (index == SHOW_FILTER) {
                        viewModel.hideBottomNavigation()
                        setActiveContainer(Container.FILTER)
                        showFloatingButton = false
                    }
                    if (index == CHANGE_ORDER) {
                        viewModel.filterProducts(isActiveContainer(Container.SEARCH))//searchScreenDisplayed())
                    }
                }
                }

            AnimatedVisibility(
                visible = isActiveContainer(Container.DETAIL),
                enter = fadeIn(
                    animationSpec = tween(800)
                ),
                exit = fadeOut(
                    animationSpec = tween(500)
                )
            ) {
                ShowDetailProduct(detailProduct)
            }

            AnimatedVisibility(
                        visible = isFocusedSearchTextField,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        ShowSearchHistory(textSearch, searchState)//lastSearchQuery)
                    }

            if (isActiveContainer(Container.FILTER))
            AnimatedVisibility(
                visible = isActiveContainer(Container.FILTER),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                ShowFilterDisplay(OrderDisplay.getInstance(), reset = {
                    panelOffsetHeightPx.value = 0f
                    showBottomNavigation()
                    val result = OrderDisplay.resetFilter()
                    if (result == 0) {
                        viewModel.filterProducts(isActiveContainer(Container.SEARCH))//searchScreenDisplayed())
                    }
                }) {filter ->
                    panelOffsetHeightPx.value = 0f
                    showBottomNavigation()
                    val changedFilterData   = !OrderDisplay.equalsFilterData(filter)
                    OrderDisplay.setFilter(filter)
                    if (changedFilterData) {
                        viewModel.filterProducts(isActiveContainer(Container.SEARCH))//searchScreenDisplayed())
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
            )
        }
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
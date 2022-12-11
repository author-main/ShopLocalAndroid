package com.training.shoplocal.screens.mainscreen

import android.R.attr.data
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.training.shoplocal.*
import com.training.shoplocal.R
import com.training.shoplocal.classes.Product
import com.training.shoplocal.dialogs.ShowMessage
import com.training.shoplocal.ui.theme.*
import com.training.shoplocal.viewmodel.RepositoryViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*


@OptIn(ExperimentalMaterialApi::class, ExperimentalAnimationApi::class)
@Composable
fun MainScreen(state: ModalBottomSheetState){

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


/*        @Composable
        fun AnimateMessage(count: Int, content: @Composable () -> Unit) {
        }*/
        val MAX_SIZE = 32f
        val scope = rememberCoroutineScope()
        val animate  = remember{ Animatable(0f) }
        val animate1 = remember{ Animatable(0f) }
        val animate2 = remember{ Animatable(0f) }
        val align = remember{ mutableStateOf( Alignment.Center)}
        val count = remember{ mutableStateOf(0) }
        count.value = value
        //log ("${18.toPx}, ${32.toPx}")

      //      log("recomposition")
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
                    //log("animation")
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

                      //  scope.launch {
                            animate1.animateTo(
                                targetValue = 18f,
                                animationSpec = tween(//delayMillis = 1200,
                                    durationMillis = 200)
                            )
                        /*animate.animateTo(targetValue = 0f, tween(delayMillis = 0, durationMillis = 0))
                        animate1.animateTo(targetValue = 0f, tween(delayMillis = 0, durationMillis = 0))
                        animate2.animateTo(targetValue = 0f, tween(delayMillis = 0, durationMillis = 0))
                        align.value = Alignment.Center
                        animated.value = false*/
                    }
                    //animated.value = false
                    /*animate.animateTo(targetValue = 0f)
                    animate1.animateTo(targetValue = 0f)
                    animate2.animateTo(targetValue = 0f)
                    //align.value = Alignment.Center
                    animated.value = false*/
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
                            .background(color = BgDiscount.copy(alpha = animate1.value / 18), shape = CircleShape),
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
/*        SideEffect {
            //Thread.sleep(1000)
            if (!animated.value)
                animated.value = true
            //Thread.sleep(1000)
            //animate = true
            /*if (animate)
                animateBigCircle.targetState = true*/
        }*/

    }


    //val scope = rememberCoroutineScope()
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

    Column(modifier = Modifier.fillMaxWidth()) {
    TopAppBar(backgroundColor = MaterialTheme.colors.primary) {
        Row(
            Modifier
                .padding(horizontal = 4.dp)
                .fillMaxWidth(),
        horizontalArrangement = Arrangement.End) {
            /*val interaction = remember {
                MutableInteractionSource()
            }*/
            BasicTextField(
                modifier = Modifier
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
                                        if (showClearIcon)
                                            textSearch.value = ""
                                        else {
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
            ShowMessageCount(31)
        }
    }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BgScreenDark)
        ) {
            val stateGrid = rememberLazyGridState()
            if (products.isNotEmpty()) {
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 10.dp),
                    columns = GridCells.Adaptive(minSize = 150.dp),
                    state = stateGrid,
                    contentPadding = PaddingValues(10.dp),
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
                    stateGrid.layoutInfo.visibleItemsInfo.lastOrNull()?.index == stateGrid.layoutInfo.totalItemsCount - 1
                            //&& stateGrid.isScrollInProgress
                            && stateGrid.layoutInfo.viewportEndOffset - stateGrid.layoutInfo.visibleItemsInfo.last().offset.y >= stateGrid.layoutInfo.visibleItemsInfo.last().size.height / 2
                }
            }
            LaunchedEffect(nextPart.value) {
                if (nextPart.value) {
                    viewModel.getNextPortionData()
                }
            }
        }

    }
    if (dataSnackbar.second) {
        ShowMessage(message = dataSnackbar.first, viewModel = viewModel, type = dataSnackbar.third)//, type = MESSAGE.WARNING)
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
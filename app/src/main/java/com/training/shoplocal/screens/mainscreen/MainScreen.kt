package com.training.shoplocal.screens.mainscreen

import android.widget.ImageButton
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.training.shoplocal.R
import com.training.shoplocal.classes.Product
import com.training.shoplocal.log
import com.training.shoplocal.screens.appscreen.BottomSheet
import com.training.shoplocal.ui.theme.*
import com.training.shoplocal.viewmodel.RepositoryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterialApi::class)
@Composable
//fun MainScreen(state: ModalBottomSheetState, scope: CoroutineScope){
fun MainScreen(state: ModalBottomSheetState){
    //var part by remember {mutableStateOf(1)}
    //val state = rememberModalBottomSheetState(initialValue = ModalBottomSheetValue.Hidden)
    //val scope = rememberCoroutineScope()
    val viewModel: RepositoryViewModel = viewModel()
    //val products: MutableList<Product> by viewModel.products.collectAsState()
    //BottomSheet(state) {
    Column(modifier = Modifier.fillMaxWidth()) {
    TopAppBar(backgroundColor = MaterialTheme.colors.primary) {
        val textSearch = remember {
            mutableStateOf("")
        }
        val interaction = remember {
            MutableInteractionSource()
        }
        BasicTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp)
                .background(color = TextFieldBg, shape = RoundedCornerShape(32.dp)),
            cursorBrush = SolidColor(TextFieldFont),
            value = textSearch.value,
            textStyle = TextStyle(color = TextFieldFont),
            onValueChange = {
                textSearch.value = it
            },
            singleLine = true,

            /*colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Red,
                cursorColor = TextFieldFont,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),*/


            decorationBox = { innerTextField ->
               /* Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = TextFieldBg, shape = RoundedCornerShape(32.dp)),
                   // verticalAlignment = Alignment.CenterVertically,
                ) {*/
                    //if (value.isEmpty()) {
                    /* Text(
                            text = "checkit",
                            //color = if (isSystemInDarkTheme()) Color(0xFF969EBD) else Color.Gray,
                            fontSize = 14.sp
                        )*/
                    // }
                    /*if (textSearch.value.isEmpty())
                        Text(
                            modifier = Modifier.padding(start = 8.dp),
                            text = stringResource(id = R.string.text_search))*/
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
                             Icon(imageVector = ImageVector.vectorResource(R.drawable.ic_search),
                                 contentDescription = null)
                                       },
                         trailingIcon = {
                             val showClearIcon = textSearch.value.isNotEmpty()
                             val iconSize = if (showClearIcon) 16.dp else 24.dp
                             Icon(
                                 imageVector = if (showClearIcon)
                                    ImageVector.vectorResource(R.drawable.ic_cancel_bs)
                                 else
                                    ImageVector.vectorResource(R.drawable.ic_microphone)

                                     ,
                                 contentDescription = null,
                                 modifier = Modifier
                                     .clip(CircleShape)
                                     .size(iconSize)
                                     .clickable/*(interactionSource = remember { MutableInteractionSource() },
                                            indication = rememberRipple(radius = 16.dp)) */{
                                         /*val value: Byte = if (product.favorite > 0) 0 else 1
                                         product.favorite = value//if (isFavorited.value) 1 else 0
                                         action(value > 0)*/
                                        if (showClearIcon)
                                            textSearch.value = ""
                                        else {
                                            // Вызвать голосовой ввод
                                        }
                                     }
                                 )
                         },

                        visualTransformation = VisualTransformation.None,
                        innerTextField = innerTextField,
                        singleLine = true,
                        enabled = true,
                        interactionSource = interaction,
                        contentPadding = PaddingValues(0.dp)
                    )
                    //innerTextField()
               // }
            })

/*
            Modifier
                //.focusRequester(focusRequester)
               /* .border(1.dp, borderColor[0], RoundedCornerShape(16.dp))
                .onFocusChanged {
                    if (it.isFocused || it.hasFocus)
                        errors[0] = false
                    /*  else {
                        //log(it.toString())
                        errors[0] = !validateMail(email.value)
                    }*/

                }*/
                .fillMaxWidth(),
            //.padding(horizontal = 8.dp),

       /*     trailingIcon = {
                Image(
                    imageVector = ImageVector.vectorResource(idDrawable),
                    colorFilter = ColorFilter.tint(MaterialTheme.colors.secondary),
                    contentDescription = null
                )
            },*/
            //textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = TextFieldBg,
                cursorColor = TextFieldFont,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(32.dp),
            singleLine = true,
            /*keyboardActions = KeyboardActions (
                onDone = {
                    keyboardController?.hide()
                    //  focusManager.clearFocus()
                  }
            ),*/
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
                keyboardType = KeyboardType.Email
            )*/
      //  )
    }


        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BgScreenDark)
        ) {
            val products: MutableList<Product> by viewModel.products.collectAsState()
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
                        //log("recomposition Grid")
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
 }
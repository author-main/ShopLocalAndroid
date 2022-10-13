package com.training.shoplocal.dialogs

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onPlaced
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.training.shoplocal.MESSAGE
import com.training.shoplocal.R
import com.training.shoplocal.log
import com.training.shoplocal.ui.theme.*
import com.training.shoplocal.viewmodel.RepositoryViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
@Composable
fun <T:ViewModel> ShowMessage(message: String, type: MESSAGE = MESSAGE.INFO, viewModel:T){
    //val scope = rememberCoroutineScope()
   // val labelFont = FontFamily(Font(R.font.robotocondensed_light))
    val snackbarHostState = remember { mutableStateOf(SnackbarHostState()) }
    var color = TextFieldBg
    when (type) {
        MESSAGE.INFO ->{
            color = SelectedItem
        }
        MESSAGE.WARNING ->{

        }
        MESSAGE.ERROR ->{

        }
    }

    LaunchedEffect(null) {
        when (snackbarHostState.value.showSnackbar(message)) {
            SnackbarResult.Dismissed -> {
                (viewModel as RepositoryViewModel).showSnackbar(visible = false)
            }
            SnackbarResult.ActionPerformed -> {

            }
        }
    }


        Box(Modifier.fillMaxSize()){
            SnackbarHost(
                modifier = Modifier.align(Alignment.BottomCenter),
                hostState = snackbarHostState.value,
                snackbar = { snackbarData: SnackbarData ->
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        //border = BorderStroke(1.dp, TextFieldFont),
                        backgroundColor = color,//TextFieldBg,
                        modifier = Modifier
                            .padding(16.dp)
                            .wrapContentSize()
                        //    .background(TextOrange)
                        //.align(Alignment.BottomCenter)

                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(modifier = Modifier.size(48.dp)
                                .border(width = 2.dp, color = TextLightGray, CircleShape)
                                .padding(8.dp),
                                imageVector = Icons.Default.Notifications, contentDescription = ""
                            )
                            Text(
                                text = snackbarData.message,
                                //fontFamily = labelFont,
                                textAlign = TextAlign.Center,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            )
        }
 }
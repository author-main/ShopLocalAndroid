package com.training.shoplocal.dialogs

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.training.shoplocal.MESSAGE
import com.training.shoplocal.log
import kotlinx.coroutines.launch
@Composable
fun ShowMessage(message: String, type: MESSAGE = MESSAGE.INFO){
    //val scope = rememberCoroutineScope()
    val snackbarHostState = remember { mutableStateOf(SnackbarHostState()) }
    when (type) {
        MESSAGE.INFO ->{
            LaunchedEffect(null) {
                snackbarHostState.value.showSnackbar(message)
            }
        }
        MESSAGE.WARNING ->{

        }
        MESSAGE.ERROR ->{

        }
    }
        Box(Modifier.fillMaxSize()) {
            SnackbarHost(
                modifier = Modifier.align(Alignment.BottomCenter),
                hostState = snackbarHostState.value,
                snackbar = { snackbarData: SnackbarData ->
                    Card(
                        shape = RoundedCornerShape(8.dp),
                        //border = BorderStroke(2.dp, Color.White),
                        modifier = Modifier
                            .padding(16.dp)
                            .wrapContentSize()
                        //.align(Alignment.BottomCenter)

                    ) {
                        Column(
                            modifier = Modifier.padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(imageVector = Icons.Default.Notifications, contentDescription = "")
                            Text(text = snackbarData.message)
                        }
                    }
                }
            )
        }
 }
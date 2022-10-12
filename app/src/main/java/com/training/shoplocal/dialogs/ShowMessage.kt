package com.training.shoplocal.dialogs

import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.runtime.*
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
    SnackbarHost(snackbarHostState.value)
}
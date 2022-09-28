package com.training.shoplocal.dialogs

import android.widget.Toast
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.training.shoplocal.AppShopLocal.Companion.appContext
import com.training.shoplocal.DialogRouter
import com.training.shoplocal.R
import com.training.shoplocal.ui.theme.*
import com.training.shoplocal.validateMail
import com.training.shoplocal.viewmodel.RepositoryViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DialogRestore(){
    val viewModel: RepositoryViewModel = viewModel()
    val email = remember{mutableStateOf("")}
    val error = remember {
        mutableStateOf(true)
    }
    Dialog(onDismissRequest = {
        DialogRouter.reset()
       /* Toast.makeText(appContext(), "Dialog dismissed!", Toast.LENGTH_SHORT)
            .show()*/
    }) {
        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current
        Card(
            elevation = 8.dp,
            shape = RoundedCornerShape(12.dp),
            backgroundColor = PrimaryDark,
            contentColor = TextLightGray
        ) {

            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    text = stringResource(id = R.string.title_restore),
                    //fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    modifier = Modifier.padding(8.dp),
                    style = MaterialTheme.typography.h1
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = email.value,
                    onValueChange = {
                        email.value = it
                    },
                    Modifier.onFocusChanged {
                        if (it.isFocused)
                            error.value = false
                    }

                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    trailingIcon = {
                        if (error.value)
                            Icon(Icons.Filled.Email, contentDescription = "", tint = SelectedItem)
                        else
                            Icon(Icons.Filled.Email, contentDescription = "")
                    },
                    //textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = TextFieldBg,
                        cursorColor = TextFieldFont,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    keyboardActions = KeyboardActions (
                        onDone = {
                            keyboardController?.hide()
                            //  focusManager.clearFocus()
                          }
                    ),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Email),
                )
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)

                ) {
                    TextButton(onClick = {
                        DialogRouter.reset()
                    }) {
                        Text(text = stringResource(id = R.string.button_cancel).uppercase(),
                            color = TextOrange
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    TextButton(onClick = {
                        if (!validateMail(email.value)) {
                            error.value = true
                            focusManager.clearFocus()
                        }
                        else {
                            viewModel.onRestoreUser(email.value)
                            DialogRouter.reset()
                        }
                    }) {
                        Text(text = stringResource(id = R.string.btn_send).uppercase(),
                            color = TextOrange
                        )
                    }
                }
            }
        }
        
    }
    
    
    
    
    
  /*  Dialog(onDismissRequest = { }) {
        
    }*/
    
    /*
    AlertDialog(backgroundColor = Color.DarkGray,
        contentColor = TextLightGray,//contentColorFor(backgroundColor),
        shape = RoundedCornerShape(16.dp),
        onDismissRequest = {
           DialogRouter.reset()
        },
        title = { Text(text = stringResource(R.string.title_restore))},
        text = {
            Column(Modifier.fillMaxHeight()) {
                //Spacer(modifier = Modifier.height(64.dp))
                TextField(
                    value = email.value,
                    onValueChange = {
                        email.value = it
                    },
                    Modifier
                        .fillMaxWidth(),
                    trailingIcon = {
                        /*  if (errorEmail.value)
                        Icon(Icons.Filled.Email, contentDescription = "", tint = SelectedItem)
                    else
                        Icon(Icons.Filled.Email, contentDescription = "")*/
                    },
                    textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = TextFieldBg,
                        cursorColor = TextFieldFont,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                )



            }
        },
        buttons = {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.End,
            ) {
                OutlinedButton(
                    onClick = {
                        DialogRouter.reset()
                    },
                    border = null,
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)

                ) {
                    Text( stringResource(id = R.string.button_cancel).uppercase(), color = TextOrange)
                }
                OutlinedButton(
                    modifier = Modifier.padding(end = 16.dp),
                    onClick = {
                        val repository = viewModel.getRepository()
                        val email = repository.getEmail()
                        repository.onRestoreUser(email)
                        DialogRouter.reset()
                    },
                    border = null,
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent)
                ) {
                    Text(stringResource(id = R.string.btn_send).uppercase(), color = TextOrange)
                }

            }
        }
    )*/
}

/*@Preview(showBackground = true)
@Composable
fun DialogRestorePreview() {
    DialogRestore()
}*/
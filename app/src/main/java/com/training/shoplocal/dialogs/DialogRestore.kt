package com.training.shoplocal.dialogs

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.training.shoplocal.DialogRouter
import com.training.shoplocal.R
import com.training.shoplocal.ui.theme.*
import com.training.shoplocal.validateMail
import com.training.shoplocal.viewmodel.RepositoryViewModel

@Composable
fun DialogRestore(){
    val viewModel: RepositoryViewModel = viewModel()
    val email = remember{mutableStateOf("")}
  /*  Dialog(onDismissRequest = { }) {
        
    }*/
    
    
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
    )
}

/*@Preview(showBackground = true)
@Composable
fun DialogRestorePreview() {
    DialogRestore()
}*/
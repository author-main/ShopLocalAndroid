package com.training.shoplocal.dialogs

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.training.shoplocal.DialogRouter
import com.training.shoplocal.R
import com.training.shoplocal.ui.theme.*
import com.training.shoplocal.viewmodel.RepositoryViewModel

@Composable
fun DialogRestore(){
    val viewModel: RepositoryViewModel = viewModel()
    AlertDialog(backgroundColor = Color.DarkGray,
        contentColor = TextLightGray,//contentColorFor(backgroundColor),
        shape = RoundedCornerShape(16.dp),
        onDismissRequest = {
           DialogRouter.reset()
        },
        title = { Text(text = stringResource(R.string.title_restore))},
        text = { Text("Вы действительно хотите удалить выбранный элемент?") },
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
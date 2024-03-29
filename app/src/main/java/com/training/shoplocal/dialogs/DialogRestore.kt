package com.training.shoplocal.dialogs

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.training.shoplocal.*
import com.training.shoplocal.R
import com.training.shoplocal.classes.User
import com.training.shoplocal.ui.theme.*
import com.training.shoplocal.viewmodel.RepositoryViewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DialogRestore() {
    val labelFont = FontFamily(Font(R.font.robotocondensed_light))
    val viewModel: RepositoryViewModel = viewModel()
    val usermail = User.getUserData()?.email ?: ""
    val email = remember { mutableStateOf(usermail) }
    val password = remember { mutableStateOf("") }
    val showchar = remember { mutableStateOf(false) }
    val errors = remember { mutableStateListOf<Boolean>(false, false) }
    val trailingIcon = @Composable {
        val idDrawable = if (showchar.value)
            R.drawable.ic_showsym_on
        else
            R.drawable.ic_showsym_off
        Image(
            modifier = Modifier
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null
                ) {
                    showchar.value = !showchar.value
                },
            imageVector = ImageVector.vectorResource(idDrawable),
            colorFilter = ColorFilter.tint(MaterialTheme.colors.secondary),
            contentDescription = null
        )
    }

    Dialog(onDismissRequest = {
        DialogRouter.reset()
    }) {
        var progress by remember {
            mutableStateOf(false)
        }
        val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current
        val borderColor = arrayOf(Color.Transparent, Color.Transparent)
        for (i in 0..1)
            borderColor[i] = if (errors[i])
                SelectedItem
            else Color.Transparent
        Card(
            elevation = 8.dp,
            shape = RoundedCornerShape(12.dp),
            backgroundColor = PrimaryDark,
            contentColor = TextLightGray
        ) {
            Column(
                modifier = Modifier.padding(
                    top = 8.dp,
                    bottom = 8.dp,
                    start = 16.dp,
                    end = 16.dp
                )
            ) {
                Text(
                    text = stringResource(id = R.string.title_restore),
                    fontSize = 17.sp,
                    modifier = Modifier.padding(8.dp),
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = email.value,
                    onValueChange = {
                        email.value = it
                    },
                    Modifier
                        .border(1.dp, borderColor[0], RoundedCornerShape(16.dp))
                        .onFocusChanged {
                            if (it.isFocused || it.hasFocus)
                                errors[0] = false
                        }

                        .fillMaxWidth(),
                    trailingIcon = {
                        if (errors[0])
                            Icon(Icons.Filled.Email, contentDescription = "", tint = SelectedItem)
                        else
                            Icon(Icons.Filled.Email, contentDescription = "")
                    },
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = TextFieldBg,
                        cursorColor = TextFieldFont,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Next,
                        keyboardType = KeyboardType.Email
                    ),
                )
                Row(
                    Modifier.padding(top = 8.dp, bottom = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(id = R.string.text_newpassword),
                        fontFamily = labelFont
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    TextField(
                        value = password.value,
                        onValueChange = { value ->
                            val regExp = "\\d{0,5}".toRegex()
                            if (regExp.matches(value))
                                password.value = value
                        },
                        modifier = Modifier
                            .border(1.dp, borderColor[1], RoundedCornerShape(16.dp))
                            .onFocusChanged {
                                if (it.isFocused || it.hasFocus)
                                    errors[1] = false
                            },
                        colors = TextFieldDefaults.textFieldColors(
                            backgroundColor = TextFieldBg,
                            cursorColor = TextFieldFont,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent
                        ),
                        trailingIcon = trailingIcon,
                        shape = RoundedCornerShape(16.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.NumberPassword
                        ),
                        visualTransformation = if (showchar.value)
                            VisualTransformation.None
                        else PasswordVisualTransformation(),
                        keyboardActions = KeyboardActions(
                            onDone = {
                                keyboardController?.hide()
                            }
                        ),
                    )

                }
                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(all = 8.dp)

                ) {
                    val message = stringResource(id = R.string.message_restore_email)
                    TextButton(onClick = {
                        DialogRouter.reset()
                    }) {
                        Text(
                            text = stringResource(id = R.string.button_cancel).uppercase(),
                            color = TextOrange
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    TextButton(onClick = {
                        focusManager.clearFocus()
                        errors[0] = !validateMail(email.value)
                        errors[1] = password.value.length < 5
                        if (!errors[0] && !errors[1]) {
                            progress = true
                            viewModel.onRestoreUser(
                                action = { result ->
                                    progress = false
                                    if (result) {
                                        viewModel.setEmail(email.value)
                                        viewModel.removePassword()
                                        viewModel.getLoginState().checkFingerButtonState()
                                        DialogRouter.reset()
                                        viewModel.showSnackbar(message)
                                    }
                                },
                                email.value,
                                password.value
                            )
                        }
                    }) {
                        Text(
                            text = stringResource(id = R.string.btn_send).uppercase(),
                            color = TextOrange
                        )
                    }
                }
            }
        }
        if (progress)
            ShowProgress()
    }
}

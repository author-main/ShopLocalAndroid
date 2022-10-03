package com.training.shoplocal.dialogs

import android.text.TextPaint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
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
import androidx.core.text.isDigitsOnly
import com.training.shoplocal.DialogRouter
import com.training.shoplocal.R
import com.training.shoplocal.log
import com.training.shoplocal.ui.theme.*


@Composable
fun DialogRegistration(){

    //LocalConfiguration.current.screenWidthDp.dp
    val showChar  = remember{mutableStateOf(false)}
    val labelFont = FontFamily(Font(R.font.robotocondensed_light))
    val family    = remember{mutableStateOf("")}
    val name      = remember{mutableStateOf("")}
    val phone     = remember{mutableStateOf("")}
    val email     = remember{mutableStateOf("")}
    val password  = remember{mutableStateOf("")}

    @Composable
    fun TextGroup(label: String, text: MutableState<String>, keyboardType: KeyboardType = KeyboardType.Text, onTextChange: (value: String)-> Unit = { }){
        val visualTransformation =
            if (keyboardType == KeyboardType.NumberPassword && !showChar.value)
                    PasswordVisualTransformation()
                else
                    VisualTransformation.None
        Row(verticalAlignment = Alignment.CenterVertically){
            Text(text = label, fontFamily = labelFont, modifier = Modifier.width(70.dp))
            //Spacer(modifier = Modifier.width(8.dp))
            TextField(value = text.value, onValueChange = onTextChange,
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = TextFieldBg,
                    cursorColor = TextFieldFont,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(16.dp),
                trailingIcon = {
                    if (keyboardType == KeyboardType.NumberPassword) {
                        val idDrawable = if (showChar.value)
                            R.drawable.ic_showsym_on
                        else
                            R.drawable.ic_showsym_off
                        Image(
                            modifier = Modifier
                                .clickable (
                                    onClick = {
                                        showChar.value = !showChar.value
                                    }
                                )
                                .size(24.dp, 24.dp),
                            imageVector = ImageVector.vectorResource(idDrawable),
                            colorFilter = ColorFilter.tint(MaterialTheme.colors.secondary),
                            contentDescription = null
                        )
                    }
                },
                singleLine = true,
                visualTransformation = visualTransformation,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = keyboardType)
            )
        }
    }


    Dialog(onDismissRequest = {
        DialogRouter.reset()
        /* Toast.makeText(appContext(), "Dialog dismissed!", Toast.LENGTH_SHORT)
             .show()*/
    }) {
        /*  val focusManager = LocalFocusManager.current
        val keyboardController = LocalSoftwareKeyboardController.current*/
        Card(
            elevation = 8.dp,
            shape = RoundedCornerShape(12.dp),
            backgroundColor = PrimaryDark,
            contentColor = TextLightGray
        ) {


            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(id = R.string.title_reg),
                    fontSize = 17.sp,
                    modifier=Modifier.padding(bottom = 8.dp)
                )
                TextGroup(label = stringResource(id = R.string.text_family),    text = family,
                    onTextChange = {value -> family.value = value})
                TextGroup(label = stringResource(id = R.string.text_name),      text = name,
                    onTextChange = {value -> name.value = value})
                TextGroup(label = stringResource(id = R.string.text_phone),     text = phone, keyboardType = KeyboardType.Phone,
                    onTextChange = {
                        val firstCharValid = try {
                            it[0] == '+' || it[0].isDigit()
                        } catch(e: java.lang.Exception){
                            false
                        }

                        if (it.isEmpty())
                            phone.value = it
                        else
                        if (firstCharValid) {
                            val str = if (it[0] == '+')
                                it.substring(1)
                            else
                                it.substring(0)
                            if (str.isDigitsOnly() && str.length <=11)
                                phone.value = it
                        }

                    })
                TextGroup(label = stringResource(id = R.string.text_email),     text = email, keyboardType = KeyboardType.Email,
                    onTextChange = {value -> email.value = value})
                TextGroup(label = stringResource(id = R.string.text_password),  text = password, keyboardType = KeyboardType.NumberPassword,
                    onTextChange = {value ->
                        val regExp = "\\d{0,5}".toRegex()
                        if (regExp.matches(value))
                            password.value = value
                    })
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
                            DialogRouter.reset()
                    }) {
                        Text(text = stringResource(id = R.string.btn_reg).uppercase(),
                            color = TextOrange
                        )
                    }
                }
            }
        }
    }

}

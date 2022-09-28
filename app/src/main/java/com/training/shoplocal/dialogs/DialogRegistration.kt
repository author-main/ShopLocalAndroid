package com.training.shoplocal.dialogs

import android.text.TextPaint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.training.shoplocal.DialogRouter
import com.training.shoplocal.R
import com.training.shoplocal.ui.theme.*


@Composable
fun DialogRegistration(){

    //LocalConfiguration.current.screenWidthDp.dp
    val labelFont = FontFamily(Font(R.font.robotocondensed_light))
    val family    = remember{mutableStateOf("")}
    val name      = remember{mutableStateOf("")}
    val phone     = remember{mutableStateOf("")}
    val email     = remember{mutableStateOf("")}
    val password  = remember{mutableStateOf("")}

    @Composable
    fun TextGroup(label: String, text: MutableState<String>, onTextChange: (value: String)-> Unit = { }){
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
                singleLine = true
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
                TextGroup(label = stringResource(id = R.string.text_phone),     text = phone,
                    onTextChange = {value -> phone.value = value})
                TextGroup(label = stringResource(id = R.string.text_email),     text = email,
                    onTextChange = {value -> email.value = value})
                TextGroup(label = stringResource(id = R.string.text_password),  text = password,
                    onTextChange = {value -> password.value = value})
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

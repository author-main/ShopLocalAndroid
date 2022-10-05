package com.training.shoplocal.dialogs

import android.util.Patterns
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.training.shoplocal.DialogRouter
import com.training.shoplocal.R
import com.training.shoplocal.log
import com.training.shoplocal.ui.theme.*
import com.training.shoplocal.validateMail
import com.training.shoplocal.viewmodel.RepositoryViewModel


@Composable
fun DialogRegistration(){
    val errors = remember{ mutableStateOf(List(5){false})}

  /*  val error = remember {
        mutableStateListOf(List(5){false})
    }*/


    //LocalConfiguration.current.screenWidthDp.dp
    val focusRequesters = List(5) { FocusRequester() }
    val showChar  = remember{mutableStateOf(false)}
    val labelFont = FontFamily(Font(R.font.robotocondensed_light))
    val dataUser  = List(5){remember{mutableStateOf("")}}
    /*val family    = remember{mutableStateOf("")}
    val name      = remember{mutableStateOf("")}
    val phone     = remember{mutableStateOf("")}
    val email     = remember{mutableStateOf("")}
    val password  = remember{mutableStateOf("")}*/

    fun changeError(order: Int, value: Boolean) {
        val tError = errors.value.toMutableList()
        tError[order] = value
        errors.value = tError.toList()
    }

    @Composable
    fun TextGroup(label: String, text: MutableState<String>, keyboardType: KeyboardType = KeyboardType.Text, onTextChange: (value: String)-> Unit = { }, order: Int){
        val trailingIcon = @Composable {
                val idDrawable = if (showChar.value)
                    R.drawable.ic_showsym_on
                else
                    R.drawable.ic_showsym_off
                Image(
                    modifier = Modifier
                        .clickable(
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

        val focusManager = LocalFocusManager.current
        val borderColor = if (errors.value[order])
                        SelectedItem
                    else Color.Transparent
        val visualTransformation =
            if (keyboardType == KeyboardType.NumberPassword && !showChar.value)
                    PasswordVisualTransformation()
                else
                    VisualTransformation.None
        Row(verticalAlignment = Alignment.CenterVertically){
            Text(text = label, fontFamily = labelFont,
                modifier = Modifier.width(70.dp)
            )
            Column(){
            Spacer(modifier = Modifier.height(8.dp))
            TextField(value = text.value, onValueChange = onTextChange,
                Modifier
                    .onFocusChanged {
                        if (it.isFocused) {
                            if (errors.value[order])
                                changeError(order, false)
                        }
                    }
                    .border(1.dp, borderColor, RoundedCornerShape(16.dp))
                    .fillMaxWidth()
                    .focusRequester(focusRequesters[order])
                    .focusProperties {
                        val next = if (order < focusRequesters.size - 1)
                            focusRequesters[order + 1]
                        else
                            focusRequesters[0]
                        down = next
                    },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = TextFieldBg,
                    cursorColor = TextFieldFont,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(16.dp),
                trailingIcon = if (keyboardType == KeyboardType.NumberPassword)
                    trailingIcon else null,
                singleLine = true,
                visualTransformation = visualTransformation,
                keyboardActions = KeyboardActions (
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }/*,
                    onDone = {
                        //keyboardController?.hide()
                        //  focusManager.clearFocus()
                    }*/
                ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = keyboardType)
            )

            }
        }
    }


    Dialog(onDismissRequest = {
        DialogRouter.reset()
        /* Toast.makeText(appContext(), "Dialog dismissed!", Toast.LENGTH_SHORT)
             .show()*/
    }) {
        val viewModel: RepositoryViewModel = viewModel()
        //val keyboardController = LocalSoftwareKeyboardController.current
        Card(
            elevation = 8.dp,
            shape = RoundedCornerShape(12.dp),
            backgroundColor = PrimaryDark,
            contentColor = TextLightGray
        ) {


            val focusManager = LocalFocusManager.current

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = stringResource(id = R.string.title_reg),
                    fontSize = 17.sp,
                    modifier=Modifier.padding(bottom = 8.dp)
                )
                TextGroup(label = stringResource(id = R.string.text_family),    text = dataUser[0],//family,
                    onTextChange = {value -> dataUser[0].value = value}, order=0)
                TextGroup(label = stringResource(id = R.string.text_name),      text = dataUser[1],//name,
                    onTextChange = {value -> dataUser[1].value = value}, order=1)
                TextGroup(label = stringResource(id = R.string.text_phone),     text = dataUser[2],//phone,
                    keyboardType = KeyboardType.Phone,
                    onTextChange = {
                        value -> dataUser[2].value = value
                        /*val firstCharValid = try {
                            it[0] == '+' || it[0].isDigit()
                        } catch(e: java.lang.Exception){
                            false
                        }

                        if (it.isEmpty())
                            dataUser[2].value = it
                        else
                        if (firstCharValid) {
                            val str = if (it[0] == '+')
                                it.substring(1)
                            else
                                it.substring(0)
                            if (str.isDigitsOnly() && str.length <=11)
                                dataUser[2].value = it
                        }
*/
                    }, order=2)
                TextGroup(label = stringResource(id = R.string.text_email),     text = dataUser[3],//email,
                    keyboardType = KeyboardType.Email,
                    onTextChange = {value -> dataUser[3].value = value},order=3)
                TextGroup(label = stringResource(id = R.string.text_password),  text = dataUser[4],//password,
                    keyboardType = KeyboardType.NumberPassword,
                    onTextChange = {value ->
                        val regExp = "\\d{0,5}".toRegex()
                        if (regExp.matches(value))
                            dataUser[4].value = value
                    }, order=4)
                Spacer(modifier = Modifier.height(8.dp))
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
                            focusManager.clearFocus()
                            val tErrors = MutableList(5){false}
                            for (i in 0..4)
                                if (dataUser[i].value.isBlank())
                                    tErrors[i] = true

                            if (dataUser[4].value.length < 5)
                                tErrors[4] = true

                            if (!validateMail(dataUser[3].value))
                                tErrors[3] = true

                            val regExp = "^(8|\\+7)\\d{10}".toRegex()
                            if (!regExp.matches(dataUser[2].value))
                                tErrors[2] = true

                            /*if (!Patterns.PHONE.matcher(dataUser[2].value).matches())
                                tErrors[2] = true*/

                            var validate = true
                            tErrors.forEach foreach@{value ->
                                if (value) {
                                    validate = false
                                    return@foreach // возврат по метке, можно заменить на return@forEach без указания метки
                                }
                            }

                            if (validate) {
                                DialogRouter.reset()
                                viewModel.onRegisterUser(
                                    dataUser[0].value,
                                    dataUser[1].value,
                                    dataUser[2].value,
                                    dataUser[3].value,
                                    dataUser[4].value
                                )
                            }
                            else
                                errors.value = tErrors.toList()

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

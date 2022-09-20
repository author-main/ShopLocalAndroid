package com.training.shoplocal.buttonpanel

import androidx.compose.foundation.Image
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.training.shoplocal.passwordview.PasswordViewState
import com.training.shoplocal.ui.theme.TextLightGray
import kotlinx.coroutines.delay
import com.training.shoplocal.R
import com.training.shoplocal.ui.theme.TextOrange

@Composable
fun ButtonPanel(changeChar: (value: Char)-> Unit){
    Column(horizontalAlignment = Alignment.CenterHorizontally){
        for (i in 0..3) {
            Row(){
                for (j in 1..3 ) {
                    val st = i * 3 + j
//                    val char: Char= ((i * 3) + j).toChar()
                    var image = 0
                    val char = if (st > 9) {
                        when (st){
                            10 -> {
                                image = R.drawable.ic_fingerprint
                                ' '}
                            12 -> {
                                image = R.drawable.ic_backspace
                                '<'}
                            else -> '0'
                        }
                    }
                    else st.toString()[0]

                    OutlinedButton(
                        onClick = {changeChar.invoke(char)},
                        modifier = Modifier.size(64.dp, 64.dp),
                        border = null,
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp),
                    ) {
                        if (char != ' ' && char != '<')
                            Text(
                                text = char.toString(),
                                fontSize = 32.sp,
                                color = TextLightGray,
                                fontWeight = FontWeight.Light
                            )
                        else Image(imageVector = ImageVector.vectorResource(image),
                            modifier = Modifier.size(24.dp, 24.dp),
                            contentDescription = null)
                    }
                    if (j<3)
                        Spacer(modifier = Modifier.width(16.dp))
                }
            }
          /*  if (i<3)
                Spacer(modifier = Modifier.height(8.dp))*/
        }
        Row(verticalAlignment = Alignment.CenterVertically){
            OutlinedButton(
                onClick = {},
                border = null,
                shape = CircleShape,
                contentPadding = PaddingValues(8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.btn_reg),
                    color = TextOrange,
                    fontSize = 13.sp
                )
            }

            Text(
                modifier = Modifier
                    .padding(horizontal = 4.dp),
                text = "|",
                color = TextLightGray,
                fontSize = 14.sp,
            )

            OutlinedButton(
                onClick = {},
                border = null,
                shape = CircleShape,
                contentPadding = PaddingValues(8.dp)
            ) {
                Text(
                    text = stringResource(id = R.string.btn_rest),
                    color = TextOrange,
                    fontSize = 13.sp
                )
            }

        }
    }

    /*
     LaunchedEffect(true) {
         delay(1000)
         changeChar.invoke('<')
     }*/
}

/*
fun ButtonPanel(onClick: ((char:Char) -> Unit)?){
    onClick?.invoke(char)
}
 */
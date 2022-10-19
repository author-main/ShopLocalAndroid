package com.training.shoplocal.screens.bottomnavigation

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.training.shoplocal.R
import com.training.shoplocal.TEXT_BOTTOMNAVIGATION
import com.training.shoplocal.log
import com.training.shoplocal.ui.theme.TextFieldFont
import com.training.shoplocal.ui.theme.TextOrange
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch


enum class BottomNavigationItemData(@DrawableRes var icon: Int, val text: String) {
    MAIN        (R.drawable.ic_main,        TEXT_BOTTOMNAVIGATION[0]),
    CATALOG     (R.drawable.ic_catalog,     TEXT_BOTTOMNAVIGATION[1]),
    CART        (R.drawable.ic_cart,        TEXT_BOTTOMNAVIGATION[2]),
    PROFILE     (R.drawable.ic_profile,     TEXT_BOTTOMNAVIGATION[3])
}

sealed class BottomNavigationItem(val route: String, val data: BottomNavigationItemData){
    object Main:        BottomNavigationItem("route_main", BottomNavigationItemData.MAIN)
    object Catalog:     BottomNavigationItem("route_catalog", BottomNavigationItemData.CATALOG)
    object Cart:        BottomNavigationItem("route_cart", BottomNavigationItemData.CART)
    object Profile:     BottomNavigationItem("route_profile", BottomNavigationItemData.PROFILE)
}

@Composable
fun IconWithText(value: BottomNavigationItemData, selected: Boolean = false,
                 interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
                 action: ()->Unit = {}){
   /* val defaulScale = remember {
        mutableStateOf(1.2f)
    }*/
    val scope = rememberCoroutineScope()
    val animate = remember{ Animatable(1.2f) }
    val labelFont = FontFamily(Font(R.font.robotocondensed_light))
    /*val scale:Float = if (selected)
                    animate.value
                else
                    1.0f*/

    /*val valueScale by remember {
        derivedStateOf {
            if (!selected && animate.value != 1f)
                1.7f
            else
                animate.value
        }
    }*/

  /*  val scale = if (selected)
        defaulScale.value
    else
        1f*/

    Column(
        Modifier
            .fillMaxHeight()
            .padding(horizontal = 24.dp)
            .graphicsLayer(scaleX = if (selected) animate.value else 1f, scaleY = if (selected) animate.value else 1f)
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) {
                if (!selected) {

                    scope.launch {
                       /* animate.animateTo(
                            targetValue = 0.9f,
                            animationSpec = tween(80)
                        )*/
                        animate.animateTo(
                            targetValue = 1.4f,
                            animationSpec = tween(150)
                        )
                        animate.animateTo(
                            targetValue = 1.2f,
                            animationSpec = tween(100)
                        ) {}/*{
                            action.invoke()
                        }*/
                        //action.invoke()
                    }
                    action.invoke()
                }

            },
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        val color = if (selected) TextOrange
        else TextFieldFont
        Icon(painterResource(id = value.icon),
            tint = color,
            contentDescription = null)
        Text(value.text, color = color, fontSize = 13.sp, fontFamily = labelFont)
    }
}


@Composable
fun BottomNavigationBar(navController: NavController) {
    val bottomNavigationItems = listOf(
        BottomNavigationItem.Main,
        BottomNavigationItem.Catalog,
        BottomNavigationItem.Cart,
        BottomNavigationItem.Profile
    )
    BottomNavigation(
        /*  Modifier.onGloballyPositioned {
              log("${it.size.height}")
          },*/
        backgroundColor = MaterialTheme.colors.primary
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        bottomNavigationItems.forEach { item ->
            val selected = currentRoute == item.route
            IconWithText(value = item.data, selected, action = {
                navController.navigate(item.route) {
                    launchSingleTop = true
                    restoreState = true
                    navController.graph.startDestinationRoute?.let { route ->
                        popUpTo(route) {
                            saveState = true
                        }
                    }
                }
            })
        }
        /*  Row(modifier = Modifier.fillMaxSize(), verticalAlignment = Alignment.CenterVertically,
              horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)) {
              bottomNavigationItems.forEach { item ->
                  Text(text = item.data.text)
              }
          }*/
        /*bottomNavigationItems.forEach { item ->
            val selected = currentRoute == item.route
           /* val color = if (selected)
                TextOrange
            else
                TextFieldFont*/
            BottomNavigationItem(
                icon = {
                    Icon(painterResource(id = item.data.icon),
                        //    tint = color,
                            contentDescription = null)
                },
                label = {
                        Text(item.data.text,
                        //color = color
                    ) },
                onClick = {},
                selected = selected,
                selectedContentColor = TextOrange,
                unselectedContentColor = TextFieldFont,
                alwaysShowLabel = true
            )
        }*/
    }
}

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController, startDestination = BottomNavigationItem.Main.route) {
        composable(BottomNavigationItem.Main.route) {

        }
        composable(BottomNavigationItem.Catalog.route) {

        }
        composable(BottomNavigationItem.Cart.route) {

        }
        composable(BottomNavigationItem.Profile.route) {

        }
    }
}
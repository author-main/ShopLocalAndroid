package com.training.shoplocal.screens.bottomnavigation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.training.shoplocal.ui.theme.TextFieldFont
import com.training.shoplocal.ui.theme.TextOrange


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
    val labelFont = FontFamily(Font(R.font.robotocondensed_light))
    Column(Modifier.fillMaxHeight()
        .padding(horizontal = 24.dp)
        .clickable(
            interactionSource = interactionSource,
            indication = null) {
                action.invoke()
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
            IconWithText(value = item.data, selected, action = {})
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

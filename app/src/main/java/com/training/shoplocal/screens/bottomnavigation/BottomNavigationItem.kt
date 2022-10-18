package com.training.shoplocal.screens.bottomnavigation

import androidx.annotation.DrawableRes
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.training.shoplocal.AppShopLocal
import com.training.shoplocal.R
import com.training.shoplocal.TEXT_BOTTOMNAVIGATION
import com.training.shoplocal.log


enum class BottomNavigationItemData(@DrawableRes var icon: Int, val iconOn: Int? = null, val text: String) {
    MAIN        (R.drawable.ic_error,     R.drawable.ic_error,    TEXT_BOTTOMNAVIGATION[0]),
    CATALOG     (R.drawable.ic_error,     R.drawable.ic_error,    TEXT_BOTTOMNAVIGATION[1]),
    CART        (R.drawable.ic_error,     R.drawable.ic_error,    TEXT_BOTTOMNAVIGATION[2]),
    PROFILE     (R.drawable.ic_error,     R.drawable.ic_error,    TEXT_BOTTOMNAVIGATION[3])
}

sealed class BottomNavigationItem(val route: String, val data: BottomNavigationItemData){
    object Main:        BottomNavigationItem("route_main", BottomNavigationItemData.MAIN)
    object Catalog:     BottomNavigationItem("route_catalog", BottomNavigationItemData.CATALOG)
    object Cart:        BottomNavigationItem("route_cart", BottomNavigationItemData.CART)
    object Profile:     BottomNavigationItem("route_profile", BottomNavigationItemData.PROFILE)
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
            BottomNavigationItem(
                icon = {
                    Icon(painterResource(id = item.data.icon), contentDescription = null)
                },
                label = { Text(item.data.text) },
                onClick = {},
                selected = false
            )
        }
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

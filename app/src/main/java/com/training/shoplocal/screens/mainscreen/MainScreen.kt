package com.training.shoplocal.screens.mainscreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.training.shoplocal.log
import com.training.shoplocal.ui.theme.PrimaryDark

@Composable
fun MainScreen(){
    Box(modifier = Modifier.fillMaxSize()
        .background(PrimaryDark))
}
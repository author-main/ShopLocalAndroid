package com.training.shoplocal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import com.training.shoplocal.AppShopLocal.Companion.appRepository
import com.training.shoplocal.ui.theme.ShopLocalTheme
import com.training.shoplocal.viewmodel.FactoryViewModel
import com.training.shoplocal.viewmodel.RepositoryViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: RepositoryViewModel by viewModels(factoryProducer = {
        FactoryViewModel(
     this,
            appRepository()
        )
    })
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getRepository().getUserFingerPrint(this)
//        val repository = AppShopLocal.appRepository();

        //val passwordState = PasswordViewState.getPasswordState()
        setContent {
            ShopLocalTheme(true) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    when (ScreenRouter.current) {
                        ScreenItem.LoginScreen ->
                            LoginScreen(viewModel.getRepository().passwordState)
                        ScreenItem.MainScreen -> {}
                    }
                }
            }
        }
    }
}

/*@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ShopLocalTheme {
        Greeting("Android")
    }
}*/
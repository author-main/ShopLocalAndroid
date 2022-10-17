package com.training.shoplocal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.fragment.app.FragmentActivity
import com.training.shoplocal.AppShopLocal.Companion.appContext
import com.training.shoplocal.AppShopLocal.Companion.appRepository
import com.training.shoplocal.dialogs.DialogRegistration
import com.training.shoplocal.dialogs.DialogRestore
import com.training.shoplocal.dialogs.ShowProgress
import com.training.shoplocal.retrofit.ApiManager
import com.training.shoplocal.retrofit.User.Companion.getUserData
import com.training.shoplocal.screens.LoginScreen
import com.training.shoplocal.screens.MainScreen
import com.training.shoplocal.ui.theme.ShopLocalTheme
import com.training.shoplocal.viewmodel.FactoryViewModel
import com.training.shoplocal.viewmodel.RepositoryViewModel

class MainActivity : FragmentActivity() {//ComponentActivity() {
    private val viewModel: RepositoryViewModel by viewModels(factoryProducer = {
        FactoryViewModel(
     this,
            appRepository()
        )
    })
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val context = LocalContext.current as FragmentActivity
//        log("start app")
        viewModel.getUserFingerPrint(this)
//        val repository = AppShopLocal.appRepository();
        val user = getUserData()

        //val passwordState = PasswordViewState.getPasswordState()
        setContent {
            ShopLocalTheme(true) {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ShowScreen()
                    ShowDialog()
                }
            }
        }
    }


    @Composable
    fun ShowScreen(){
        when (ScreenRouter.current) {
            ScreenItem.LoginScreen ->
                LoginScreen(viewModel.getLoginState())
            ScreenItem.MainScreen -> {
                MainScreen()
            }
            else -> {}
        }
    }

    @Composable
    fun ShowDialog(){
        when (DialogRouter.current) {
            DialogItem.None -> {}
            DialogItem.RegUserDialog -> {
                DialogRegistration()
            }
            DialogItem.RestoreUserDialog -> {
                DialogRestore()
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
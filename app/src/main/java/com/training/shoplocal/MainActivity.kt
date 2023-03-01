package com.training.shoplocal

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import com.training.shoplocal.AppShopLocal.Companion.appRepository
import com.training.shoplocal.dialogs.DialogRegistration
import com.training.shoplocal.dialogs.DialogRestore
import com.training.shoplocal.classes.User.Companion.getUserData
import com.training.shoplocal.screens.appscreen.AppScreen
import com.training.shoplocal.screens.LoginScreen
import com.training.shoplocal.screens.ScreenItem
import com.training.shoplocal.screens.ScreenRouter
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
        viewModel.passContextFingerPrint(this)
        viewModel.setOnCloseApp {
            this.finish()
        }
//        val repository = AppShopLocal.appRepository();
        //val user = getUserData()

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
        /*ScreenRouter.navigateTo(ScreenItem.MainScreen)
        AppScreen()*/
        val authorizedUser: Boolean by viewModel.authorizedUser.collectAsState()
        if (authorizedUser) {
            AppScreen()
        }
        else
            LoginScreen(viewModel.getLoginState())
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
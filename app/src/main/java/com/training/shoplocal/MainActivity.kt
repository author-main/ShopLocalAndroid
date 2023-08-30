package com.training.shoplocal

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import com.training.shoplocal.AppShopLocal.Companion.appComponent
import com.training.shoplocal.AppShopLocal.Companion.appRepository
import com.training.shoplocal.dialogs.DialogRegistration
import com.training.shoplocal.dialogs.DialogRestore
import com.training.shoplocal.classes.User.Companion.getUserData
import com.training.shoplocal.classes.downloader.DiskCache
import com.training.shoplocal.classes.downloader.ImageLinkDownloader
import com.training.shoplocal.classes.downloader.MemoryCache
import com.training.shoplocal.dagger.ActivityMainScope
import com.training.shoplocal.dagger.MainActivitySubcomponent
import com.training.shoplocal.loginview.LoginViewState
import com.training.shoplocal.repository.AccessUser
import com.training.shoplocal.repository.AccessUserInterface
import com.training.shoplocal.repository.Repository
//import com.training.shoplocal.dagger.MainActivitySubcomponent
import com.training.shoplocal.screens.appscreen.AppScreen
import com.training.shoplocal.screens.LoginScreen
import com.training.shoplocal.screens.ScreenItem
import com.training.shoplocal.screens.ScreenRouter
import com.training.shoplocal.ui.theme.ShopLocalTheme
import com.training.shoplocal.userfingerprint.UserFingerPrint
import com.training.shoplocal.userfingerprint.UserFingerPrintInterface
import com.training.shoplocal.viewmodel.FactoryViewModel
import com.training.shoplocal.viewmodel.RepositoryViewModel
import dagger.android.AndroidInjection
import javax.inject.Inject

@SuppressLint("CompositionLocalNaming")
val ImageDownloader = staticCompositionLocalOf<ImageLinkDownloader> {
    error("initialization...")
}

class MainActivity : FragmentActivity() {//ComponentActivity() {
    @Inject
    lateinit var imageDownloader: ImageLinkDownloader

    @Inject
    lateinit var accessUser: AccessUserInterface

    @Inject
    lateinit var userFingerPrint: UserFingerPrint

    @Inject
    lateinit var viewModel: RepositoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        appComponent.mainActivitySubcomponent().create(this).inject(this)
        appRepository().accessUser = accessUser
        accessUser.setFingerPrint(userFingerPrint)
        viewModel.setOnCloseApp {
            this.finish()
        }
        setContent {
            ShopLocalTheme(true) {
                CompositionLocalProvider(ImageDownloader provides imageDownloader) {
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
    }

    @Composable
    fun ShowScreen(){
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
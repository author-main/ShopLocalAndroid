package com.training.shoplocal.repository



import android.content.Context
import com.training.shoplocal.AppShopLocal.Companion.appContext
import com.training.shoplocal.classes.Brand
import com.training.shoplocal.classes.Category
import com.training.shoplocal.classes.DataDisplay
import com.training.shoplocal.classes.Product
import com.training.shoplocal.classes.downloader.ImageLinkDownloader
import com.training.shoplocal.classes.searcher.SearchQueryStorage
import com.training.shoplocal.getCacheDirectory
import com.training.shoplocal.log
import com.training.shoplocal.loginview.LoginViewState
import java.io.File


class Repository: DAOinterface {
    private val dataDisplay = DataDisplay()
    val loginState = LoginViewState.getLoginState()

    /**
     *  Реализация методов для получения доступа (регистрация, вход по паролю,
     *  восстановление, вход по отпечатку)
     *  accessUser - класс реализующий все операции, поведение определено
     *  интерфейсом AccessUserInterface
     */
    override var accessUser: AccessUserInterface = AccessUser().apply {
        this.updateViewWhen(loginState)
    }

    override var databaseCRUD: DatabaseCRUDInterface = DatabaseCRUD()

    /** Context типа FragmentActivity главной активити приходится тянуть для отображения
     *  BiometricPrompt - диалог сканирования отпечатка
     */
    fun setContextFingerPrint(context: Context){
        accessUser.getContextFingerPrint(context)
    }
    fun onRestoreUser(action: ((result: Boolean) -> Unit)?, email: String, password: String) {
        accessUser.onRestoreUser(action, email, password)
    }
    fun onRegisterUser(action: ((result: Boolean) -> Unit)?, vararg userdata: String) {
        accessUser.onRegisterUser(action, *userdata)
    }
    fun onLogin(action: (result: Int) -> Unit, email: String, password: String, finger: Boolean = false) {
        accessUser.onLogin(action, email, password, finger)
    }
    fun onFingerPrint(action: ((result: Int) -> Unit)?, email: String) {
        accessUser.onFingerPrint(action, email)
    }
    fun removePassword(){
        accessUser.onRemoveUserPassword()
    }

    fun getDataDisplay() = dataDisplay

    /**
     *  Реализация методов для получения данных из базы данных MySQL
     */
    fun getProduct(id: Int, action: (product: Product) -> Unit){
        databaseCRUD.getProduct(id, action)
    }

    fun getPromoProducts(id: Int, part: Int, action: (products: List<Product>) -> Unit){
        databaseCRUD.getPromoProducts(id, part, action)
    }

    fun getBrands(action: (brands: List<Brand>) -> Unit){
        databaseCRUD.getBrands(action)
    }

    fun getCategories() {
        databaseCRUD.getCategories()
    }

    suspend fun updateFavorite(id_user: Int, id_product: Int, value: Byte){
        databaseCRUD.updateFavorite(id_user, id_product, value)
    }

    suspend fun getProducts(id: Int, part: Int): List<Product> =
        databaseCRUD.getProducts(id, part)

     fun getSearchHistoryList(): List<String> {
        return SearchQueryStorage.getInstance().getQueries()
     }

    fun disposeSearchHistoryList(){
        SearchQueryStorage.getInstance().dispose()
    }


}
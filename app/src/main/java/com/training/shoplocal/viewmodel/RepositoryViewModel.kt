package com.training.shoplocal.viewmodel

import android.content.Context
import android.os.*
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.training.shoplocal.*
import com.training.shoplocal.classes.*
import com.training.shoplocal.classes.fodisplay.FieldFilter
import com.training.shoplocal.classes.fodisplay.ItemFilter
import com.training.shoplocal.classes.fodisplay.OrderDisplay
import com.training.shoplocal.classes.screenhelpers.DataScreen
import com.training.shoplocal.repository.Repository
import com.training.shoplocal.screens.ScreenRouter
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*
import kotlin.collections.HashMap

class RepositoryViewModel(private val repository: Repository) : ViewModel() {
    //var SIZE_PORTION = 10
    /*private val _accessFinger = MutableStateFlow<Boolean>(false)
    val accessFinger = _accessFinger.asStateFlow()
    private fun accessFingerPrint(value: Boolean) {
        _accessFinger.value = value
    }*/

    private val _refreshUserMessages = MutableStateFlow<Boolean>(false)
    val refreshUserMessages = _refreshUserMessages.asStateFlow()
    fun updateUserMessages(){
        _refreshUserMessages.value = true
        getMessages()
    }

    private val _countUnreadMessages = MutableStateFlow<Int>(0)
    val countUnreadMessages = _countUnreadMessages.asStateFlow()
    fun setCountUnreadMessages(value: Int){
        _countUnreadMessages.value = value
    }

   /* private val _userMessages = MutableStateFlow<MutableList<UserMessage>>(mutableListOf())
    val userMessages = _userMessages.asStateFlow()
    private fun setUserMessages(value: List<UserMessage>){
        _userMessages.value = mutableListOf<UserMessage>(*value.toTypedArray()) // * spread operator, входной параметр элементы массива/списка
    }*/


    private var _userMessages = mutableStateListOf<UserMessage>()
    val userMessages: List<UserMessage> = _userMessages
    private fun setUserMessages(value: List<UserMessage>){
        _userMessages.apply {
            clear()
            addAll(value)
        }
    }



    private val _reviews = MutableStateFlow(listOf<Review>())
    val reviews = _reviews.asStateFlow()
    private fun setReviews(value: List<Review>) {
        _reviews.value = value
    }
    fun clearReviews(){
        _reviews.value = listOf()
    }

    private var onCloseApp: (() -> Unit)? = null
    fun setOnCloseApp(value:() -> Unit ) {
        onCloseApp = value
    }
    fun closeApp(){
        USER_ID = -1
        repository.loginState.reset()
        _products.value.clear()
        repository.clearMapScreenProducts()
        brands.values.clear()
        categories.values.clear()
        OrderDisplay.resetFilter()
        containerStack.removeAllElements()
        onCloseApp?.invoke()
    }

    private var searchQuery: String = EMPTY_STRING
    private val _progressCRUD = MutableStateFlow<Boolean>(false)
    val progressCRUD = _progressCRUD.asStateFlow()
    private fun showProgressCRUD() {
        _progressCRUD.value = true
    }
    private fun hideProgressCRUD() {
        _progressCRUD.value = false
    }

    private val _activeContainer = MutableStateFlow(Container.LOGIN)
    val activeContainer = _activeContainer.asStateFlow()
    private val containerStack = Stack<Container>().apply {
        push(Container.LOGIN)
    }
    /*var deviceUuid =
        UUID(androidId.hashCode(), tmDevice.hashCode() as Long shl 32 or tmSerial.hashCode())*/
    private val UUID_QUERY = System.nanoTime().toString()
    private var maxPortion: Int = -1
    //private val UUID_QUERY: String = UUID.randomUUID().toString()
    //private var lockDB = false
    //private val reflexRepository = Repository::class.java.methods
    //log(reflexRepository.toString())
    private val _hiddenBottomNavigation = MutableStateFlow<Boolean>(false)
    val hiddenBottomNavigation = _hiddenBottomNavigation.asStateFlow()
  /*  fun setPortionDataDB(value: Int) {
        SIZE_PORTION = value
    }*/
    fun hideBottomNavigation() {
        _hiddenBottomNavigation.value = true
    }

    fun showBottomNavigation() {
        _hiddenBottomNavigation.value = false
    }

    private var loadedPortion = 0
    private val _selectedProduct = MutableStateFlow<Product>(Product())
    val selectedProduct = _selectedProduct.asStateFlow()

    @JvmName("setSelectedProduct1")
    fun setSelectedProduct(product: Product){
        /*if (product.favorite < 1) {
            if (OrderDisplay.getInstance().getFavorite() == 1) {
                _products.value.remove(product)
            }
        }*/
        _selectedProduct.value = product.copy(favorite = product.favorite)
    }

    private var USER_ID: Int = -1
    private var brands = hashMapOf<Int, Brand>()
        //listOf<Brand>()
    private var categories = hashMapOf<Int, Category>()
        //listOf<Category>()

    /*private val _products = MutableStateFlow<MutableList<Product>>(mutableListOf<Product>())
    val products = _products.asStateFlow()*/

    private val _products = MutableStateFlow<MutableList<Product>>(mutableListOf<Product>())
    val products = _products.asStateFlow()

    private val actionLogin: (result: Int) -> Unit = {
        val result = it > 0
        if (result) {
            //accessFingerPrint(true)
            hideSnackbar()
            maxPortion = -1
            USER_ID = it
            /*ScreenRouter.navigateTo(ScreenItem.MainScreen)
            setOrderDisplay(FieldFilter.SCREEN, ScreenItem.MainScreen.key.value)*/
            containerStack.pop()
            putComposeViewStack(Container.MAIN)
            getBrands()
            getCategories()
            getMessages(getCountUnread = true)
//            exchangeDataMap[ExchangeData.GET_PRODUCTS] = false
            loadedPortion = 0
            getProducts(1)
            authorizeUser()
        }
        else {
            showSnackbar(message = getStringResource(R.string.message_login_error), type = MESSAGE.ERROR)
          /*  val vibe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                val vibratorManager =
                    AppShopLocal.appContext().getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
                    vibratorManager.defaultVibrator
            } else {
                @Suppress("DEPRECATION")
                AppShopLocal.appContext().getSystemService(VIBRATOR_SERVICE) as Vibrator
            }
            val effect: VibrationEffect = VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE);
            vibe.vibrate(effect)*/
            vibrate(400)
        }
    }
    private val _authorizedUser = MutableStateFlow<Boolean>(false)
    val authorizedUser = _authorizedUser.asStateFlow()
    private val _snackbarData = MutableStateFlow(Triple<String, Boolean, MESSAGE>("", false, MESSAGE.INFO))
    val snackbarData = _snackbarData.asStateFlow()
    fun showSnackbar(message: String = "", type: MESSAGE = MESSAGE.INFO, visible: Boolean = true){
        _snackbarData.value = Triple(message, visible, type)
    }
    private fun hideSnackbar(){
        _snackbarData.value = Triple("", false, MESSAGE.INFO)
    }

    private fun authorizeUser(value: Boolean = true){
        _authorizedUser.value = value
    }
    fun getLoginState() = repository.loginState

    fun getPassword()   = repository.loginState.getPassword()

    fun removePassword()   = repository.removePassword()

    fun setEmail(value: String)      = repository.loginState.setEmail(value)

    fun onRestoreUser(action: ((result: Boolean) -> Unit)?, email: String, password: String) {
        repository.onRestoreUser(action, email, password)
    }

    fun onRegisterUser(action: ((result: Boolean) -> Unit)?, vararg userdata: String) {
        repository.onRegisterUser(action, *userdata)
    }

    fun onLogin(email: String, password: String, finger: Boolean = false) {
        repository.onLogin({ result ->
            actionLogin(result)
        }, email, password, finger)

    }

    fun onFingerPrint(email: String) {
        repository.onFingerPrint(actionLogin, email)
    }

    fun getOrderDisplay(field: FieldFilter) =
        repository.getOrderDisplay(field)


    private fun<T> setOrderDisplay(field: FieldFilter, value: T){
        repository.setOrderDisplay(field, value)
    }

    fun passContextFingerPrint(context: Context) {
        repository.setContextFingerPrint(context)
    }

  /*  private fun getProduct(id: Int){
        repository.getProduct(id) { product ->
            //log(product.toString())
            //_products.value = products.toMutableList()
        }
    }*/

    /**
     * Получить max число порций, количество записей возвращается в первой строке результа
     * выборки продуктов в формате &lt;count&gt; name
     * @param value первый элемент списка продуктов
     * @return Pair<max, name>, где max - число порций, name - наименование продукта
     */
    private fun getMaxPortion(value: String): Pair<Int, String>{
        var calcMaxPortion = 0
        var name = ""
        if (value.startsWith('<')) {
            val index = value.indexOf('>')
            try {
                if (index != -1) {
                    val count = value.substring(1, index).toInt()
                    name = value.substring(index + 1)
                    calcMaxPortion = getPortion(count)
                }
            } catch (_: Exception) {
            }
        }
        return calcMaxPortion to name
    }

    //@Synchronized
    private fun getProducts(part: Int){
      // val exchangeData = exchangeDataMap[ExchangeData.GET_PRODUCTS] ?: false
      /*  if (lockDB) return
            lockDB = true*/
          //  exchangeDataMap[ExchangeData.GET_PRODUCTS] = true


            /*val coroutine = CoroutineScope(Job() + Dispatchers.IO)
        coroutine.launch {*/
            showProgressCRUD()
            repository.getProducts(USER_ID, part) { listProducts ->
                //log("loaded products...")
                if (listProducts.isNotEmpty()) {
                    if (part == 1) {
                        val extractedData = getMaxPortion(listProducts[0].name)
                        maxPortion = extractedData.first
                        listProducts[0].name = extractedData.second
                        _products.value.clear()
                    }
                    loadedPortion = part

                    val list = _products.value.toMutableList().apply { addAll(listProducts) }
                    _products.value = list
                } else {
                    if (part == 1)
                        _products.value = mutableListOf()
                }
                //lockDB = false
                hideProgressCRUD()
            //}

        }
    }

    private fun getBrands(){
        repository.getBrands { list ->
            brands = list.associateBy { it.id } as HashMap<Int, Brand>
        }
    }

    private fun getCategories(){
        repository.getCategories { list ->
            categories = list.associateBy { it.id } as HashMap<Int, Category>
        }
    }

    fun getBrand(id: Int) =
        brands[id]?.name ?: EMPTY_STRING
        //brands.find {it.id == id}?.name ?: ""

    fun getCategory(id: Int) =
        categories[id]?.name ?: EMPTY_STRING
        //categories.find {it.id == id}?.name ?: ""


    fun getSectionFilter(): HashMap<Int, MutableList<ItemFilter>>{
        val map = kotlin.collections.HashMap<Int, MutableList<ItemFilter>>()

       /* val categoryKey =
            ItemFilter(
                id = CATEGORY_ITEM,
                name = getStringResource(R.string.text_category)
            )*/
        val list = mutableListOf<ItemFilter>()
        categories.forEach {
            list.add(
                ItemFilter(
                    id = it.key,
                    name = it.value.name
                )
            )
        }
        map[CATEGORY_ITEM] = list.toMutableList()


       /* val brendKey =
            ItemFilter(
                id = BREND_ITEM,
                name = getStringResource(R.string.text_brend)
            )*/
        list.clear()
        brands.forEach {
            list.add(
                ItemFilter(
                    id = it.key,
                    name = it.value.name
                )
            )
        }
        map[BREND_ITEM] = list
        return map
    }


    fun setProductFavorite(product: Product){
        //val favorite: Byte = if (product.favorite > 0) 1 else 0
        //log("favorite ${product.favorite}")

        viewModelScope.launch {
            /*
            Если нужно обработать результат
            val result: Int =
            try{
               val response = repository.updateFavorite(USER_ID, product.id, product.favorite)
               response.body()?.toInt() ?: 0
               // log("response = ${response.body()}")
            }
            catch (_: Exception) {
               0
            }
            if (result > 0) {

            }*/
            repository.updateFavorite(USER_ID, product.id, product.favorite)
        }
        var currentProduct: Product? = null
        products.value.find { it.id== product.id }?.let {
            it.favorite = product.favorite
            currentProduct = it
        }
        if (product.favorite < 1) {
            if (OrderDisplay.getInstance().getFavorite() == 1) {
                currentProduct?.let{
                    //log("delete favorite item")
                    val list = _products.value.toMutableList()
                    list.remove(it)
                    _products.value = list
                }

            }
        }
        setSelectedProduct(product)
    }


    /*fun setProductFavorite(id: Int, value: Boolean){
        val favorite: Byte = if (value) 1 else 0
        products.value.find { it.id== id }?.let{
            it.favorite = favorite
            setSelectedProduct(it)
            viewModelScope.launch {
                repository.updateFavorite(USER_ID, it.id, favorite)
            }
        }
    }*/

    fun nextPortionAvailable() =
        loadedPortion + 1<= maxPortion

    fun getNextPortionData(searchMode: Boolean, textSearch: String){
      /* val nextPortion = loadedPortion + 1//getPortion()+1
        if (nextPortion <= maxPortion) */
        if (nextPortionAvailable()) {
            //log("load partion")
            val nextPortion = loadedPortion + 1
            if (searchMode) {
                //log("next portion = $nextPortion, max portion = $maxPortion")
                findProductsRequest(textSearch, nextPortion)
            } else {
                getProducts(nextPortion)
            }
        }
    }

    fun saveScreenProducts(key: ScreenRouter.KEYSCREEN, firstIndex: Int) {//, firstOffset: Int) {
        repository.saveScreenProducts(key,
//            DataScreen(firstIndex, firstOffset, maxPortion, products.value)
            DataScreen(firstIndex, maxPortion, products.value, OrderDisplay.clone())
        )
    }

    fun restoreScreenProducts(key: ScreenRouter.KEYSCREEN): Int{//} Pair<Int, Int> {
    //    log("restore")
        val data = repository.restoreScreenProducts(key)
        OrderDisplay.restoreDataDisplay(data.datadisplay)
        maxPortion = data.maxPortion
        _products.value = data.products//repository.restoreScreenProducts(key)//.toMutableList()
        loadedPortion = getPortion() // количество загруженных порций
        //log("firstIndex ${data.firstItemIndex}, firstOffset ${data.firstItemOffset}")
        return data.firstItemIndex// to data.firstItemOffset
    }

    private fun getPortion(size: Int = 0): Int{
        val count = if (size == 0)
                        products.value.size
                    else size
        /*val value = products.value.size % SIZE_PORTION
        var portion = products.value.size/ SIZE_PORTION*/
        val value   = count % SIZE_PORTION
        var portion = count / SIZE_PORTION
        if (value > 0)
            portion += 1
        return portion
    }

    /**
     *  Блок методов для управления журналом поисковых запросов
     */
    fun getSearchHistoryList(fromFile: Boolean = false): List<String> {
/*        val callable = Callable<List<String>> {
            repository.getSearchHistoryList()
        }
        return Executors.newSingleThreadExecutor().submit(callable).get()*/
        return repository.getSearchHistoryList(fromFile)
    }

    fun disposeSearchHistoryList(){
        repository.disposeSearchHistoryList()
    }

    fun putSearchHistoryQuery(value: String) {
        repository.putSearchHistoryQuery(value)
    }

    fun removeSearchHistoryQuery(index: Int){//value: String) {
        repository.removeSearchHistoryQuery(index)
    }

    fun saveSearchHistory() {
        repository.saveSearchHistory()
    }

    fun disposeSearchHistory() {
        repository.disposeSearchHistory()
    }

    fun clearSearchHistory() {
        repository.clearSearchHistory()
    }
    /**
     *  End Блок методов для управления журналом поисковых запросов
     */

    //@Synchronized
    fun findProductsRequest(query: String, value: Int = 0){
        if (value == -1) { //  Очистка результатов поиcка в BD
            repository.findProductsRequest(query, 0, UUID_QUERY, USER_ID){}
            return
        }


/*        val list = _products.value.toMutableList()
        list.addAll(_products.value)
        _products.value = mutableListOf()
        _products.value = list
        return*/

       // if (lockDB) return
        searchQuery = query
        showProgressCRUD()
        var portion = value
        if (value == 0) {
            loadedPortion = 0
            //UUID_QUERY = UUID.randomUUID()
            portion = 1
            //lockDB = false
                //_products.value.clear()// = mutableListOf()
            //log("clear products")
        }
      //  lockDB = true
        //log ("find portion = $portion")
        //repository.findProductsRequest(query, portion, UUID_QUERY.toString(), USER_ID) { listFound ->
            repository.findProductsRequest(query, portion, UUID_QUERY, USER_ID) { listFound ->
             //   log ("size ${listFound.size}")
                //setSelectedProduct(Product())
                if (listFound.isNotEmpty()) {
                    //_products.value = listFound.toMutableList()
                    //log("get portion $portion")
                    if (portion == 1) {
                        val extractedData = getMaxPortion(listFound[0].name)
                        maxPortion = extractedData.first
                        listFound[0].name = extractedData.second
                        _products.value.clear()
                    }
                    val list = _products.value.toMutableList().apply { addAll(listFound) }
                    _products.value = list
                    loadedPortion = portion
                } else {
                    if (portion == 1)
                        _products.value = mutableListOf()
                }
              // lockDB = false
                hideProgressCRUD()
            }

        /*INSERT INTO new_table_name
        SELECT labels.label,shortabstracts.ShortAbstract,images.LinkToImage,types.Type
        FROM ner.images,ner.labels,ner.shortabstracts,ner.types
        WHERE labels.Resource=images.Resource AND labels.Resource=shortabstracts.Resource
                AND labels.Resource=types.Resource;*/
    }

    fun clearResultSearch(){
        findProductsRequest("*", -1)
    }

    fun putComposeViewStack(value: Container) {
        val equalValue = containerStack.isNotEmpty() && containerStack.peek() == value
        if (!equalValue) {
            containerStack.push(value)
            _activeContainer.value = value
        }
     //   log(containerStack)
    }

    fun getPrevContainer(): Container? {
        val prev = try {
            containerStack[containerStack.lastIndex - 1]
        } catch (_: Exception){null}
        return prev
    }

    fun prevComposeViewStack(): Container {
        containerStack.pop()
        val value = containerStack.peek()
        _activeContainer.value = value
     //   log(containerStack)
        return value//containerStacks.peek()
    }

/*    fun removeComposeViewStack(): ComposeView {
        val value = composeViewStack.pop()
        log(composeViewStack)
        return value//composeViewStack.pop()
    }*/

  /*  fun getContainerStack(): Container {
        return _activeContainer.value//containerStack.peek()
    }*/

 /*   fun existImageCache(filename: String?, convert: Boolean = false):Boolean {
        if (filename == null)
            return false
        val hash = if (convert)
            md5(filename) else filename
        //log("hash = $hash")
        var exist = false
        if (MemoryCache.exist(hash))
            exist = true
        else {
            val pathFile = CACHE_DIR + filename //getCacheDirectory() + filename
          //  log("path = $pathFile")
            if (fileExists(pathFile))
                exist = true
        }
        return exist
    }*/

    fun filterProducts(searchMode: Boolean = false){
        maxPortion = -1
        loadedPortion = 0
        if (searchMode) {
            findProductsRequest(searchQuery)
        } else {
            getProducts(1)
        }
    }

    fun getReviewProduct(id: Int)
    {
        showProgressCRUD()
        repository.getReviewProduct(
            id
        ) {reviews ->
            setReviews(reviews)
            hideProgressCRUD()
        }
    }

    /**
     * Получить сообщения пользователя или количество непрочитанных сообщений
     * @param id идентификатор пользователя, если отрицательное значение - возвращает количество
     * непрочитанных сообщений (в первой записи списка, в поле id)
     */
    fun getMessages(getCountUnread: Boolean = false) {
        val passId = if (getCountUnread) -USER_ID else USER_ID
        repository.getMessages(passId) {messages ->
            if (messages.isNotEmpty()) {
                if (passId < 0)
                    setCountUnreadMessages(messages[0].id)
                else {
                    setUserMessages(messages)
                    setCountUnreadMessages(messages.count { it.read == 0 })
                }
            }
         //   log("messages updates...")
            _refreshUserMessages.value = false
        }
    }
    fun clearMessages(){
        _userMessages.clear()
    }


    fun markDeletedUserMessages(id: Int, deleted: Boolean = true){
        val message = _userMessages.find { msg -> msg.id == id }
        message?.let {userMessage ->
            //_userMessages[index].deleted = deleted
            userMessage.deleted = deleted
            if (deleted) {
                if (userMessage.read == 0)
                    _countUnreadMessages.value -= 1

            } else {
                if (userMessage.read == 0)
                    _countUnreadMessages.value += 1
            }
        }
    }

    fun updateUserMessage(ids: IntArray, what: Int) {
        //0 - отметить как прочитанное
        //1 - удалить
        viewModelScope.launch {
            val result: Int =
                try{
                    val id_message = ids.joinToString(",")
                    val response = repository.updateUserMessage(USER_ID, what, id_message)
                    response.body()?.toInt() ?: 0
                }
                catch (_: Exception) {
                    0
                }
            if (result > 0) {
                var recomposition = false
                val listMessages = _userMessages.toMutableList()
                if (what == USERMESSAGE_READ) {
                    var countUnread = _countUnreadMessages.value
                    ids.forEach { id ->
                        listMessages.find {
                            it.id == id
                        }?.let { message ->
                            message.read = 1
                            recomposition = true
                            countUnread -= 1
                            //_countUnreadMessages.value -= 1
                        }
                    }
                    _countUnreadMessages.value = countUnread


                }
                if (what == USERMESSAGE_DELETE) {
                  /*  var countUnread = _countUnreadMessages.value
                    ids.forEach {id ->
                        val message = listMessages.find {
                            it.id == id
                        }
                        message?.let {
                            if (it.read == 0)
                                countUnread -= 1
                            listMessages.remove(it)
                            //recomposition = true
                        }
                    }
                    _countUnreadMessages.value = countUnread*/
                    recomposition = true

                }

                if (recomposition)
                    setUserMessages(listMessages)

            }
        }

    }

 }
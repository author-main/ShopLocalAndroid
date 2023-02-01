package com.training.shoplocal.classes.fodisplay

import android.provider.ContactsContract.Data
import androidx.compose.runtime.*
import com.training.shoplocal.classes.ANY_VALUE
import com.training.shoplocal.classes.EMPTY_STRING
import com.training.shoplocal.log
import com.training.shoplocal.screens.ScreenItem

/**
  Класс для выборки данных (product) из БД (таблица products),
 * определяет тип сортировки и фильтр выборки по параметрам продукта
 */

/*const val ANY_VALUE =    -1
enum class SORT_TYPE(val value: Int)     {ASCENDING(0), DESCENDING(1)}
enum class SORT_PROPERTY(val value: Int) {PRICE(0), POPULAR(1), RATING(2)}*/

class OrderDisplay: ProviderDataDisplay{
    data class SortData(var sortType: SORT_TYPE             = SORT_TYPE.ASCENDING,
                        var sortProperty: SORT_PROPERTY     = SORT_PROPERTY.PRICE)

    private val sortData                = SortData()
    private var filterData              = FilterData()
    private var currentScreen:Int       = ANY_VALUE
   // private var completeUpdate = true
    //var state by mutableStateOf(false)


    override fun setViewMode(value: VIEW_MODE) {
        filterData.viewmode = value
    }

    override fun getViewMode() = filterData.viewmode

    override fun setDiscount(value: Int) {
        filterData.discount = value
    }

    override fun getDiscount(): Int {
        return filterData.discount
    }

    //override var state: MutableState<Boolean> = mutableStateOf(false)

    override fun setSortType(value: SORT_TYPE) {
        sortData.sortType = value
    }

    override fun setSortProperty(value: SORT_PROPERTY) {
        if (value != sortData.sortProperty)
            sortData.sortProperty = value
        else
            invertSortType()
      //  updateState()
    }

    override fun setBrend(value: String) {
        filterData.brend = value
    }

    override fun setCategory(value: String) {
        filterData.category = value
    }

    override fun setFavorite(value: Int) {
        filterData.favorite = value
    }

    override fun setPriceRange(valueFrom: Float, valueTo: Float) {
        filterData.priceRange = valueFrom to valueTo
    }

    override fun setScreen(value: Int) {
        //log("set current screen $value")
        currentScreen = value
    }

    override fun getSortType(): SORT_TYPE {
        return sortData.sortType
    }

    override fun getSortProperty(): SORT_PROPERTY {
        return sortData.sortProperty
    }

    override fun getBrend(): String {
        return filterData.brend
    }

    override fun getCategory(): String {
        return filterData.category
    }

    override fun getFavorite(): Int {
        return filterData.favorite
    }

    override fun getPriceRange(): Pair<Float, Float> {
        return filterData.priceRange
    }

    override fun getScreen(): Int {
        return currentScreen
    }

    /*private fun updateState(){
        if (completeUpdate)
            state.value = !state.value
    }
    fun beginUpdate(){
        completeUpdate = false
    }
    fun endUpdate(){
        completeUpdate = true
        updateState()
    }*/

    private fun invertSortType(){
        if (sortData.sortType == SORT_TYPE.ASCENDING)
            sortData.sortType = SORT_TYPE.DESCENDING
        else
            sortData.sortType = SORT_TYPE.ASCENDING
    }

    override fun resetFilter(): Int {
        val CHANGED_FILTER   =  0
        val CHANGED_VIEWMODE =  1
        val NO_CHANGED       = -1

      //  var result = false
        val filter = FilterData()
        val changedFilter   = if (!equalsFilterData(filter))     CHANGED_FILTER   else NO_CHANGED
        val changedViewMode = if (!equalsFilterViewMode(filter)) CHANGED_VIEWMODE else NO_CHANGED
        filterData = filter
        return if (changedFilter < 0) changedViewMode else CHANGED_FILTER


    //    log("filter Data = $filterData")
        /*if (   filter.brend             != filterData.brend
            || filter.favorite          != filterData.favorite
            || filter.priceRange.first  != filterData.priceRange.first
            || filter.priceRange.second != filterData.priceRange.second
            || filter.category          != filterData.category
            || filter.discount          != filterData.discount
        )*/

       /* if (!equalsFilter(filter)){
            filterData = filter
            result = true
        }*/

      /*  if (filterData != filter) {
            filterData = filter
            result = true
        }*/


        //log("reset $result")
     //   return result
    }

    override fun equalsFilterData(filter: FilterData) =
        filterData.equalsFilterData(filter)

    override fun equalsFilterViewMode(filter: FilterData) =
        filterData.equalsFilterViewMode(filter)


    override fun setFilter(filter: FilterData) {
        filterData = filter
    }

    override fun getFilter() = filterData

    companion object {
        private lateinit var instance: ProviderDataDisplay//OrderDisplay
        //private var backupData: ProviderDataDisplay? = null
        @Synchronized
        fun getInstance(): ProviderDataDisplay{//OrderDisplay {
            if (!this::instance.isInitialized)
                instance = OrderDisplay()
            return instance
        }

        /*fun saveDataDisplay(){
            backupData = instance
        }*/

        fun clone(): ProviderDataDisplay{
            val dataDisplay = OrderDisplay()
            with (dataDisplay) {
                filterData              = instance.getFilter().copy()
                sortData.sortType       = instance.getSortType()
                sortData.sortProperty   = instance.getSortProperty()
                currentScreen           = instance.getScreen()
            }
            return dataDisplay
        }

        fun restoreDataDisplay(backup: ProviderDataDisplay){
                instance = backup
        }


        fun resetFilter(): Int {
            getInstance()
            return instance.resetFilter()
        }

        fun setFilter(filter: FilterData){
            getInstance()
            instance.setFilter(filter)
        }

        fun setViewMode(viewmode: VIEW_MODE){
            getInstance()
            instance.setViewMode(viewmode)
        }

        fun equalsFilterData(filter: FilterData): Boolean {
            getInstance()
            return instance.equalsFilterData(filter)
        }

        fun equalsFilterViewMode(filter: FilterData): Boolean {
            getInstance()
            return instance.equalsFilterViewMode(filter)
        }

        fun getFilter(): FilterData {
            getInstance()
            return instance.getFilter()
        }

        fun getFilterQuery(): String {
            getInstance()
            return instance.getFilterQuery()

            /*val sort_order          = instance.getSortType().value
            val sort_type           = instance.getSortProperty().value
            val filter_category     = instance.getCategory()
            val filter_brend        = instance.getBrend()
            val filter_favorite     = instance.getFavorite()
            val current_screen      = instance.getScreen()
            val filter_price        = run {
                val value: Pair<Float, Float>   = instance.getPriceRange()
                "${value.first}-${value.second}"
            }
            /** Порядок для извлечения в PHP:
             *  sort_order:         0 - ASCENDING, 1 - DESCENDING
             *  sort_type:          0 POPULAR, 1 - RATING, 2 - PRICE
             *  filter_category:    ID категории продукта
             *  filter_brand:       ID бренда
             *  filter_favorite:    0 - все продукты, 1 - избранное
             *  filter_price:       интервал цен, н/р 1000,00-20000,00
             *  current_screen:     текущий экран
             */
            //log("$sort_order $sort_type $filter_category $filter_brend $filter_favorite $filter_price $current_screen")
            return "$sort_order $sort_type $filter_category $filter_brend $filter_favorite $filter_price $current_screen"*/
        }




    }



    /*

    fun setSortProperty(value: SORT_PROPERTY){
        if (value != sortData.sortProperty)
            sortData.sortProperty = value
        else
            invertSortType()
        updateState()
    }

    fun setScreenData(value: ScreenItem){
        screenData = value
    }

    fun getScreenData()     = screenData
    fun getSortType()       = sortData.sortType
    fun getSortProperty()   = sortData.sortProperty

    fun setBrend(value: Int){
        filterData.brend = value
    }
    fun setFavorite(value: Int){
        filterData.favorite = value
        updateState()
    }
    fun setPriceRange(value: Pair<Float, Float>){
        filterData.priceRange = value
    }
    fun setCategory(value: Int){
        filterData.category = value
    }
    fun getBrend()          = filterData.brend
    fun getFavorite()       = filterData.favorite
    fun getPriceRange()     = filterData.priceRange
    fun getCategory()       = filterData.category
    fun isFiltered() : Boolean {
        return filterData.brend != ANY_VALUE || filterData.favorite == 1 || filterData.category != ANY_VALUE || filterData.priceRange.second != 0.00f
    }
    fun resetFilter() {
        filterData.brend           = ANY_VALUE
        filterData.favorite        = 0
        filterData.priceRange      = 0.00f to 0.00f
        filterData.category        = ANY_VALUE
    }
    companion object {
        private lateinit var instance: OrderDisplay
        fun getInstance(): OrderDisplay {
            if (!this::instance.isInitialized)
                instance = OrderDisplay()
            return instance
        }

        fun getOrderDislayQuery(): String {
            getInstance()
            val sort_order          = instance.getSortType().value
            val sort_type           = instance.getSortProperty().value
            val filter_category     = instance.getCategory()
            val filter_brend        = instance.getBrend()
            val filter_favorite     = instance.getFavorite()
            val filter_price        = run {
                val value: Pair<Float, Float>   = instance.getPriceRange()
                "${value.first}-${value.second}"
            }
            val current_screen = when (instance.getScreenData()) {
                ScreenItem.MainScreen    -> 0
                ScreenItem.CatalogScreen -> 1
                ScreenItem.CartScreen    -> 2
                ScreenItem.ProfileScreen -> 3
                else                     -> -1
            }
            /** Порядок для извлечения в PHP:
             *  sort_order:         0 - ASCENDING, 1 - DESCENDING
             *  sort_type:          0 POPULAR, 1 - RATING, 2 - PRICE
             *  filter_category:    ID категории продукта
             *  filter_brand:       ID бренда
             *  filter_favorite:    0 - все продукты, 1 - избранное
             *  filter_price:       интервал цен, н/р 1000,00-20000,00
             *  current_screen:     текущий экран
             */
            //log("$sort_order $sort_type $filter_category $filter_brend $filter_favorite $filter_price $current_screen")
            return "$sort_order $sort_type $filter_category $filter_brend $filter_favorite $filter_price $current_screen"
        }




    }


*/


}

/*data class SortData(private var sortType: SORT_TYPE             = SORT_TYPE.ASCENDING,
                    private var sortProperty: SORT_PROPERTY     = SORT_PROPERTY.PRICE){
    fun setSortType(value: SORT_TYPE){
        sortType = value
    }
    fun setSortProperty(value: SORT_PROPERTY){
        sortProperty = value
    }
    fun getSortType()       = sortType
    fun getSortProperty()   = sortProperty
}

data class FilterData(
    private var brend: Int                      = ANY_VALUE,
    private var favorite: Int                   = ANY_VALUE,
    private var priceRange: Pair<Float, Float>
                                                = 0.00f to 0.00f,
    private var category: Int                   = ANY_VALUE
    ) {
        fun setBrend(value: Int){
            brend = value
        }
        fun setFavorite(value: Int){
            favorite = value
        }
        fun setPriceRange(value: Pair<Float, Float>){
            priceRange = value
        }
        fun setCategory(value: Int){
            category = value
        }
        fun getBrend()          = brend
        fun getFavorite()       = favorite
        fun getPriceRange()     = priceRange
        fun getCategory()       = category
        fun isFiltered() : Boolean {
            return brend != ANY_VALUE || favorite != ANY_VALUE || category != ANY_VALUE || priceRange.second != 0.00f
        }
        fun default() {
             brend           = ANY_VALUE
             favorite        = ANY_VALUE
             priceRange      = 0.00f to 0.00f
             category        = ANY_VALUE
        }
}

interface FilterDataBuilder {
    fun addSortType(sortType: SORT_TYPE):               FilterDataBuilder
    fun addSortProperty(sortProperty: SORT_PROPERTY):   FilterDataBuilder
    fun addBrend(brend: Int):                           FilterDataBuilder
    fun addFavorite(favorite: Int):                     FilterDataBuilder
    fun addPriceRange(from: Float, to: Float):          FilterDataBuilder
    fun addCategory(category: Int):                     FilterDataBuilder
    fun build():                                        FilterData
}

class ProductBuilder: FilterDataBuilder {
    private var sortType: SORT_TYPE             = SORT_TYPE.ASCENDING
    private var sortProperty: SORT_PROPERTY     = SORT_PROPERTY.PRICE
    private var brend: Int                      = ANY_VALUE
    private var favorite: Int                   = ANY_VALUE
    private var priceRange: Pair<Float, Float>
            = 0.00f to 0.00f
    private var category: Int                   = ANY_VALUE

    override fun addSortType(sortType: SORT_TYPE): FilterDataBuilder {
        this.sortType = sortType
        return this
    }

    override fun addSortProperty(sortProperty: SORT_PROPERTY): FilterDataBuilder {
        this.sortProperty = sortProperty
        return this
    }

    override fun addBrend(brend: Int): FilterDataBuilder {
        this.brend = brend
        return this
    }

    override fun addFavorite(favorite: Int): FilterDataBuilder {
        this.favorite = favorite
        return this
    }

    override fun addPriceRange(from: Float, to: Float): FilterDataBuilder {
        this.priceRange= Pair(from, to)
        return this
    }

    override fun addCategory(category: Int): FilterDataBuilder {
        this.category = category
        return this
    }
    override fun build() = FilterData(
        sortType,
        sortProperty,
        brend,
        favorite,
        priceRange,
        category
    )
}
*/
/*
class EasyTest(){
    private val filterProduct = ProductBuilder().addBrend(2).build()
    init{
        log(filterProduct.getBrend().toString())
    }

}*/

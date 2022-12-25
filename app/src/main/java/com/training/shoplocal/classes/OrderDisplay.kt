package com.training.shoplocal.classes

import com.training.shoplocal.FieldFilter
import com.training.shoplocal.log
import com.training.shoplocal.screens.ScreenItem

/**
  Класс для выборки данных (product) из БД (таблица products),
 * определяет тип сортировки и фильтр выборки по параметрам продукта
 */

const val ANY_VALUE =    -1
enum class SORT_TYPE(val value: Int)     {ASCENDING(0), DESCENDING(1)}
enum class SORT_PROPERTY(val value: Int) {POPULAR(0), RATING(1), PRICE(2)}

class OrderDisplay{
    data class SortData(var sortType: SORT_TYPE             = SORT_TYPE.ASCENDING,
                        var sortProperty: SORT_PROPERTY     = SORT_PROPERTY.PRICE)

    data class FilterData(
        var brend: Int                      = ANY_VALUE,
        var favorite: Int                   = 0,
        var priceRange: Pair<Float, Float>
        = 0.00f to 0.00f,
        var category: Int                   = ANY_VALUE
    )
    private val sortData                = SortData()
    private val filterData              = FilterData()
    private var screenData: ScreenItem  = ScreenItem.MainScreen
    fun setSortType(value: SORT_TYPE){
        sortData.sortType = value
    }
    fun setSortProperty(value: SORT_PROPERTY){
        sortData.sortProperty = value
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

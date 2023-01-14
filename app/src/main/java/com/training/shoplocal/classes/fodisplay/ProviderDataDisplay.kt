package com.training.shoplocal.classes.fodisplay

import com.training.shoplocal.log
import com.training.shoplocal.screens.ScreenItem


enum class FieldFilter {
    SORT_TYPE,
    SORT_PROPERTY,
    BREND,
    CATEGORY,
    FAVORITE,
    PRICE_RANGE,
    SCREEN
}
const val ANY_VALUE =    -1
enum class SORT_TYPE(val value: Int)     {ASCENDING(0), DESCENDING(1)}
enum class SORT_PROPERTY(val value: Int) {PRICE(0), POPULAR(1), RATING(2)}

interface ProviderDataDisplay {
    fun getSortType():      SORT_TYPE
    fun getSortProperty():  SORT_PROPERTY
    fun getBrend():         Int
    fun getCategory():      Int
    fun getFavorite():      Int
    fun getPriceRange():    Pair<Float, Float>
    //fun getScreenData():    ScreenItem

    /*fun getFieldValue(field: FieldFilter): Any{
        return when (field) {
            FieldFilter.SORT_TYPE     -> getSortType()
            FieldFilter.SORT_PROPERTY -> getSortProperty()
            FieldFilter.BREND         -> getBrend()
            FieldFilter.CATEGORY      -> getCategory()
            FieldFilter.FAVORITE      -> getFavorite()
            FieldFilter.PRICE_RANGE   -> getPriceRange()
            FieldFilter.SCREEN        -> getScreenData()
        }
    }*/

    fun getDataSearchQuery(): String {
        val sort_order          = getSortType().value
        val sort_type           = getSortProperty().value
        val filter_category     = getCategory()
        val filter_brend        = getBrend()
        val filter_favorite     = getFavorite()
        val filter_price        = run {
            val value: Pair<Float, Float>   = getPriceRange()
            "${value.first}-${value.second}"
        }
        /*val current_screen = when (OrderDisplay.instance.getScreenData()) {
            ScreenItem.MainScreen    -> 0
            ScreenItem.CatalogScreen -> 1
            ScreenItem.CartScreen    -> 2
            ScreenItem.ProfileScreen -> 3
            else                     -> -1
        }*/
        /** Порядок для извлечения в PHP:
         *  sort_order:         0 - ASCENDING, 1 - DESCENDING
         *  sort_type:          0 POPULAR, 1 - RATING, 2 - PRICE
         *  filter_category:    ID категории продукта
         *  filter_brand:       ID бренда
         *  filter_favorite:    0 - все продукты, 1 - избранное
         *  filter_price:       интервал цен, н/р 1000,00-20000,00
         *  current_screen:     текущий экран
         */
        return "$sort_order $sort_type $filter_category $filter_brend $filter_favorite $filter_price"// $current_screen"
    }



}
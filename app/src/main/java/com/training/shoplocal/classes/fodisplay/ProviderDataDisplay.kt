package com.training.shoplocal.classes.fodisplay

import androidx.compose.runtime.*
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
    var state: MutableState<Boolean>
    fun setSortType(value: SORT_TYPE)
    fun setSortProperty(value: SORT_PROPERTY)
    fun setBrend(value: Int)
    fun setCategory(value: Int)
    fun setFavorite(value: Int)
    fun setPriceRange(valueFrom: Float, valueTo: Float)
    fun setScreen(value: Int)

    fun getSortType():      SORT_TYPE
    fun getSortProperty():  SORT_PROPERTY
    fun getBrend():         Int
    fun getCategory():      Int
    fun getFavorite():      Int
    fun getPriceRange():    Pair<Float, Float>
    fun getScreen():        Int

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
        val screen = getScreen()
        /** Порядок для извлечения в PHP:
         *  sort_order:         0 - ASCENDING, 1 - DESCENDING
         *  sort_type:          0 POPULAR, 1 - RATING, 2 - PRICE
         *  filter_category:    ID категории продукта
         *  filter_brand:       ID бренда
         *  filter_favorite:    0 - все продукты, 1 - избранное
         *  filter_price:       интервал цен, н/р 1000,00-20000,00
         *  current_screen:     текущий экран
         */
        return "$sort_order $sort_type $filter_category $filter_brend $filter_favorite $filter_price $screen"
    }



}
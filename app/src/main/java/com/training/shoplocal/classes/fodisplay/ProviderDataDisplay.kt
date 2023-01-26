package com.training.shoplocal.classes.fodisplay

import androidx.compose.runtime.*
import com.training.shoplocal.classes.DECIMAL_SEPARATOR
import com.training.shoplocal.log


enum class FieldFilter {
    SORT_TYPE,
    SORT_PROPERTY,
    BREND,
    CATEGORY,
    FAVORITE,
    PRICE_RANGE,
    DISCOUNT,
    SCREEN
}

enum class VIEW_MODE(val value: Int)     {CARD(0), ROW(1)}
enum class SORT_TYPE(val value: Int)     {ASCENDING(0), DESCENDING(1)}
enum class SORT_PROPERTY(val value: Int) {PRICE(0), POPULAR(1), RATING(2)}

interface ProviderDataDisplay {
    var state: MutableState<Boolean>
    fun equalsFilterData(filter:FilterData): Boolean
    fun equalsFilterViewMode(filter:FilterData): Boolean
    fun setFilter(filter:FilterData)
    fun resetFilter(): Boolean
    fun setViewMode(value: VIEW_MODE)
    fun setSortType(value: SORT_TYPE)
    fun setSortProperty(value: SORT_PROPERTY)
    fun setBrend(value: String)
    fun setCategory(value: String)
    fun setFavorite(value: Int)
    fun setPriceRange(valueFrom: Float, valueTo: Float)
    fun setScreen(value: Int)
    fun setDiscount(value: Int)

    fun getViewMode():      VIEW_MODE
    fun getSortType():      SORT_TYPE
    fun getSortProperty():  SORT_PROPERTY
    fun getBrend():         String
    fun getCategory():      String
    fun getFavorite():      Int
    fun getPriceRange():    Pair<Float, Float>
    fun getScreen():        Int
    fun getDiscount():      Int

    fun getFilterQuery(): String {
        val sort_order          = getSortType().value
        val sort_type           = getSortProperty().value
        val filter_category     = getCategory()
        val filter_brend        = getBrend()
        val filter_favorite     = getFavorite()
        val filter_price        = run {
            val value: Pair<Float, Float>   = getPriceRange()
            "${value.first}-${value.second}"
        }
        val filter_discount = getDiscount()
        val filter_screen = getScreen()
        /** Порядок для извлечения в PHP:
         *  0 - sort_order:         0 - ASCENDING, 1 - DESCENDING
         *  1 - sort_type:          0 POPULAR, 1 - RATING, 2 - PRICE
         *  2 - filter_category:    ID категории продукта
         *  3 - filter_brand:       ID бренда
         *  4 - filter_favorite:    0 - все продукты, 1 - избранное
         *  5 - filter_price:       интервал цен, н/р 1000,00-20000,00
         *  6 - filter_discount:    скидка
         *  7 - filrter_screen:     текущий экран
         */
        return "$sort_order $sort_type $filter_category $filter_brend $filter_favorite $filter_price $filter_discount $filter_screen"
    }



}
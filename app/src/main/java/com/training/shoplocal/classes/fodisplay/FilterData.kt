package com.training.shoplocal.classes.fodisplay

import com.training.shoplocal.classes.ANY_VALUE

data class FilterData(
    var brend: String                   = ANY_VALUE.toString(),
    var favorite: Int                   = 0,
    var priceRange: Pair<Float, Float>
    = 0f to 0f,
    var category: String                = ANY_VALUE.toString(),
    var discount: Int                   = 0
) {
    override fun equals(other: Any?): Boolean {
        val filter = other as FilterData
        return     filter.brend             == brend
                && filter.favorite          == favorite
                && filter.priceRange.first  == priceRange.first
                && filter.priceRange.second == priceRange.second
                && filter.category          == category
                && filter.discount          == discount
    }
}

package com.training.shoplocal.classes

/**
  Класс для выборки данных (product) из БД (таблица products),
 * определяет тип сортировки и фильтр выборки по параметрам продукта
 */
const val ANY_VALUE =    -1
enum class SORT_TYPE     {ASCENDING, DESCENDING}
enum class SORT_PROPERTY {POPULAR, RATING, PRICE}
class FilterData(
    var sortType: SORT_TYPE             = SORT_TYPE.ASCENDING,
    var sortProperty: SORT_PROPERTY     = SORT_PROPERTY.PRICE,
    var brend: Int                      = ANY_VALUE,
    var favorite: Int                   = ANY_VALUE,
    var priceRange: Pair<Float, Float>
                                        = 0.00f to 0.00f,
    var category: Int                   = ANY_VALUE
    ) {
    fun isFiltered() : Boolean {
        return brend != ANY_VALUE || favorite != ANY_VALUE || category != ANY_VALUE || priceRange.second != 0.00f
    }
}

interface FilterDataBuilder {
    fun addBrend(brend: Int):                  FilterDataBuilder?
    fun addFavorite(value: Boolean):           FilterDataBuilder?
    fun addPriceRange(from: Int, to: Int):     FilterDataBuilder?
    fun addCategory(category: Int):            FilterDataBuilder?
    fun default():                             FilterDataBuilder?
    fun build():                               FilterData?
}

class FilterProduct: FilterDataBuilder {
    private var sortType: SORT_TYPE             = SORT_TYPE.ASCENDING
    private var sortProperty: SORT_PROPERTY     = SORT_PROPERTY.PRICE
    private var brend: Int                      = ANY_VALUE
    private var favorite: Int                   = ANY_VALUE
    private var priceRange: Pair<Float, Float>  = 0.00f to 0.00f
    private var category: Int                   = ANY_VALUE

    override fun addBrend(brend: Int): FilterDataBuilder? {
        TODO("Not yet implemented")
    }

    override fun addFavorite(value: Boolean): FilterDataBuilder? {
        TODO("Not yet implemented")
    }

    override fun addPriceRange(from: Int, to: Int): FilterDataBuilder? {
        TODO("Not yet implemented")
    }

    override fun addCategory(category: Int): FilterDataBuilder? {
        TODO("Not yet implemented")
    }

    override fun default(): FilterDataBuilder? {
        TODO("Not yet implemented")
    }

    override fun build(): FilterData? {
        TODO("Not yet implemented")
    }
}
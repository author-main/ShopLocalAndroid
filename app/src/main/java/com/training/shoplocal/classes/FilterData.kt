package com.training.shoplocal.classes

import com.training.shoplocal.log

/**
  Класс для выборки данных (product) из БД (таблица products),
 * определяет тип сортировки и фильтр выборки по параметрам продукта
 */
const val ANY_VALUE =    -1
enum class SORT_TYPE     {ASCENDING, DESCENDING}
enum class SORT_PROPERTY {POPULAR, RATING, PRICE}

data class FilterData(
    private var sortType: SORT_TYPE             = SORT_TYPE.ASCENDING,
    private var sortProperty: SORT_PROPERTY     = SORT_PROPERTY.PRICE,
    private var brend: Int                      = ANY_VALUE,
    private var favorite: Int                   = ANY_VALUE,
    private var priceRange: Pair<Float, Float>
                                        = 0.00f to 0.00f,
    private var category: Int                   = ANY_VALUE
    ) {
        fun setSortType(value: SORT_TYPE){
            sortType = value
        }
        fun setSortProperty(value: SORT_PROPERTY){
            sortProperty = value
        }
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
        fun getSortType()       = sortType
        fun getSortProperty()   = sortProperty
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

class EasyTest(){
    private val filterProduct = ProductBuilder().addBrend(2).build()
    init{
        log(filterProduct.getBrend().toString())
    }

}
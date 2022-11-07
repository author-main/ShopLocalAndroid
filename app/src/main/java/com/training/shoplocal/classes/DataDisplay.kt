package com.training.shoplocal.classes

/**
  Класс для выборки данных (product) из БД (таблица products),
 * определяет тип сортировки и фильтр выборки по параметрам продукта
 */

const val ANY_VALUE =    -1
enum class SORT_TYPE     {ASCENDING, DESCENDING}
enum class SORT_PROPERTY {POPULAR, RATING, PRICE}

class DataDisplay{
    data class SortData(var sortType: SORT_TYPE             = SORT_TYPE.ASCENDING,
                        var sortProperty: SORT_PROPERTY     = SORT_PROPERTY.PRICE)
    {}
    data class FilterData(
        var brend: Int                      = ANY_VALUE,
        var favorite: Int                   = ANY_VALUE,
        var priceRange: Pair<Float, Float>
        = 0.00f to 0.00f,
        var category: Int                   = ANY_VALUE
    ) {}
    private val sortData    = SortData()
    private val filterData  = FilterData()
    fun setSortType(value: SORT_TYPE){
        sortData.sortType = value
    }
    fun setSortProperty(value: SORT_PROPERTY){
        sortData.sortProperty = value
    }
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
        return filterData.brend != ANY_VALUE || filterData.favorite != ANY_VALUE || filterData.category != ANY_VALUE || filterData.priceRange.second != 0.00f
    }
    fun resetFilter() {
        filterData.brend           = ANY_VALUE
        filterData.favorite        = ANY_VALUE
        filterData.priceRange      = 0.00f to 0.00f
        filterData.category        = ANY_VALUE
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

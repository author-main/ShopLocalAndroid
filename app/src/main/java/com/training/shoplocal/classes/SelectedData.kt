package com.training.shoplocal.classes

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
}

interface FilterDataBuilder {
    fun addSortType(sortType: SORT_TYPE):               FilterDataBuilder
    fun addSortProperty(sortProperty: SORT_PROPERTY):   FilterDataBuilder
    fun addBrend(brend: Int):                           FilterDataBuilder
    fun addFavorite(favorite: Int):                     FilterDataBuilder
    fun addPriceRange(from: Float, to: Float):          FilterDataBuilder
    fun addCategory(category: Int):                     FilterDataBuilder
    fun default():                                      FilterDataBuilder
    fun build():                                        FilterData
}

class ProductBuilder: FilterDataBuilder {
 /*   init {
        instance = ProductBuilder()
    }*/
    private val filterdata = FilterData()
    override fun addSortType(sortType: SORT_TYPE): FilterDataBuilder {
        filterdata.setSortType(sortType)
        return this
    }

    override fun addSortProperty(sortProperty: SORT_PROPERTY): FilterDataBuilder {
        filterdata.setSortProperty(sortProperty)
        return this
    }

    override fun addBrend(brend: Int): FilterDataBuilder {
        filterdata.setBrend(brend)
        return this
    }

    override fun addFavorite(favorite: Int): FilterDataBuilder {
        filterdata.setFavorite(favorite)
        return this
    }

    override fun addPriceRange(from: Float, to: Float): FilterDataBuilder {
        filterdata.setPriceRange(Pair(from, to))
        return this
    }

    override fun addCategory(category: Int): FilterDataBuilder {
        filterdata.setCategory(category)
        return this
    }

    override fun default(): FilterDataBuilder {
        with(filterdata){
            /*setSortType(SORT_TYPE.ASCENDING)
            setSortProperty(SORT_PROPERTY.PRICE)*/
            setBrend(ANY_VALUE)
            setFavorite(ANY_VALUE)
            setPriceRange(0.00f to 0.00f)
            setCategory(ANY_VALUE)
        }
        return this
    }

    override fun build() = filterdata
}

/*class EasyTest(){
    val filterProduct = ProductBuilder().addBrend(2).build()
}*/
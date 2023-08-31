package com.training.shoplocal.classes.fodisplay

import android.provider.ContactsContract.Data
import androidx.compose.runtime.*
import com.training.shoplocal.classes.ANY_VALUE
import com.training.shoplocal.classes.EMPTY_STRING
import com.training.shoplocal.log
import com.training.shoplocal.screens.ScreenItem

class OrderDisplay: ProviderDataDisplay {
    data class SortData(
        var sortType: SORT_TYPE = SORT_TYPE.ASCENDING,
        var sortProperty: SORT_PROPERTY = SORT_PROPERTY.PRICE
    )

    private val sortData = SortData()
    private var filterData = FilterData()
    private var currentScreen: Int = 1 //MAIN_SCREEN

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

    override fun setSortType(value: SORT_TYPE) {
        sortData.sortType = value
    }

    override fun setSortProperty(value: SORT_PROPERTY) {
        if (value != sortData.sortProperty)
            sortData.sortProperty = value
        else
            invertSortType()
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

    private fun invertSortType() {
        if (sortData.sortType == SORT_TYPE.ASCENDING)
            sortData.sortType = SORT_TYPE.DESCENDING
        else
            sortData.sortType = SORT_TYPE.ASCENDING
    }

    override fun resetFilter(): Int {
        val CHANGED_FILTER = 0
        val CHANGED_VIEWMODE = 1
        val NO_CHANGED = -1
        val filter = FilterData()
        val changedFilter = if (!equalsFilterData(filter)) CHANGED_FILTER else NO_CHANGED
        val changedViewMode = if (!equalsFilterViewMode(filter)) CHANGED_VIEWMODE else NO_CHANGED
        filterData = filter
        return if (changedFilter < 0) changedViewMode else CHANGED_FILTER
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
        private lateinit var instance: ProviderDataDisplay
        fun getInstance(): ProviderDataDisplay {
            if (!this::instance.isInitialized)
                instance = OrderDisplay()
            return instance
        }

        fun clone(): ProviderDataDisplay {
            val dataDisplay = OrderDisplay()
            with(dataDisplay) {
                filterData = instance.getFilter().copy()
                sortData.sortType = instance.getSortType()
                sortData.sortProperty = instance.getSortProperty()
                currentScreen = instance.getScreen()
            }
            return dataDisplay
        }

        fun restoreDataDisplay(backup: ProviderDataDisplay) {
            instance = backup
        }

        fun resetFilter(): Int {
            getInstance()
            return instance.resetFilter()
        }

        fun setFilter(filter: FilterData) {
            getInstance()
            instance.setFilter(filter)
        }

        fun getViewMode(): VIEW_MODE {
            getInstance()
            return instance.getViewMode()
        }

        fun equalsFilterData(filter: FilterData): Boolean {
            getInstance()
            return instance.equalsFilterData(filter)
        }

        fun getFilterQuery(): String {
            getInstance()
            return instance.getFilterQuery()
        }
    }
}
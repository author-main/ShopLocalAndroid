package com.training.shoplocal.classes.fodisplay

import com.training.shoplocal.classes.BREND_ITEM
import com.training.shoplocal.classes.CATEGORY_ITEM


data class ItemFilter(
    val id: Int,
    val name: String,
    //val parent: Int,
    var selected: Boolean = false
) {
/*
    fun isHeader() =
        id < 0
    fun isCategory() =
        id == CATEGORY_ITEM
    fun isBrand() =
        id == BREND_ITEM*/
}

package com.training.shoplocal.classes.fodisplay

import com.training.shoplocal.classes.BREND_ITEM
import com.training.shoplocal.classes.CATEGORY_ITEM


data class ItemFilter(
    val id: Int,
    val name: String,
    var selected: Boolean = false
)

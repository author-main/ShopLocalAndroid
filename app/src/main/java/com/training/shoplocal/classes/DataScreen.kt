package com.training.shoplocal.classes

data class DataScreen(
   /* var firstItemIndex:                     Int = 0,
    var firstItemOffset:                    Int = 0,*/
    var maxPortion:                         Int = -1,
    var products:                           MutableList<Product> = mutableListOf()
)

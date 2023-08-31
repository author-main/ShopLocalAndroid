package com.training.shoplocal.classes.screenhelpers

import com.training.shoplocal.classes.Product
import com.training.shoplocal.classes.fodisplay.OrderDisplay
import com.training.shoplocal.classes.fodisplay.ProviderDataDisplay

data class DataScreen(
    var firstItemIndex:                     Int = 0,
    var loadedPortion:                      Int = 0,
    var products:                           MutableList<Product> = mutableListOf(),
    var datadisplay:                        ProviderDataDisplay = OrderDisplay()
)

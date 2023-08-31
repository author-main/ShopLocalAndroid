package com.training.shoplocal.classes

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName
import com.training.shoplocal.R
import com.training.shoplocal.classes.downloader.Callback
import com.training.shoplocal.classes.downloader.ImageLinkDownloader
import com.training.shoplocal.getStringResource
import com.training.shoplocal.log

data class Product(
    @SerializedName("id")
    var id: Int,
    @SerializedName("name")
    var name:           String,
    @SerializedName("category")
    var category:       Int? = null,
    @SerializedName("description")
    var description:    String,
    @SerializedName("instock")
    var instock:        Int = 1,
    @SerializedName("discount")
    var discount:       Int,
    @SerializedName("price")
    var price:          Float,
    @SerializedName("star")
    var star:           Float = 1.0f,
    @SerializedName("favorite")
    var favorite:       Byte = 0,
    @SerializedName("brand")
    var brand:          Int? = null,
    @SerializedName("sold")
    var sold:          Int? = null,
    @SerializedName("linkimages")
    var linkimages:     List<String>? = null,
    ) {
        constructor(): this(
                id              = -1,
                name            = "",
                category        = 0,
                description     = "",
                instock         = 0,
                discount        = 0,
                price           = 0.0f,
                star            = 1.0f,
                favorite        = 0,
                brand           = 0,
                sold            = 0,
                linkimages      =  emptyList<String>()
        )

    fun getTypeSale() =
        if (star >= 4)
            getStringResource(R.string.text_bestseller)
        else if (discount > 0)
            getStringResource(R.string.text_action)
        else
            ""
    fun copydata(value: Product){
        id              = value.id
        name            = value.name
        category        = value.category
        description     = value.description
        instock         = value.instock
        discount        = value.discount
        price           = value.price
        star            = value.star
        favorite        = value.favorite
        brand           = value.brand
        sold            = value.sold
        linkimages      = value.linkimages
    }
 }
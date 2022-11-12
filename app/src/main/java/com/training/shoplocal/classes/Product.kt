package com.training.shoplocal.classes

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName
import com.training.shoplocal.Error

data class Product(val id: Int,
    @SerializedName("id")
    val name:           String,
    @SerializedName("category")
    val category:       Int? = null,
    @SerializedName("description")
    val description:    String,
    @SerializedName("instock")
    val instock:        Int = 1,
    @SerializedName("discount")
    val discount:       Float,
    @SerializedName("price")
    val price:          Float,
    @SerializedName("star")
    val star:           Byte? = 1,
    @SerializedName("favorite")
    var favorite:       Byte? = 0,
    @SerializedName("ibrand")
    val brand:          Short? = null,
    @SerializedName("linkimages")
    val linkimages:     MutableList<String>? = null
    ) {
/**
 * // Структура таблицы PRODUCTS //
 * `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
 * `name` VARCHAR(50) NOT NULL,
 * `category` INT UNSIGNED NULL,
 * `description` TEXT NOT NULL,
 * `instock` INT NOT NULL DEFAULT 1,
 * `price` DECIMAL(10,2) UNSIGNED NOT NULL DEFAULT 0.00,
 * `deleted` TINYINT(1) UNSIGNED NOT NULL DEFAULT 0,
 * `star` TINYINT NULL DEFAULT 1,
 * `brand` INT UNSIGNED NULL,
 * FOREIGN KEY (`category`) REFERENCES `shop_local`.`category` (`id`)
 * FOREIGN KEY (`brand`) REFERENCES `shop_local`.`brands` (`id`)
 */

    private var mainImage: Bitmap? = null
    fun getImage(action: (image: Bitmap?) -> Unit = {}) {
        if (mainImage == null) {
            if (!linkimages.isNullOrEmpty())
                ImageLinkLoader().getLinkImage(linkimages[0], object : Callback {
                        override fun onComplete(image: Bitmap) {
                            mainImage = image
                            action(image)
                        }
                        override fun onFailure(error: Error) {
                        }
                    })
         }
        else
            action(mainImage)
    }
}

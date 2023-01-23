package com.training.shoplocal.classes

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("link_img")
    val link_img: String
)
    /*private var image: Bitmap? = null
    fun getImage(action: (image: Bitmap?) -> Unit = {}) {
        if (image == null) {

        }
        action(image)
    }*/
/*  // Структура таблицы CATEGORY //
 *  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
 *  `name` VARCHAR(30) NOT NULL,
 *  `link_img` VARCHAR(45) NOT NULL
*/


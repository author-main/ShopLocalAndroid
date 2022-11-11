package com.training.shoplocal.classes

import android.graphics.Bitmap

data class Category(val id: Int, val name: String, val linkImage: String){
    private var image: Bitmap? = null
    fun getImage(action: (image: Bitmap?) -> Unit = {}) {
        if (image == null) {

        }
        action(image)
    }
/*  // Структура таблицы CATEGORY //
 *  `id` INT UNSIGNED NOT NULL AUTO_INCREMENT,
 *  `name` VARCHAR(30) NOT NULL,
 *  `link_img` VARCHAR(45) NOT NULL
*/
}

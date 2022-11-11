package com.training.shoplocal.classes

import android.graphics.Bitmap
import com.training.shoplocal.Error
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
data class Product(val id: Int,
    val name:           String,
    val category:       Int? = null,
    val description:    String,
    val instock:        Int = 1,
    val discount:       Float,
    val price:          Float,
    val star:           Byte? = 1,
    var favorite:       Byte? = 0,
    val brand:          Short? = null,
    val linkImages:     MutableList<String>? = null
    ) {

    private var mainImage: Bitmap? = null
    fun getImage(action: (image: Bitmap?) -> Unit = {}) {
        if (mainImage == null) {
            if (!linkImages.isNullOrEmpty()) {
                ImageLinkLoader().getLinkImage(linkImages[0], object : Callback {
                        override fun onComplete(image: Bitmap) {
                            mainImage = image
                            action(image)
                        }
                        override fun onFailure(error: Error) {
                        }
                    })
                }
        } else
        action(mainImage)
    }
}

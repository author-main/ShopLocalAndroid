package com.training.shoplocal.classes

import android.graphics.Bitmap
import com.google.gson.annotations.SerializedName
import com.training.shoplocal.classes.downloader.Callback
import com.training.shoplocal.classes.downloader.ImageLinkDownloader
import com.training.shoplocal.log

data class Product(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name:           String,
    @SerializedName("category")
    val category:       Int? = null,
    @SerializedName("description")
    val description:    String,
    @SerializedName("instock")
    val instock:        Int = 1,
    @SerializedName("discount")
    val discount:       Int,
    @SerializedName("price")
    val price:          Float,
    @SerializedName("star")
    val star:           Float = 1.0f,
    @SerializedName("favorite")
    var favorite:       Byte? = 0,
    @SerializedName("brand")
    val brand:          Int? = null,
    @SerializedName("linkimages")
    val linkimages:     List<String>? = null
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
 }
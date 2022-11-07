package com.training.shoplocal.classes

/**
  Класс для выборки данных (product) из БД (таблица products),
 * определяет тип сортировки и фильтр выборки по параметрам продукта
 */
const val ANY_VALUE = -1
enum class SORT_TYPE     {ASCENDING, DESCENDING}
enum class SORT_PROPERTY {POPULAR, RATING, PRICE}
class Data {
    var sortType        = SORT_TYPE.ASCENDING
    var sortProperty    = SORT_PROPERTY.PRICE
    var brend: Int      = ANY_VALUE
    var favorite: Int   = ANY_VALUE
    var price           = arrayOf<Float>(0.00f, 0.00f)
    var category: Int   = ANY_VALUE
}
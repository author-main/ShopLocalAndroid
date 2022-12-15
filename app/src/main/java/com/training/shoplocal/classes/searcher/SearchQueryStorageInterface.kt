package com.training.shoplocal.classes.searcher

interface SearchQueryStorageInterface {
    val fileNameStorage: String
    /**
     * Сохранить строку поискового запроса
     * @param value - строка запроса
     */
    fun put(value: String)
    /**
     * Удалить строку поискового запроса
     * @param value - строка запроса
     */
    fun remove(value: String)
    /**
     * Получить весь список сохраненных строк поисковых запросов
     * @return список строк
     */
    fun getQueries(): List<String>
    /**
     * Сохраняет список строк поисковых запросов
     * @param - список строк
     * @return true - данные сохранены, false - ошибка сохранения данных
     */
    fun saveQueries(list: List<String>): Boolean
    /**
     * Переместить строку запроса в начало списка
     * @param - строка поискового запроса
     */
    fun moveFirst(value: String)
    fun removeAllQueries()
}
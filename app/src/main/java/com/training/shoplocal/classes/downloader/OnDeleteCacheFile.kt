package com.training.shoplocal.classes.downloader

/**
 *  Необходимо реализовать в случае state записи журнала кэша
 *  в состоянии REMOVE или DIRTY c размером файла 0 (недогруженный файл),
 *  удалите эти записи из файла журнала и,
 *  соответсвенно, файлы в кэше hash, hash.t
 *  @param hash уникальное имя(хэш) файла
 */
interface OnDeleteCacheFile {
    fun deleteCacheFile(hash: String)
}
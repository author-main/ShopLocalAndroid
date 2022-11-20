package com.training.shoplocal

import java.io.File
import java.io.FileFilter
import java.io.IOException

const val HASH_LENGTH = 32
const val CACHE_SIZE  = 50 * 1024 * 1024  // 50Мб

fun renameFile(source: String, dest: String){
    renameFile(File(source), File(dest))
}

fun renameFile(source: File, dest: File){
    try {
        if (source.exists()) {
            deleteFile(dest)
            source.renameTo(dest)
        }
    } catch (_: IOException){}
}

fun deleteFile(filename: String) {
    deleteFile(File(filename))
}

fun deleteFiles(folderName: String, mask: String = "*"){
    val dir = File(folderName)
    val files = dir.listFiles(FileFilter {
        if (mask == "*")
            true
        else
            it.extension == mask
    })
    files?.forEach {
        deleteFile(it)
    }
}

fun getFileSize(filename: String): Long {
    val file = File(filename)
    return if (file.exists())
                file.length()
           else
                0
}

fun deleteFile(file: File) {
    try {
        file.delete()
    } catch (_: IOException){}
}

fun createDirectory(value: String): Boolean {
    val dir: File = File(value)
    return if (!dir.exists()) {
        try {
            dir.mkdirs()
        } catch (_: IOException) {
            false
        }
    } else
        true
}
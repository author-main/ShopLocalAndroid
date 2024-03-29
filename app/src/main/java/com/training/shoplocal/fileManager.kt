package com.training.shoplocal

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileFilter
import java.io.IOException
import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import kotlin.math.roundToInt

val CART_IMAGE_SIZE = 150.Px
const val HASH_LENGTH = 32
const val DISK_CACHESIZE  = 50 * 1024 * 1024  // 50Мб
const val EXT_CACHETEMPFILE = "t"
enum class StateEntry(val value: String){
    CLEAN   ("CLEAN "), // файл кэша свободен
    DIRTY   ("DIRTY "), // файл кэша занят, чтение/запись
    REMOVE  ("REMOVE")  // удалить сведения, файл из кэша
}

val CACHE_DIR = getCacheDirectory()

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

fun fileExists(fileName: String): Boolean {
    val file = File(fileName)
    return file.exists()
}

fun fileExists(file: File) = file.exists()

fun getCacheDirectory(): String =
    AppShopLocal.appContext().applicationInfo.dataDir + "/cache/"


fun getTempDirectory(): String =
    AppShopLocal.appContext().applicationInfo.dataDir + "/temp/"

fun md5(link: String): String {
    try {
        val md = MessageDigest.getInstance("MD5")
        val messageDigest = md.digest(link.toByteArray())
        var hashtext = BigInteger(1, messageDigest).toString(16)
        /*try {
            hashtext = "0".repeat(HASH_LENGTH - hashtext.length) + hashtext
        } catch (_: IllegalArgumentException) {}*/
        while (hashtext.length < HASH_LENGTH)
            hashtext = "0$hashtext"
        return hashtext
    } catch (e: NoSuchAlgorithmException) {
        throw RuntimeException(e)
    }
}

fun loadBitmap(filename: String, reduce: Boolean = false): Bitmap?{
    if (!File(filename).exists())
        return null
    return try {
        val option = BitmapFactory.Options()
        if (reduce) {
            option.inJustDecodeBounds = true
            BitmapFactory.decodeFile(filename, option)
            val reduceSize = option.outWidth.coerceAtLeast(option.outHeight).toDouble()
            val inSampleSize = (reduceSize / CART_IMAGE_SIZE).roundToInt()
            //log("inSampleSize = $inSampleSize, bitmapWidth = ${option.outWidth}")
            option.inSampleSize = inSampleSize * 2
            option.inJustDecodeBounds = false
        }
        option.inPreferredConfig = Bitmap.Config.ARGB_8888
        val bitmap = BitmapFactory.decodeFile(filename, option)
        //log("loadedBitmapWidth = ${bitmap.width}")
        bitmap
    } catch(_:Exception){
        null
    }
}

fun fileNameFromPath(path: String): String {
    val file = File(path)
    return file.name
}



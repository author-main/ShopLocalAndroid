package com.training.shoplocal.classes.downloader

import java.util.concurrent.Callable

interface DownloadTask<T>: Callable<T> {
    fun download(url: String): T
}
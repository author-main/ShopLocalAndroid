package com.training.shoplocal.classes.downloader

import android.graphics.Bitmap

class DownloadImageTask(private val link: String,
                        private val cache: DiskCache,
                        private val callback: Callback): DownloadTask<Bitmap?>
{
    override fun download(link: String): Bitmap? {
        TODO("Not yet implemented")
    }

    override fun call(): Bitmap? {
        val image = download(this.link)
        image?.let {bitmap ->
            callback.onComplete(bitmap)
            cache.put(link, bitmap)
        }
        return image
    }
}
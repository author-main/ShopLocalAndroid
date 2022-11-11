package com.training.shoplocal.classes

import android.graphics.Bitmap
import com.training.shoplocal.Error

interface Callback {
    fun onComplete(image: Bitmap)
    fun onFailure(error: Error)
}

class ImageLinkLoader {
    fun getLinkImage(link: String, callback: Callback){
        val image = Bitmap.createBitmap(100,100,Bitmap.Config.ALPHA_8)
        if (image != null)
            callback.onComplete(image)
        else
            callback.onFailure(Error.NO_CONNECTION)
    }
}
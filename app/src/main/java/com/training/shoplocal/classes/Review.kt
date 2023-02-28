package com.training.shoplocal.classes

import com.google.gson.annotations.SerializedName

data class Review (
    @SerializedName("comment")      var comment     : String,
    @SerializedName("username")     var username    : String,
    @SerializedName("countstar")    var countstar   : Int,
    @SerializedName("date")         var date        : String,
                                    var hasOverflow:Boolean = false
/*    $time = strtotime($datetimeFromMysql);
    $myFormatForView = date("m/d/y g:i A", $time);*/
)
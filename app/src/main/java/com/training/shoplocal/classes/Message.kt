package com.training.shoplocal.classes

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("id")       val id          :Int,
    @SerializedName("message")  val message     :String,
    @SerializedName("type")     val type        :Int,
    @SerializedName("read")     val read        :Int
)

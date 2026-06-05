package com.rafabs.sp4u.model

import com.google.gson.annotations.SerializedName

data class Parada(
    @SerializedName("cp") val cp: Int,
    @SerializedName("np") val np: String,
    @SerializedName("ed") val ed: String,
    @SerializedName("py") val py: Double,
    @SerializedName("px") val px: Double
)
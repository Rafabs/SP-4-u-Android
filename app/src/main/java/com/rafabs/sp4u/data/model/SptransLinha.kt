package com.rafabs.sp4u.model

import com.google.gson.annotations.SerializedName

data class SptransLinha(
    @SerializedName("cl") val cl: Int,      // Código identificador da linha
    @SerializedName("lc") val lc: Boolean,  // Indica se a linha é circular
    @SerializedName("lt") val lt: String,   // Letreiro numérico (ex: 8000)
    @SerializedName("sl") val sl: Int,      // Sentido (1 ou 2)
    @SerializedName("tp") val tp: String,   // Letreiro de destino
    @SerializedName("ts") val ts: String    // Letreiro de origem
)
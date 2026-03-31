package com.rafabs.sp4u.data.remote

import com.google.gson.JsonElement

// Modelo para o Feed Individual (Usado no getAirQuality)
data class WaqiResponse(
    val status: String,
    val data: JsonElement
) {
    fun isSuccess(): Boolean = status == "ok" && data.isJsonObject
}

data class AqiData(
    val aqi: Int,
    val city: CityData
)

data class CityData(
    val name: String,
    val geo: List<Double>? = null
)
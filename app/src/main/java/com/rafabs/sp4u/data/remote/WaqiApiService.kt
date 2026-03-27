package com.rafabs.sp4u.data.remote

import com.rafabs.sp4u.ui.aqi.WaqiResponse // Certifique-se de que o import está correto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WaqiApiService {

    // A função que o Fragment está tentando chamar
    @GET("feed/{city}/")
    suspend fun getAirQuality(
        @Path("city") city: String,
        @Query("token") token: String
    ): WaqiResponse
}
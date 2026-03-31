package com.rafabs.sp4u.data.remote

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WaqiApiService {

    @GET("feed/{city}/")
    suspend fun getAirQuality(
        @Path("city") city: String,
        @Query("token") token: String
    ): WaqiResponse // Agora ele encontrará a referência no mesmo pacote
}
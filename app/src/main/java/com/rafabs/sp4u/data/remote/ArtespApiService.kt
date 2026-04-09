package com.rafabs.sp4u.data.remote

import com.rafabs.sp4u.data.model.StatusResponse
import retrofit2.http.GET

interface ArtespApiService {
    @GET("metroferroviario/api/status/")
    suspend fun getStatus(): StatusResponse
}
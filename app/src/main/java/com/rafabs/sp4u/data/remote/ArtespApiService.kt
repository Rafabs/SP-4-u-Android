package com.rafabs.sp4u.data.remote

import com.rafabs.sp4u.data.model.StatusResponse
import retrofit2.http.GET
import retrofit2.http.Header

interface ArtespApiService {
    @GET("metroferroviario/api/status/")
    suspend fun getStatus(
        @Header("Authorization") auth: String = "Api-Key ${Secrets.CCM_TOKEN}"
    ): StatusResponse
}
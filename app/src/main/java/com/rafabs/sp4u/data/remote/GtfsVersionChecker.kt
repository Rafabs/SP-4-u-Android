package com.rafabs.sp4u.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

object GtfsVersionChecker {

    private const val AGENCY_URL =
        "https://raw.githubusercontent.com/Rafabs/SP-4-u-Web/main/public/gtfs-sptrans/agency.txt"

    suspend fun getRemoteVersion(): String? = withContext(Dispatchers.IO) {
        try {
            val content = URL(AGENCY_URL).readText()
            // Extrai o valor de "versao=XXXXXX" da URL da agencia
            val regex = Regex("versao=(\\d+)")
            regex.find(content)?.groupValues?.get(1)
        } catch (e: Exception) {
            null
        }
    }
}
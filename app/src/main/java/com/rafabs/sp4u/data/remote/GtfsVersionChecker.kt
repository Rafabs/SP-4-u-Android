package com.rafabs.sp4u.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

object GtfsVersionChecker {

    private const val AGENCY_SPTRANS =
        "https://raw.githubusercontent.com/Rafabs/SP-4-u-Web/main/public/gtfs-sptrans/agency.txt"
    private const val AGENCY_EMTU =
        "https://raw.githubusercontent.com/Rafabs/SP-4-u-Web/main/public/gtfs-emtu/agency.txt"

    suspend fun getRemoteVersion(source: String): String? = withContext(Dispatchers.IO) {
        try {
            val url = if (source == "SPTRANS") AGENCY_SPTRANS else AGENCY_EMTU
            val content = URL(url).readText()
            val regex = Regex("versao=(\\d+)")
            regex.find(content)?.groupValues?.get(1)
                ?: content.lines().getOrNull(1) // fallback: segunda linha do agency.txt
        } catch (e: Exception) {
            null
        }
    }
}
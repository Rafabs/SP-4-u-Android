package com.rafabs.sp4u.data.remote

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.URL

object GtfsDownloader {

    private const val BASE_URL = "https://raw.githubusercontent.com/Rafabs/SP-4-u-Web/main/public/gtfs-sptrans"
    private const val BASE_URL_EMTU = "https://raw.githubusercontent.com/Rafabs/SP-4-u-Web/main/public/gtfs-emtu"

    val urls = mapOf(
        "routes"         to "$BASE_URL/routes.txt",
        "stops"          to "$BASE_URL/stops.txt",
        "shapes"         to "$BASE_URL/shapes.txt",
        "emtu_routes"    to "$BASE_URL_EMTU/routes.txt",
        "emtu_stops"     to "$BASE_URL_EMTU/stops.txt",
        "emtu_shapes"    to "$BASE_URL_EMTU/shapes.txt"
    )

    suspend fun download(context: Context, key: String, onProgress: (String) -> Unit): File? {
        return withContext(Dispatchers.IO) {
            try {
                val url  = urls[key] ?: return@withContext null
                val file = File(context.cacheDir, "$key.txt")
                onProgress("Baixando $key.txt...")
                URL(url).openStream().use { input ->
                    file.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                file
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}
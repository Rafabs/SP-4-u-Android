package com.rafabs.sp4u.data.repository

import android.content.Context
import com.rafabs.sp4u.data.local.AppDatabase
import com.rafabs.sp4u.data.local.entity.Route
import com.rafabs.sp4u.data.local.entity.Shape
import com.rafabs.sp4u.data.local.entity.Stop
import com.rafabs.sp4u.data.remote.GtfsDownloader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class GtfsRepository(private val context: Context) {

    private val db = AppDatabase.getDatabase(context)

    private fun processRoutes(file: File, source: String): List<Route> {
        val routes = mutableListOf<Route>()
        var header: List<String>? = null
        file.bufferedReader(Charsets.UTF_8).useLines { lines ->
            lines.forEach { line ->
                if (header == null) {
                    header = line.split(",").map { it.trim().removeSurrounding("\"") }
                    return@forEach
                }
                val values = line.split(",").map { it.trim().removeSurrounding("\"") }
                if (values.size < (header?.size ?: 0)) return@forEach
                val row = header!!.zip(values).toMap()
                val id = row["route_id"] ?: return@forEach
                routes.add(Route(
                    routeId   = id,
                    shortName = row["route_short_name"] ?: "",
                    longName  = row["route_long_name"] ?: "",
                    color     = "#" + (row["route_color"] ?: "000000"),
                    textColor = "#" + (row["route_text_color"] ?: "FFFFFF"),
                    source    = source
                ))
            }
        }
        return routes
    }

    private fun processStops(file: File, source: String): List<Stop> {
        val stops = mutableListOf<Stop>()
        var header: List<String>? = null
        file.bufferedReader(Charsets.UTF_8).useLines { lines ->
            lines.forEach { line ->
                if (header == null) {
                    header = line.split(",").map { it.trim().removeSurrounding("\"") }
                    return@forEach
                }
                val values = line.split(",").map { it.trim().removeSurrounding("\"") }
                if (values.size < (header?.size ?: 0)) return@forEach
                val row = header!!.zip(values).toMap()
                try {
                    stops.add(Stop(
                        stopId   = row["stop_id"] ?: return@forEach,
                        stopName = row["stop_name"] ?: "",
                        stopLat  = row["stop_lat"]?.toDouble() ?: return@forEach,
                        stopLon  = row["stop_lon"]?.toDouble() ?: return@forEach,
                        source   = source
                    ))
                } catch (e: Exception) { }
            }
        }
        return stops
    }

    private suspend fun processShapes(file: File, source: String) {
        var header: List<String>? = null
        val batch = mutableListOf<Shape>()
        file.bufferedReader(Charsets.UTF_8).useLines { lines ->
            lines.forEach { line ->
                if (header == null) {
                    header = line.split(",").map { it.trim().removeSurrounding("\"") }
                    return@forEach
                }
                val values = line.split(",").map { it.trim().removeSurrounding("\"") }
                if (values.size < (header?.size ?: 0)) return@forEach
                val row = header!!.zip(values).toMap()
                try {
                    batch.add(Shape(
                        shapeId         = row["shape_id"] ?: return@forEach,
                        shapePtLat      = row["shape_pt_lat"]?.toDouble() ?: return@forEach,
                        shapePtLon      = row["shape_pt_lon"]?.toDouble() ?: return@forEach,
                        shapePtSequence = row["shape_pt_sequence"]?.toInt() ?: 0,
                        source          = source
                    ))
                    if (batch.size >= 1000) {
                        db.shapeDao().insertAll(batch)
                        batch.clear()
                    }
                } catch (e: Exception) { }
            }
        }
        if (batch.isNotEmpty()) db.shapeDao().insertAll(batch)
    }

    suspend fun importGtfs(
        source: String,
        onProgress: (String) -> Unit
    ) = withContext(Dispatchers.IO) {

        val prefix = source.lowercase()

        val routesFile = GtfsDownloader.download(context, "${prefix}_routes", onProgress)
        if (routesFile != null) {
            onProgress("Importando rotas...")
            val routes = processRoutes(routesFile, source)
            db.routeDao().deleteBySource(source)
            db.routeDao().insertAll(routes)
            onProgress("${routes.size} rotas importadas")
        }

        val stopsFile = GtfsDownloader.download(context, "${prefix}_stops", onProgress)
        if (stopsFile != null) {
            onProgress("Importando paradas...")
            val stops = processStops(stopsFile, source)
            db.stopDao().deleteBySource(source)
            db.stopDao().insertAll(stops)
            onProgress("${stops.size} paradas importadas")
        }

        onProgress("Importacao concluida!")
    }

    fun getAllRoutes(source: String) = db.routeDao().getRoutesBySource(source)
    fun searchRoutes(query: String, source: String) = db.routeDao().searchRoutes(query, source)
}
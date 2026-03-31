package com.rafabs.sp4u.ui.aqi

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.rafabs.sp4u.R
import com.rafabs.sp4u.data.remote.Secrets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.net.URL
import com.rafabs.sp4u.data.remote.WaqiResponse
import com.rafabs.sp4u.data.remote.AqiData

// Modelo simplificado para os resultados do mapa
data class StationResult(
    val name: String,
    val aqi: String,
    val lat: Double,
    val lon: Double
)

class AqiFragment : Fragment(R.layout.fragment_aqi) {

    private val waqiToken = Secrets.WAQI_TOKEN
    private lateinit var mapView: MapView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Corrigido: Usando a forma moderna de carregar as configurações do OSMDroid
        // Substitui o PreferenceManager depreciado
        val sharedPrefs = requireContext().getSharedPreferences("osmdroid", Context.MODE_PRIVATE)
        Configuration.getInstance().load(requireContext(), sharedPrefs)
        Configuration.getInstance().userAgentValue = "Sampa4u-App"

        mapView = view.findViewById(R.id.mapView)
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)

        // Centralizar em São Paulo (Praça da Sé)
        val spCenter = GeoPoint(-23.5505, -46.6333)
        mapView.controller.setZoom(12.0)
        mapView.controller.setCenter(spCenter)

        carregarEstacoes(view)
    }

    private fun carregarEstacoes(view: View) {
        lifecycleScope.launch {
            try {
                val estacoes = buscarEstacoesPorArea()
                // Garantir que a renderização dos marcadores ocorra na Main Thread
                withContext(Dispatchers.Main) {
                    estacoes.forEach { estacao ->
                        adicionarMarcador(estacao, view)
                    }
                }
            } catch (ex: Exception) {
                Log.e("AQI_DEBUG", "Erro ao carregar estações: ${ex.message}")
            }
        }
    }

    private suspend fun buscarEstacoesPorArea(): List<StationResult> =
        withContext(Dispatchers.IO) {
            try {
                val url = "https://api.waqi.info/map/bounds/?latlng=-24.0,-47.0,-23.0,-46.0&token=$waqiToken"
                val response = URL(url).readText()
                val json = Gson().fromJson(response, JsonObject::class.java)

                if (json.get("status").asString == "ok") {
                    val data = json.getAsJsonArray("data")
                    data.mapNotNull { item ->
                        try {
                            val obj = item.asJsonObject

                            // CORREÇÃO AQUI: No endpoint de mapa, 'station' é um objeto que contém 'name'
                            val stationObj = obj.getAsJsonObject("station")
                            val stationName = stationObj.get("name").asString

                            val latitude = obj.get("lat").asDouble
                            val longitude = obj.get("lon").asDouble
                            val aqiValue = obj.get("aqi").asString

                            StationResult(
                                name = stationName,
                                aqi  = aqiValue,
                                lat  = latitude,
                                lon  = longitude
                            )
                        } catch (ex: Exception) {
                            // Log detalhado para sabermos exatamente o que falhou no JSON
                            Log.e("AQI_DEBUG", "Erro no item JSON: ${ex.message}")
                            null
                        }
                    }
                } else emptyList()
            } catch (ex: Exception) {
                Log.e("AQI_DEBUG", "Erro na busca: ${ex.message}")
                emptyList()
            }
        }

    private fun adicionarMarcador(estacao: StationResult, view: View) {
        val aqi = estacao.aqi.toIntOrNull() ?: -1
        val (colorStr, statusText) = getAqiInfo(aqi)

        val marker = Marker(mapView)
        marker.position = GeoPoint(estacao.lat, estacao.lon)
        marker.title = estacao.name
        marker.snippet = if (aqi >= 0) "AQI: $aqi - $statusText" else "Dados indisponíveis"
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

        marker.icon = criarIconeColorido(colorStr)

        marker.setOnMarkerClickListener { _, _ ->
            exibirDetalhesEstacao(estacao, colorStr, statusText, aqi, view)
            true
        }

        mapView.overlays.add(marker)
        mapView.invalidate()
    }

    private fun exibirDetalhesEstacao(estacao: StationResult, colorStr: String, status: String, aqi: Int, view: View) {
        val card = view.findViewById<LinearLayout>(R.id.cardEstacao)
        val tvCity = view.findViewById<TextView>(R.id.tvCityName)
        val tvValue = view.findViewById<TextView>(R.id.tvAqiValue)
        val tvStatus = view.findViewById<TextView>(R.id.tvAqiStatus)

        card.visibility = View.VISIBLE
        tvCity.text = estacao.name
        tvValue.text = if (aqi >= 0) aqi.toString() else "--"
        tvValue.setTextColor(colorStr.toColorInt())
        tvStatus.text = status
    }

    private fun criarIconeColorido(colorStr: String): GradientDrawable {
        return GradientDrawable().apply {
            shape = GradientDrawable.OVAL
            setSize(40, 40)
            setColor(try { colorStr.toColorInt() } catch (ex: Exception) { Color.GRAY })
            setStroke(3, Color.WHITE)
        }
    }

    private fun getAqiInfo(aqi: Int): Pair<String, String> = when (aqi) {
        in 0..50   -> "#009966" to "Boa"
        in 51..100 -> "#ffde33" to "Moderada"
        in 101..150 -> "#ff9933" to "Insalubre para sensíveis"
        in 151..200 -> "#cc0033" to "Insalubre"
        in 201..300 -> "#660099" to "Muito Insalubre"
        in 301..Int.MAX_VALUE -> "#7e0023" to "Perigosa"
        else -> "#9e9e9e" to "Indisponível"
    }

    override fun onResume() {
        super.onResume()
        if (::mapView.isInitialized) mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        if (::mapView.isInitialized) mapView.onPause()
    }
}
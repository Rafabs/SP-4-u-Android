package com.rafabs.sp4u.ui.aqi

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.core.graphics.toColorInt
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.rafabs.sp4u.R
import com.rafabs.sp4u.data.remote.Secrets
import com.rafabs.sp4u.data.remote.WaqiApiService
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Modelos de dados internos para evitar "Unresolved reference"
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
    val name: String
)

class AqiFragment : Fragment(R.layout.fragment_aqi) {

    private val waqiToken = Secrets.WAQI_TOKEN

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("AQI_DEBUG", "Fragment AQI carregado!")

        // 1. Configuração do Retrofit local (já que não há repositório injetado)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.waqi.info/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(WaqiApiService::class.java)

        // 2. Chamada da API
        lifecycleScope.launch {
            try {
                // Usamos o 'waqiToken' definido no topo da classe
                val response = service.getAirQuality("sao-paulo", waqiToken)

                if (response.status == "ok" && response.data.isJsonObject) {
                    val aqiData = Gson().fromJson(response.data, AqiData::class.java)
                    updateUI(aqiData, view)
                } else {
                    val errorMsg = if (response.data.isJsonPrimitive) response.data.asString else "Erro na chave"
                    showError(errorMsg, view)
                }
            } catch (e: Exception) {
                Log.e("AQI_DEBUG", "Falha: ${e.message}")
                showError("Sem conexão", view)
            }
        }
    }

    private fun updateUI(data: AqiData, view: View) {
        val tvCity = view.findViewById<TextView>(R.id.tvCityName)
        val tvValue = view.findViewById<TextView>(R.id.tvAqiValue)
        val tvStatus = view.findViewById<TextView>(R.id.tvAqiStatus)

        tvCity.text = data.city.name
        tvValue.text = data.aqi.toString()

        val (colorStr, statusText) = when (data.aqi) {
            in 0..50 -> "#009966" to "Boa"
            in 51..100 -> "#ffde33" to "Moderada"
            in 101..150 -> "#ff9933" to "Insalubre para sensíveis"
            in 151..200 -> "#cc0033" to "Insalubre"
            in 201..300 -> "#660099" to "Muito Insalubre"
            else -> "#7e0023" to "Perigosa"
        }

        // Resolvendo o aviso do KTX e aplicando a cor
        tvValue.setTextColor(colorStr.toColorInt())
        tvStatus.text = statusText
    }

    private fun showError(msg: String, view: View) {
        val tvStatus = view.findViewById<TextView>(R.id.tvAqiStatus)
        tvStatus.text = "Erro: $msg"
    }
}
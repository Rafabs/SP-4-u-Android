package com.rafabs.sp4u.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafabs.sp4u.data.model.Linha
import com.rafabs.sp4u.data.model.Status // Certifique-se de importar o novo model
import com.rafabs.sp4u.data.repository.MetroRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val repository = MetroRepository()

    private val linhasBase = listOf(
        Linha("L01", "01 - Azul",      "#0455A1", "METRÔ"),
        Linha("L02", "02 - Verde",     "#007E5E", "METRÔ"),
        Linha("L03", "03 - Vermelha",  "#EE372F", "METRÔ"),
        Linha("L04", "04 - Amarela",   "#FFF000", "VIAQUATRO"),
        Linha("L05", "05 - Lilás",     "#9B3894", "VIAMOBILIDADE"),
        Linha("L06", "06 - Laranja",   "#000000", "LINHA UNI"),
        Linha("L07", "07 - Rubi",      "#CA016B", "TIC TRENS"),
        Linha("L08", "08 - Diamante",  "#97A098", "VIAMOBILIDADE"),
        Linha("L09", "09 - Esmeralda", "#01A9A7", "VIAMOBILIDADE"),
        Linha("L10", "10 - Turquesa",  "#049FC3", "CPTM"),
        Linha("L11", "11 - Coral",     "#F68368", "CPTM"),
        Linha("L12", "12 - Safira",    "#133C8D", "CPTM"),
        Linha("L13", "13 - Jade",      "#00B352", "CPTM"),
        Linha("L15", "15 - Prata",     "#C0C0C0", "METRÔ"),
        Linha("L17", "17 - Ouro",      "#000000", "METRÔ")
    )

    private val _linhas = MutableLiveData<List<Linha>>(linhasBase)
    val linhas: LiveData<List<Linha>> = _linhas

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _erro = MutableLiveData<String?>()
    val erro: LiveData<String?> = _erro

    init {
        fetchStatus()
        iniciarAutoRefresh()
    }

    fun fetchStatus() {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getStatusLinhas()
                .onSuccess { statusMap ->
                    _linhas.value = linhasBase.map { linha ->
                        val triple = statusMap[linha.codigo]

                        // Criamos o objeto Status conforme o novo Linha.kt
                        val novoStatus = Status(
                            situacao = triple?.first ?: "Sem informação",
                            classificacao = triple?.second ?: "desconhecido",
                            descricao = triple?.third ?: "",
                            atualizadoHa = "" // Pode deixar vazio ou mapear se o triple tiver 4 valores
                        )

                        // Agora o copy recebe apenas o campo 'status' que é o objeto Status
                        linha.copy(status = novoStatus)
                    }
                    _erro.value = null
                }
                .onFailure {
                    _erro.value = "Falha ao carregar status das linhas."
                }
            _isLoading.value = false
        }
    }

    private fun iniciarAutoRefresh() {
        viewModelScope.launch {
            while (true) {
                delay(6 * 60 * 1000L)
                fetchStatus()
            }
        }
    }
}
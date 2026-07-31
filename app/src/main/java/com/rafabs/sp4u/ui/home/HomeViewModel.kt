package com.rafabs.sp4u.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rafabs.sp4u.R
import com.rafabs.sp4u.data.model.Linha
import com.rafabs.sp4u.data.model.Status
import com.rafabs.sp4u.data.repository.MetroRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val repository = MetroRepository()

    private val linhasBase = listOf(
        Linha("L01", "01 - Azul", "#0455A1", "METRÔ", R.drawable.black_metro, R.drawable.white_metro),
        Linha("L02", "02 - Verde",     "#007E5E", "METRÔ", R.drawable.black_metro, R.drawable.white_metro),
        Linha("L03", "03 - Vermelha",  "#EE372F", "METRÔ", R.drawable.black_metro, R.drawable.white_metro),
        Linha("L04", "04 - Amarela",   "#FFF000", "MOTIVA", R.drawable.black_motiva, R.drawable.white_motiva),
        Linha("L05", "05 - Lilás",     "#9B3894", "MOTIVA", R.drawable.black_motiva, R.drawable.white_motiva),
        Linha("L06", "06 - Laranja",   "#FF6600", "LINHA UNI", R.drawable.black_linha_uni, R.drawable.white_linha_uni),
        Linha("L07", "07 - Rubi",      "#CA016B", "TIC TRENS", R.drawable.black_tic_trens, R.drawable.white_tic_trens),
        Linha("L08", "08 - Diamante",  "#97A098", "MOTIVA", R.drawable.black_motiva, R.drawable.white_motiva),
        Linha("L09", "09 - Esmeralda", "#01A9A7", "MOTIVA", R.drawable.black_motiva, R.drawable.white_motiva),
        Linha("L10", "10 - Turquesa",  "#049FC3", "CPTM", R.drawable.black_cptm, R.drawable.white_cptm),
        Linha("L11", "11 - Coral",     "#F68368", "TRIVIA", R.drawable.black_trivia, R.drawable.white_trivia),
        Linha("L12", "12 - Safira",    "#133C8D", "TRIVIA", R.drawable.black_trivia, R.drawable.white_trivia),
        Linha("L13", "13 - Jade",      "#00B352", "TRIVIA", R.drawable.black_trivia, R.drawable.white_trivia),
        Linha("L15", "15 - Prata",     "#C0C0C0", "METRÔ", R.drawable.black_metro, R.drawable.white_metro),
        Linha("L17", "17 - Ouro",      "#D48500", "METRÔ", R.drawable.black_metro, R.drawable.white_metro),
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

                        val novoStatus = Status(
                            situacao      = triple?.first  ?: "Sem informação",
                            classificacao = triple?.second ?: "desconhecido",
                            descricao     = "",
                            atualizadoHa  = triple?.third  ?: ""
                        )

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
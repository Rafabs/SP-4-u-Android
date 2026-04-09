package com.rafabs.sp4u.data.model

data class Linha(
    val codigo: String,
    val nome: String,
    val cor: String,
    val empresa: String,
    val status: String = "Carregando...",
    val classificacao: String = "desconhecido",
    val descricao: String = "",
    val atualizadoHa: String = ""
)
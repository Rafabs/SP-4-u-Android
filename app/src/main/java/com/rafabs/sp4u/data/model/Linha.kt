package com.rafabs.sp4u.data.model

data class Linha(
    val codigo: String,
    val nome: String,
    val cor: String,
    val empresa: String,
    val status: Status = Status("Carregando...", "desconhecido", "", "")
)

data class Status(
    val situacao: String,
    val classificacao: String,
    val descricao: String,
    val atualizadoHa: String
)
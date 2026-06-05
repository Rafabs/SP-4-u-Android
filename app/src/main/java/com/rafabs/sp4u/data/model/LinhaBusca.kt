package com.rafabs.sp4u.model

data class LinhaBusca(
    val cl: Int,    // Código identificador da linha
    val lt: String, // Letreiro numérico
    val ts: String, // Letreiro de origem
    val tp: String,  // Letreiro de destino
    val sl: Int
)
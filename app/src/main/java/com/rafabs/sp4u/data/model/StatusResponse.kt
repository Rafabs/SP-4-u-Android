package com.rafabs.sp4u.data.model

import com.google.gson.annotations.SerializedName

data class StatusResponse(
    val empresas: List<EmpresaDto>
)

data class EmpresaDto(
    val id: Int,
    val nome: String,
    val linhas: List<LinhaDto>
)

data class LinhaDto(
    val nome: String,
    val codigo: String,
    val status: StatusDto
)

data class StatusDto(
    val situacao: String,
    val descricao: String,
    val classificacao: String,
    @SerializedName("operacao_normal") val operacaoNormal: Boolean,
    @SerializedName("atualizado_em")   val atualizadoEm: String,
    @SerializedName("atualizado_ha")   val atualizadoHa: String
)
package com.rafabs.sp4u.data.repository

import com.rafabs.sp4u.data.model.Linha
import com.rafabs.sp4u.data.remote.RetrofitClient
import retrofit2.HttpException

class MetroRepository {

    // Mesmo código da API → código local (L01, L02...)
    private val codigoApiParaLocal = mapOf(
        "1"  to "L01",
        "2"  to "L02",
        "3"  to "L03",
        "4"  to "L04",
        "5"  to "L05",
        "7"  to "L07",
        "8"  to "L08",
        "9"  to "L09",
        "10" to "L10",
        "11" to "L11",
        "12" to "L12",
        "13" to "L13",
        "15" to "L15"
    )

    suspend fun getStatusLinhas(): Result<Map<String, Triple<String, String, String>>> {
        return try {
            val response = RetrofitClient.api.getStatus()
            val statusMap = response.empresas
                .flatMap { it.linhas }
                .mapNotNull { dto ->
                    val codigoLocal = codigoApiParaLocal[dto.codigo] ?: return@mapNotNull null
                    codigoLocal to Triple(
                        dto.status.situacao,
                        dto.status.classificacao,
                        dto.status.descricao
                    )
                }
                .toMap()
            Result.success(statusMap)
        } catch (e: HttpException) {
            if (e.code() == 429)
                Result.failure(Exception("Limite de requisicoes atingido. Tente em alguns minutos."))
            else
                Result.failure(Exception("Erro ao consultar API: ${e.code()}"))
        } catch (e: Exception) {
            Result.failure(Exception("Falha de conexao. Verifique sua internet."))
        }
    }
}
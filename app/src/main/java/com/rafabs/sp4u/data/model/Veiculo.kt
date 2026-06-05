package com.rafabs.sp4u.model

import com.google.gson.annotations.SerializedName

data class Veiculo(
    @SerializedName("p") val p: String,  // Prefixo do veículo
    @SerializedName("a") val a: Boolean, // Indicador de acessibilidade
    @SerializedName("py") val py: Double, // Latitude
    @SerializedName("px") val px: Double, // Longitude
    @SerializedName("ta") val ta: String  // Horário da transmissão
)
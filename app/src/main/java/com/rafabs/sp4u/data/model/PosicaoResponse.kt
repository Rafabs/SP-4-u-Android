package com.rafabs.sp4u.model
import com.google.gson.annotations.SerializedName
data class PosicaoResponse(
    @SerializedName("hr") val hr: String,
    @SerializedName("vs") val vs: List<Veiculo>?
)

data class LinhaVeiculos(
    @SerializedName("c") val c: String,
    @SerializedName("vs") val vs: List<Veiculo>
)
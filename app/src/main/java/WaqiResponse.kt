import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName

data class WaqiResponse(
    @SerializedName("status") val status: String,
    @SerializedName("data") val data: JsonElement // Captura tanto o Objeto quanto a String de erro
) {
    // Verifica se o retorno é o objeto esperado de qualidade do ar
    fun isSuccess(): Boolean = status == "ok" && data.isJsonObject
}

data class AqiData(
    @SerializedName("aqi") val aqi: Int,
    @SerializedName("city") val city: City,
    @SerializedName("dominentpol") val dominentpol: String
)

data class City(
    @SerializedName("name") val name: String
)
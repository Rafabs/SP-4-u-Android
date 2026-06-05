package network

import com.rafabs.sp4u.model.PosicaoResponse
import com.rafabs.sp4u.model.SptransLinha
import com.rafabs.sp4u.model.Parada
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("Login/Autenticar")
    fun autenticar(@Query("token") token: String): Call<Boolean>

    @GET("Linha/Buscar")
    fun buscarLinhas(@Query("termosBusca") termos: String): Call<List<SptransLinha>>

    @GET("Posicao/Linha")
    fun getPosicoes(@Query("codigoLinha") codigoLinha: Int): Call<PosicaoResponse>

    @GET("Parada/BuscarParadasPorLinha")
    fun buscarParadasPorLinha(@Query("codigoLinha") codigoLinha: Int): Call<List<Parada>>
}
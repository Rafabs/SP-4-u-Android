package com.rafabs.sp4u.ui.olhovivo

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.rafabs.sp4u.R
import com.rafabs.sp4u.databinding.FragmentOlhovivoBinding
import com.rafabs.sp4u.network.RetrofitClient
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import androidx.core.content.res.ResourcesCompat
import androidx.annotation.DrawableRes
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.core.graphics.ColorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.rafabs.sp4u.data.remote.Secrets
import androidx.core.graphics.toColorInt
import com.rafabs.sp4u.data.local.AppDatabase
import org.osmdroid.views.overlay.Polyline
import androidx.core.graphics.toColorInt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.rafabs.sp4u.model.PosicaoResponse
import com.rafabs.sp4u.model.SptransLinha
import com.rafabs.sp4u.model.Veiculo
import com.rafabs.sp4u.model.LinhaBusca
import com.rafabs.sp4u.model.Parada
class OlhoVivoFragment : Fragment(R.layout.fragment_olhovivo) {

    private val olhovivoToken = Secrets.OLHOVIVO_TOKEN

    private var _binding: FragmentOlhovivoBinding? = null
    private val binding get() = _binding!!
    private lateinit var mapView: MapView
    private val handler = android.os.Handler(android.os.Looper.getMainLooper())
    private var runnable: Runnable? = null
    private var codigoLinhaSelecionada: Int? = null
    private val INTERVALO_MS = 7000L // 7 segundos

    private var polylinhaAtual: Polyline? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentOlhovivoBinding.bind(view)

        val sharedPrefs = requireContext().getSharedPreferences("osmdroid", Context.MODE_PRIVATE)
        Configuration.getInstance().load(requireContext(), sharedPrefs)
        Configuration.getInstance().userAgentValue = "Sampa4u-App"

        mapView = binding.mapView
        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)

        val spCenter = GeoPoint(-23.5505, -46.6333)
        mapView.controller.setZoom(12.0)
        mapView.controller.setCenter(spCenter)

        // configurarBusca() ← removido daqui
        autenticarSptrans() // configurarBusca() agora é chamado dentro após sucesso
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1001)
        } else {
            ativarLocalizacao()
        }
    }

    private var locationOverlay: MyLocationNewOverlay? = null

    private fun ativarLocalizacao() {
        locationOverlay = MyLocationNewOverlay(GpsMyLocationProvider(requireContext()), mapView)
        locationOverlay!!.enableMyLocation()
        mapView.overlays.add(locationOverlay)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 1001 && grantResults.firstOrNull() == PackageManager.PERMISSION_GRANTED) {
            ativarLocalizacao()
        }
    }

    private var primeiraAtualizacao = true

    private fun iniciarAtualizacaoTempReal(linha: LinhaBusca) {
        linhaSelecionada = linha
        codigoLinhaSelecionada = linha.cl
        primeiraAtualizacao = true
        prefixoSelecionado = null
        pararAtualizacaoTempReal()
        desenharShapeDaLinha(linha.lt, linha.sl)
        runnable = Runnable {
            buscarPosicaoOnibus(linha.cl)
            handler.postDelayed(runnable!!, INTERVALO_MS)
        }
        handler.post(runnable!!)
    }
    private fun pararAtualizacaoTempReal() {
        runnable?.let { handler.removeCallbacks(it) }
    }
    private fun efetuarBuscaDeLinha(termo: String) {
        RetrofitClient.instance.buscarLinhas(termo).enqueue(object : Callback<List<SptransLinha>> {
            override fun onResponse(call: Call<List<SptransLinha>>, response: Response<List<SptransLinha>>) {
                if (!response.isSuccessful) {
                    Toast.makeText(context, "Linha não encontrada", Toast.LENGTH_SHORT).show()
                    return
                }
                val linhas = response.body()
                if (!linhas.isNullOrEmpty()) {
                    // Agrupa por cl (código único por sentido) para evitar duplicatas
                    val linhasBusca: List<LinhaBusca> = linhas
                        .distinctBy { it.cl }
                        .map { LinhaBusca(it.cl, it.lt, it.ts, it.tp, it.sl) }

                    val adapter = LinhaAdapter(linhasBusca) { linhaSelecionada ->
                        iniciarAtualizacaoTempReal(linhaSelecionada)
                    }
                    binding.recyclerViewLines.adapter = adapter
                    binding.recyclerViewLines.visibility = View.VISIBLE
                    binding.emptyStateLayout.visibility = View.GONE
                } else {
                    Toast.makeText(context, "Nenhuma linha encontrada", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<List<SptransLinha>>, t: Throwable) {
                Log.e("OLHO_VIVO", "Erro na rede: ${t.message}")
            }
        })
    }

    private fun autenticarSptrans() {
        RetrofitClient.instance.autenticar(olhovivoToken).enqueue(object : Callback<Boolean> {
            override fun onResponse(call: Call<Boolean>, response: Response<Boolean>) {
                if (response.isSuccessful && response.body() == true) {
                    Log.d("OLHO_VIVO", "Autenticado com sucesso")
                    configurarBusca() // ← mova para cá
                } else {
                    Toast.makeText(context, "Falha na autenticação", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onFailure(call: Call<Boolean>, t: Throwable) {
                Log.e("OLHO_VIVO", "Erro na autenticação: ${t.message}")
            }
        })
    }

    private fun desenharShapeDaLinha(letreiro: String, sentido: Int) {
        val db = AppDatabase.getDatabase(requireContext())
        CoroutineScope(Dispatchers.IO).launch {

            val sample = db.routeDao().getSampleAll()
            Log.d("SHAPE", "Sample routes: ${sample.map { "${it.shortName} | ${it.longName}" }}")

            val route = db.routeDao().getRouteByLetreiro(letreiro, "SPTRANS")
            Log.d("SHAPE", "letreiro=$letreiro sentido=$sentido route=$route")
            if (route == null) return@launch

            val directionId = if (sentido == 1) 0 else 1
            Log.d("SHAPE", "routeId=${route.routeId} directionId=$directionId")

            val shapeId = db.tripDao().getShapeIdForRouteAndDirection(route.routeId, directionId)
            Log.d("SHAPE", "shapeId=$shapeId")
            if (shapeId == null) {
                // Tenta sem filtro de sentido para ver se tem algum shape
                val shapeIdSemFiltro = db.tripDao().getShapeIdForRoute(route.routeId)
                Log.d("SHAPE", "shapeId SEM filtro=$shapeIdSemFiltro")
                return@launch
            }

            val points = db.shapeDao().getShapePoints(shapeId)
            Log.d("SHAPE", "points=${points.size}")
            if (points.isEmpty()) return@launch

            val geoPoints = points.map { GeoPoint(it.shapePtLat, it.shapePtLon) }
            val color = try {
                val hex = route.color ?: "0000FF"
                if (hex.startsWith("#")) hex.toColorInt() else "#$hex".toColorInt()
            } catch (e: Exception) {
                Color.BLUE
            }

            activity?.runOnUiThread {
                polylinhaAtual?.let { mapView.overlays.remove(it) }
                val polyline = Polyline().apply {
                    setPoints(geoPoints)
                    outlinePaint.color = color
                    outlinePaint.strokeWidth = 8f
                }
                polylinhaAtual = polyline
                mapView.overlays.add(0, polyline)
                mapView.invalidate()
            }
        }
    }

    private fun buscarPosicaoOnibus(codigoLinha: Int) {
        RetrofitClient.instance.getPosicoes(codigoLinha).enqueue(object : Callback<PosicaoResponse> {
            override fun onResponse(call: Call<PosicaoResponse>, response: Response<PosicaoResponse>) {
                if (!response.isSuccessful) return
                val veiculos = response.body()?.vs ?: emptyList()

                val letreiro = linhaSelecionada?.lt ?: ""
                val db = AppDatabase.getDatabase(requireContext())

                CoroutineScope(Dispatchers.IO).launch {
                    val route = db.routeDao().getRouteByLetreiro(letreiro, "SPTRANS")
                    val corHex = route?.color
                    val corLinha = try {
                        if (corHex != null) "#$corHex".toColorInt()
                        else Color.parseColor("#1A73E8")
                    } catch (e: Exception) {
                        Color.parseColor("#1A73E8")
                    }

                    activity?.runOnUiThread {
                        desenharVeiculosNoMapa(veiculos, corLinha)
                        buscarParadasEDesenhar(codigoLinha)
                    }
                }
            }
            override fun onFailure(call: Call<PosicaoResponse>, t: Throwable) {
                Log.e("OLHO_VIVO", "Erro ao buscar posições: ${t.message}")
            }
        })
    }

    private fun criarIconeOnibusColorido(corLinha: Int): Drawable {
        val original = BitmapFactory.decodeResource(resources, R.drawable.veiculo)
        val bitmap = original.copy(Bitmap.Config.ARGB_8888, true)

        val corAlvo = Color.parseColor("#D32F2F") // vermelho original do seu PNG
        val tolerancia = 80 // quanto de variação aceitar (0-255)

        for (x in 0 until bitmap.width) {
            for (y in 0 until bitmap.height) {
                val pixel = bitmap.getPixel(x, y)
                val alpha = Color.alpha(pixel)
                if (alpha < 20) continue // ignora pixels transparentes

                if (isSimilar(pixel, corAlvo, tolerancia)) {
                    // Mantém o alpha original, troca o RGB pela cor da linha
                    val novaCorComAlpha = ColorUtils.setAlphaComponent(corLinha, alpha)
                    bitmap.setPixel(x, y, novaCorComAlpha)
                }
            }
        }

        return BitmapDrawable(resources, bitmap)
    }

    private fun isSimilar(c1: Int, c2: Int, tolerancia: Int): Boolean {
        return Math.abs(Color.red(c1) - Color.red(c2)) < tolerancia &&
                Math.abs(Color.green(c1) - Color.green(c2)) < tolerancia &&
                Math.abs(Color.blue(c1) - Color.blue(c2)) < tolerancia
    }

    private var linhaSelecionada: LinhaBusca? = null
    private var prefixoSelecionado: String? = null

    private fun desenharVeiculosNoMapa(veiculos: List<Veiculo>, corLinha: Int) {
        activity?.runOnUiThread {
            // Em vez de limpar tudo, filtramos e removemos apenas os marcadores antigos
            // para não interferir com a polilinha que roda em outra thread
            val marcadoresAntigos = mapView.overlays.filterIsInstance<Marker>()
            mapView.overlays.removeAll(marcadoresAntigos)

            // Garante que a polilinha e a localização continuem no mapa
            polylinhaAtual?.let { if (!mapView.overlays.contains(it)) mapView.overlays.add(0, it) }
            locationOverlay?.let { if (!mapView.overlays.contains(it)) mapView.overlays.add(it) }

            var markerParaReabrir: Marker? = null

            veiculos.forEach { veiculo ->
                val destino = linhaSelecionada?.let { if (it.sl == 1) it.tp else it.ts } ?: ""
                val marker = Marker(mapView).apply {
                    position = GeoPoint(veiculo.py, veiculo.px)
                    title = "Linha ${linhaSelecionada?.lt ?: ""} • Veículo ${veiculo.p}\n$destino"
                    snippet = if (veiculo.a) "♿ Acessível: Sim" else "🚫 Acessível: Não"
                    icon = getIconeOnibus(corLinha)
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                    setOnMarkerClickListener { m, _ ->
                        prefixoSelecionado = veiculo.p
                        m.showInfoWindow()
                        true
                    }
                }
                if (veiculo.p == prefixoSelecionado) markerParaReabrir = marker
                mapView.overlays.add(marker)
            }

            if (primeiraAtualizacao && veiculos.isNotEmpty()) {
                mapView.controller.animateTo(GeoPoint(veiculos[0].py, veiculos[0].px))
                mapView.controller.setZoom(14.0)
                primeiraAtualizacao = false
            }

            markerParaReabrir?.showInfoWindow()
            mapView.invalidate()
        }
    }

    private val cacheIcones = mutableMapOf<Int, Drawable>()

    private fun getIconeOnibus(corLinha: Int): Drawable {
        return cacheIcones.getOrPut(corLinha) {
            criarIconeOnibusColorido(corLinha)
        }
    }

    private fun getDrawableIcon(@DrawableRes resId: Int): Drawable {
        return ResourcesCompat.getDrawable(resources, resId, null)!!
    }

    private fun configurarBusca() {
        binding.recyclerViewLines.layoutManager = LinearLayoutManager(context)

        var ultimaBusca = 0L

        fun dispararBusca() {
            val agora = System.currentTimeMillis()
            if (agora - ultimaBusca < 1000) return
            ultimaBusca = agora

            // Esconde o teclado
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager
            imm.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)

            val termo = binding.searchEditText.text.toString().trim()
            if (termo.length >= 3) {
                efetuarBuscaDeLinha(termo)
            } else {
                Toast.makeText(context, "Digite pelo menos 3 caracteres", Toast.LENGTH_SHORT).show()
            }
        }

        binding.searchButton.setOnClickListener {
            dispararBusca()
        }

        // Corrigido o pacote para 'inputmethod'
        binding.searchEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH ||
                actionId == android.view.inputmethod.EditorInfo.IME_ACTION_DONE ||
                actionId == android.view.inputmethod.EditorInfo.IME_NULL) {
                dispararBusca()
                true
            } else {
                false
            }
        }
    }

    private fun buscarParadasEDesenhar(codigoLinha: Int) {
        RetrofitClient.instance.buscarParadasPorLinha(codigoLinha).enqueue(object : Callback<List<Parada>> {
            override fun onResponse(call: Call<List<Parada>>, response: Response<List<Parada>>) {
                val paradas = response.body() ?: emptyList()
                desenharParadasNoMapa(paradas)
            }
            override fun onFailure(call: Call<List<Parada>>, t: Throwable) {
                Log.e("OLHO_VIVO", "Erro ao buscar paradas: ${t.message}")
            }
        })
    }

    private fun desenharParadasNoMapa(paradas: List<Parada>) {
        activity?.runOnUiThread {
            // Garante que a polilinha permaneça embaixo de tudo
            polylinhaAtual?.let { if (!mapView.overlays.contains(it)) mapView.overlays.add(0, it) }

            paradas.forEach { parada ->
                val marker = Marker(mapView).apply {
                    position = GeoPoint(parada.py, parada.px)
                    title = parada.np
                    snippet = parada.ed
                    icon = getDrawableIcon(R.drawable.parada_onibus)
                    setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_CENTER)
                }
                mapView.overlays.add(marker)
            }
            mapView.invalidate()
            Log.d("SHAPE", "Paradas desenhadas na tela: ${paradas.size}")
        }
    }

    // Ciclo de vida obrigatório para o mapa não crashar
    override fun onResume() {
        super.onResume()
        if (::mapView.isInitialized) mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        if (::mapView.isInitialized) mapView.onPause()
    }

    override fun onDestroyView() {
        pararAtualizacaoTempReal()
        super.onDestroyView()
        if (::mapView.isInitialized) mapView.onDetach()
        _binding = null
    }
}
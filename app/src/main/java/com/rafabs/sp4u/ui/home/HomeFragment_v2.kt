package com.rafabs.sp4u.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.rafabs.sp4u.data.model.Linha
import com.rafabs.sp4u.data.remote.GtfsVersionChecker
import com.rafabs.sp4u.data.repository.GtfsRepository
import com.rafabs.sp4u.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val linhas = listOf(
        Linha("L01", "01 - Azul",      "#0455A1", "METRÔ"),
        Linha("L02", "02 - Verde",      "#007E5E", "METRÔ"),
        Linha("L03", "03 - Vermelha",   "#EE372F", "METRÔ"),
        Linha("L04", "04 - Amarela",    "#FFF000", "VIAQUATRO"),
        Linha("L05", "05 - Lilás",      "#9B3894", "VIAMOBILIDADE"),
        Linha("L06", "06 - Laranja",    "#000000", "LINHA UNI"),
        Linha("L07", "07 - Rubi",       "#CA016B", "TIC TRENS"),
        Linha("L08", "08 - Diamante",   "#97A098", "VIAMOBILIDADE"),
        Linha("L09", "09 - Esmeralda",  "#01A9A7", "VIAMOBILIDADE"),
        Linha("L10", "10 - Turquesa",   "#049FC3", "CPTM"),
        Linha("L11", "11 - Coral",      "#F68368", "CPTM"),
        Linha("L12", "12 - Safira",     "#133C8D", "CPTM"),
        Linha("L13", "13 - Jade",       "#00B352", "CPTM"),
        Linha("L15", "15 - Prata",      "#C0C0C0", "METRÔ"),
        Linha("L17", "17 - Ouro",       "#000000", "METRÔ")
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerLinhas.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerLinhas.adapter = MetroAdapter(linhas)
        binding.recyclerLinhas.isNestedScrollingEnabled = false

        verificarAtualizacoes()
    }

    private fun verificarAtualizacoes() {
        val prefs = requireContext()
            .getSharedPreferences("sp4u_prefs", Context.MODE_PRIVATE)

        // Verificar SPTrans
        lifecycleScope.launch {
            val localSptrans  = prefs.getString("gtfs_sptrans_version", null)
            val remoteSptrans = GtfsVersionChecker.getRemoteVersion("SPTRANS")
            if (remoteSptrans != null && remoteSptrans != localSptrans) {
                exibirBanner(
                    msg = if (localSptrans == null)
                        "Dados SPTrans não importados. Clique para baixar."
                    else
                        "Nova versão SPTrans disponivel ($remoteSptrans). Clique para atualizar.",
                    onClick = {
                        lifecycleScope.launch {
                            _binding?.bannerAtualizacao?.isEnabled = false
                            val repo = GtfsRepository(requireContext())
                            repo.importGtfs("SPTRANS") { status: String ->
                                requireActivity().runOnUiThread {
                                    _binding?.tvBannerMsg?.text = status
                                }
                            }
                            prefs.edit { putString("gtfs_sptrans_version", remoteSptrans) }
                            requireActivity().runOnUiThread {
                                _binding?.bannerAtualizacao?.visibility = View.GONE
                            }
                            // Apás SPTrans, verificar EMTU
                            verificarEmtu(prefs)
                        }
                    }
                )
                return@launch
            }
            // Se SPTrans ok, verificar EMTU
            verificarEmtu(prefs)
        }
    }

    private suspend fun verificarEmtu(prefs: android.content.SharedPreferences) {
        val localEmtu  = prefs.getString("gtfs_emtu_version", null)
        val remoteEmtu = GtfsVersionChecker.getRemoteVersion("EMTU")
        if (remoteEmtu != null && remoteEmtu != localEmtu) {
            requireActivity().runOnUiThread {
                exibirBanner(
                    msg = if (localEmtu == null)
                        "Dados ARTESP/EMTU não importados. Clique para baixar."
                    else
                        "Nova versão ARTESP/EMTU disponivel ($remoteEmtu). Clique para atualizar.",
                    onClick = {
                        lifecycleScope.launch {
                            _binding?.bannerAtualizacao?.isEnabled = false
                            val repo = GtfsRepository(requireContext())
                            repo.importGtfs("EMTU") { status: String ->
                                requireActivity().runOnUiThread {
                                    _binding?.tvBannerMsg?.text = status
                                }
                            }
                            prefs.edit { putString("gtfs_emtu_version", remoteEmtu) }
                            requireActivity().runOnUiThread {
                                _binding?.bannerAtualizacao?.visibility = View.GONE
                            }
                        }
                    }
                )
            }
        }
    }

    private fun exibirBanner(msg: String, onClick: () -> Unit) {
        _binding?.bannerAtualizacao?.visibility = View.VISIBLE
        _binding?.tvBannerMsg?.text = msg
        _binding?.bannerAtualizacao?.setOnClickListener { onClick() }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
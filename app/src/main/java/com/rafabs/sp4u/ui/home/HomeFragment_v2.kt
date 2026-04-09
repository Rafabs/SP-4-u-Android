package com.rafabs.sp4u.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rafabs.sp4u.data.model.Linha
import com.rafabs.sp4u.data.remote.GtfsVersionChecker
import com.rafabs.sp4u.data.repository.GtfsRepository
import com.rafabs.sp4u.databinding.FragmentHomeBinding
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: MetroAdapter

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

        adapter = MetroAdapter { linha ->
            mostrarDialogDescricao(linha)
        }
        binding.recyclerLinhas.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerLinhas.adapter = adapter
        binding.recyclerLinhas.isNestedScrollingEnabled = false

        viewModel.linhas.observe(viewLifecycleOwner) { linhas ->
            adapter.submitList(linhas)
        }

        viewModel.erro.observe(viewLifecycleOwner) { erro ->
            erro?.let { Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show() }
        }

        verificarAtualizacoes()
    }

    private fun mostrarDialogDescricao(linha: Linha) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Linha " + linha.nome)
            .setMessage(linha.descricao)
            .setPositiveButton("Fechar") { dialog, _ -> dialog.dismiss() }
            .show()
    }

    private fun verificarAtualizacoes() {
        val prefs = requireContext()
            .getSharedPreferences("sp4u_prefs", Context.MODE_PRIVATE)

        lifecycleScope.launch {
            val localSptrans  = prefs.getString("gtfs_sptrans_version", null)
            val remoteSptrans = GtfsVersionChecker.getRemoteVersion("SPTRANS")
            if (remoteSptrans != null && remoteSptrans != localSptrans) {
                exibirBanner(
                    msg = if (localSptrans == null)
                        "Dados SPTrans nao importados. Clique para baixar."
                    else
                        "Nova versao SPTrans disponivel ($remoteSptrans). Clique para atualizar.",
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
                            verificarEmtu(prefs)
                        }
                    }
                )
                return@launch
            }
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
                        "Dados ARTESP/EMTU nao importados. Clique para baixar."
                    else
                        "Nova versao ARTESP/EMTU disponivel ($remoteEmtu). Clique para atualizar.",
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
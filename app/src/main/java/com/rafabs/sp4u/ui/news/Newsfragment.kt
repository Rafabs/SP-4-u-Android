package com.rafabs.sp4u.ui.news

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import com.rafabs.sp4u.databinding.FragmentNewsBinding

class NewsFragment : Fragment() {

    private var _binding: FragmentNewsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNewsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configura os cliques para abrir os canais da ARTESP
        binding.btnArtespMetro.setOnClickListener {
            openUrl("https://ccm.artesp.sp.gov.br/metroferroviario/noticias")
        }

        binding.btnArtespRodovias.setOnClickListener {
            openUrl("https://ccm.artesp.sp.gov.br/rodovias/noticias")
        }

        binding.btnArtespAeroportos.setOnClickListener {
            openUrl("https://ccm.artesp.sp.gov.br/aeroportos/noticias")
        }
    }

    private fun openUrl(url: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW, url.toUri())
            startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
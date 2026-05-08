package com.rafabs.sp4u.ui.maprede

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.rafabs.sp4u.databinding.FragmentMapredeBinding // Nome atualizado pelo Android Studio

class MapRedeFragment : Fragment() {

    private var _binding: FragmentMapredeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Agora inflamos usando o novo nome do XML
        _binding = FragmentMapredeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // URL do seu mapa no Figma
        val figmaEmbedUrl = "https://www.figma.com/embed?embed_host=share&url=https%3A%2F%2Fwww.figma.com%2Fdesign%2FdV6TZhnCs7CmzCyIP2aPA8%2FMapa-do-Transporte-Metropolitano%3Fnode-id%3D0-1"

        // Configuração da WebView
        binding.webViewFigma.apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            webViewClient = WebViewClient()
            loadUrl(figmaEmbedUrl)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
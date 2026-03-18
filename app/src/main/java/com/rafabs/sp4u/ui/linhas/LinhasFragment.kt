package com.rafabs.sp4u.ui.linhas

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rafabs.sp4u.databinding.FragmentLinhasBinding

class LinhasFragment : Fragment() {
    private var _binding: FragmentLinhasBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentLinhasBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}

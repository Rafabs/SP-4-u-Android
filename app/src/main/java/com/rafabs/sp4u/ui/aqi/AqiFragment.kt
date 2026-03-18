package com.rafabs.sp4u.ui.aqi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.rafabs.sp4u.databinding.FragmentAqiBinding

class AqiFragment : Fragment() {
    private var _binding: FragmentAqiBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAqiBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onDestroyView() { super.onDestroyView(); _binding = null }
}

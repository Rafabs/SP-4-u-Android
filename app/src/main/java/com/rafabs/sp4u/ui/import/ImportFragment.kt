package com.rafabs.sp4u.ui.import_gtfs

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.rafabs.sp4u.R
import com.rafabs.sp4u.data.repository.GtfsRepository
import com.rafabs.sp4u.databinding.FragmentImportBinding
import kotlinx.coroutines.launch

class ImportFragment : Fragment() {

    private var _binding: FragmentImportBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentImportBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnImportar.setOnClickListener {
            binding.btnImportar.isEnabled = false
            binding.progressBar.visibility = View.VISIBLE
            binding.tvStatus.visibility = View.VISIBLE

            lifecycleScope.launch {
                val repo = GtfsRepository(requireContext())
                repo.importGtfs("SPTRANS") { status ->
                    requireActivity().runOnUiThread {
                        binding.tvStatus.text = status
                    }
                }
                requireContext()
                    .getSharedPreferences("sp4u_prefs", Context.MODE_PRIVATE)
                    .edit()
                    .putBoolean("gtfs_imported", true)
                    .apply()
                requireActivity().runOnUiThread {
                    findNavController().navigate(R.id.homeFragment)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
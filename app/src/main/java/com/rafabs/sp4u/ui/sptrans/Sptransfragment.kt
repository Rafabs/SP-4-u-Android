package com.rafabs.sp4u.ui.sptrans

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.rafabs.sp4u.data.local.AppDatabase
import com.rafabs.sp4u.data.local.entity.Route
import com.rafabs.sp4u.databinding.FragmentSptransBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.rafabs.sp4u.databinding.ItemLinhaSptransBinding

class SptransFragment : Fragment() {

    private var _binding: FragmentSptransBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: SptransAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSptransBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = SptransAdapter(emptyList())
        binding.recyclerLinhas.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerLinhas.adapter = adapter

        val db = AppDatabase.getDatabase(requireContext())

        lifecycleScope.launch {
            db.routeDao().getRoutesBySource("SPTRANS").collectLatest { routes ->
                adapter.updateList(routes)
                binding.tvEmpty.visibility =
                    if (routes.isEmpty()) View.VISIBLE else View.GONE
            }
        }

        binding.etBusca.addTextChangedListener { text ->
            val query = text.toString().trim()
            lifecycleScope.launch {
                if (query.isEmpty()) {
                    db.routeDao().getRoutesBySource("SPTRANS").collectLatest {
                        adapter.updateList(it)
                    }
                } else {
                    db.routeDao().searchRoutes(query, "SPTRANS").collectLatest {
                        adapter.updateList(it)
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
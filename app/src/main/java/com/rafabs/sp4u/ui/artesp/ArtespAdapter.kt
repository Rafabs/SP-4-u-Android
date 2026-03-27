package com.rafabs.sp4u.ui.artesp

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rafabs.sp4u.data.local.entity.Route
import com.rafabs.sp4u.databinding.ItemLinhaSptransBinding

class ArtespAdapter(private var linhas: List<Route>) :
    RecyclerView.Adapter<ArtespAdapter.ViewHolder>() {

    fun updateList(novaLista: List<Route>) {
        linhas = novaLista
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ItemLinhaSptransBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(route: Route) {
            binding.tvNumero.text = route.shortName
            binding.tvNome.text = route.longName.ifEmpty { route.shortName }

            try {
                binding.tvNumero.setBackgroundColor(Color.parseColor(route.color))
            } catch (e: Exception) {
                binding.tvNumero.setBackgroundColor(Color.GRAY)
            }

            try {
                val bg = Color.parseColor(route.color)
                val luminance = (0.299 * Color.red(bg) +
                        0.587 * Color.green(bg) +
                        0.114 * Color.blue(bg)) / 255
                binding.tvNumero.setTextColor(
                    if (luminance > 0.5) Color.BLACK else Color.WHITE
                )
            } catch (e: Exception) {
                binding.tvNumero.setTextColor(Color.WHITE)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLinhaSptransBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(linhas[position])
    }

    override fun getItemCount() = linhas.size
}
package com.rafabs.sp4u.ui.home

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rafabs.sp4u.R
import com.rafabs.sp4u.data.model.Linha
import com.rafabs.sp4u.databinding.ItemLinhaMetroBinding

class MetroAdapter(
    private val onItemClick: (Linha) -> Unit
) : ListAdapter<Linha, MetroAdapter.ViewHolder>(DiffCallback()) {

    class ViewHolder(private val binding: ItemLinhaMetroBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(linha: Linha, onItemClick: (Linha) -> Unit) {
            binding.tvNome.text = linha.nome
            binding.tvHex.text = linha.cor
            binding.tvEmpresa.text = linha.empresa

            val circle = GradientDrawable()
            circle.shape = GradientDrawable.OVAL
            try {
                circle.setColor(Color.parseColor(linha.cor))
            } catch (e: Exception) {
                circle.setColor(Color.GRAY)
            }
            binding.viewCor.background = circle

            val (badgeRes, textColor) = when (linha.classificacao.lowercase()) {
                "operacional", "normal", "especial" -> Pair(R.drawable.badge_status_green, "#00C853")
                "velocidade", "impacto", "atividade", "parcial", "maiores" -> Pair(R.drawable.badge_status_yellow, "#FFC107")
                "paralisada", "interrompida" -> Pair(R.drawable.badge_status_red, "#FF3B30")
                else -> Pair(R.drawable.badge_status_gray, "#9E9E9E")
            }
            binding.tvStatus.text = linha.status
            binding.tvStatus.setBackgroundResource(badgeRes)
            binding.tvStatus.setTextColor(Color.parseColor(textColor))

            if (linha.descricao.isNotBlank()) {
                binding.root.setOnClickListener { onItemClick(linha) }
                binding.root.isClickable = true
            } else {
                binding.root.setOnClickListener(null)
                binding.root.isClickable = false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLinhaMetroBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), onItemClick)
    }

    class DiffCallback : DiffUtil.ItemCallback<Linha>() {
        override fun areItemsTheSame(oldItem: Linha, newItem: Linha): Boolean =
            oldItem.codigo == newItem.codigo

        override fun areContentsTheSame(oldItem: Linha, newItem: Linha): Boolean =
            oldItem == newItem
    }
}
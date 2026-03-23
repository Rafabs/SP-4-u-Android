package com.rafabs.sp4u.ui.home

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rafabs.sp4u.R
import com.rafabs.sp4u.data.model.Linha
import com.rafabs.sp4u.databinding.ItemLinhaMetroBinding

class MetroAdapter(private var linhas: List<Linha>) :
    RecyclerView.Adapter<MetroAdapter.ViewHolder>() {

    fun updateList(novaLista: List<Linha>) {
        linhas = novaLista
        notifyDataSetChanged()
    }

    class ViewHolder(private val binding: ItemLinhaMetroBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(linha: Linha) {
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

            binding.tvStatus.text = "Operacao Normal"
            binding.tvStatus.setBackgroundResource(R.drawable.badge_status_green)
            binding.tvStatus.setTextColor(Color.parseColor("#00FF1E"))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLinhaMetroBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(linhas[position])
    }

    override fun getItemCount() = linhas.size
}
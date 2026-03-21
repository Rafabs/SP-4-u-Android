package com.rafabs.sp4u.ui.home

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rafabs.sp4u.R
import com.rafabs.sp4u.data.model.Linha
import com.rafabs.sp4u.databinding.ItemLinhaBinding

class LinhaAdapter(private val linhas: List<Linha>) :
    RecyclerView.Adapter<LinhaAdapter.LinhaViewHolder>() {

    inner class LinhaViewHolder(private val binding: ItemLinhaBinding) :
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinhaViewHolder {
        val binding = ItemLinhaBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return LinhaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LinhaViewHolder, position: Int) {
        holder.bind(linhas[position])
    }

    override fun getItemCount() = linhas.size
}
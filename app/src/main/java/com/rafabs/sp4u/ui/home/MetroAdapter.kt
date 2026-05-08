package com.rafabs.sp4u.ui.home

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.graphics.toColorInt
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

            // Usando .toColorInt() do KTX para ser mais idiomático
            val circle = GradientDrawable().apply {
                shape = GradientDrawable.OVAL
                try {
                    setColor(linha.cor.toColorInt())
                } catch (_: Exception) { // "_" remove o aviso de parâmetro não utilizado
                    setColor(Color.GRAY)
                }
            }
            binding.viewCor.background = circle

            // Se o erro de "Unresolved reference" persistir, verifique se no seu Model
            // a classe Linha tem um "val status: Status" e se "Status" tem "situacao"
            val statusObjeto = linha.status
            val situacaoTexto = statusObjeto.situacao.lowercase()

            val (badgeRes, textColor) = when {
                situacaoTexto.contains("normal") || situacaoTexto.contains("operacional") -> {
                    Pair(R.drawable.badge_status_green, "#00C853")
                }
                situacaoTexto.contains("velocidade") ||
                        situacaoTexto.contains("intervalos") ||
                        situacaoTexto.contains("parcial") ||
                        situacaoTexto.contains("atividade") ||
                        situacaoTexto.contains("impacto") -> {
                    Pair(R.drawable.badge_status_yellow, "#FFC107")
                }
                situacaoTexto.contains("paralisada") ||
                        situacaoTexto.contains("interrompida") ||
                        situacaoTexto.contains("encerrada") -> {
                    Pair(R.drawable.badge_status_red, "#FF3B30")
                }
                else -> Pair(R.drawable.badge_status_gray, "#9E9E9E")
            }

            binding.tvStatus.text = statusObjeto.situacao
            binding.tvStatus.setBackgroundResource(badgeRes)
            binding.tvStatus.setTextColor(textColor.toColorInt())

            // Verifique se no seu Model o campo é 'descricao' (sem cedilha)
            if (statusObjeto.descricao.isNotBlank()) {
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
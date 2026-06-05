package com.rafabs.sp4u.ui.olhovivo

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rafabs.sp4u.R
import com.rafabs.sp4u.model.LinhaBusca
class LinhaAdapter(
    private val linhas: List<LinhaBusca>,
    private val onClick: (LinhaBusca) -> Unit
) : RecyclerView.Adapter<LinhaAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtLetreiro: TextView = view.findViewById(R.id.txtLetreiro)
        val txtDestino: TextView = view.findViewById(R.id.txtDestino)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_linha, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val linha = linhas[position]
        holder.txtLetreiro.text = linha.lt
        holder.txtDestino.text = if (linha.sl == 1) "→ ${linha.tp}" else "→ ${linha.ts}"
        holder.itemView.setOnClickListener {
            Log.d("OLHO_VIVO", "Linha clicada: ${linha.cl}")
            onClick(linha)
        }
    }

    override fun getItemCount() = linhas.size
}
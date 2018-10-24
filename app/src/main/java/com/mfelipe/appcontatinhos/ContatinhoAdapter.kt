package com.mfelipe.appcontatinhos

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import com.mfelipe.appcontatinhos.R
import kotlinx.android.synthetic.main.contatinho_item_lista.view.*

class ContatinhoAdapter(val context: Context, val contatinhos: List<Contatinho>)
    : RecyclerView.Adapter<ContatinhoAdapter.ViewHolder>() {

    //metodo responsavel por inflar as views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.contatinho_item_lista, parent, false)
        return ViewHolder(view)
    }
    //retornar a quantidade de itens na lista
    override fun getItemCount(): Int {
        return contatinhos.size
    }
    //popula o viewHolder com as informações do contatinho
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindView(context, contatinhos[position])
    }

    //referencia para a view de cada item da lista
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindView(context: Context, contatinho: Contatinho) {
            itemView.tvNome.text = contatinho.nome
            itemView.tvTelefone.text= contatinho.telefone

            GlideApp.with(context)
                    .load(contatinho.caminhoFoto)
                    .placeholder(R.drawable.ic_person)
                    .centerCrop()
                    .into(itemView.imFoto)

        }

    }
}
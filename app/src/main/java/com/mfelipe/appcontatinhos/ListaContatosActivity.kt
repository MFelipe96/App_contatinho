package com.mfelipe.appcontatinhos

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_lista_contatos.*
import java.util.ArrayList

class ListaContatosActivity : AppCompatActivity() {

    //criar uma constante
    companion object {
        private const val REQUEST_CADASTRO = 1
        private const val LISTA = "ListaContatinhos"
    }


    var listaContatinhos: MutableList<String> = mutableListOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_contatos)

        //mandar o nome para a lista
        val novoContatinho: String? = intent.getStringExtra(CadastraContatinhoActivity.NOME_CONTATINHO)
        if (novoContatinho != null) {
            listaContatinhos.add(novoContatinho)
        }

        btnaddContatinhos.setOnClickListener(){
            //cria objeto da intent
            val cadastrarContatinho = Intent(this, CadastraContatinhoActivity::class.java)
            startActivityForResult(cadastrarContatinho, REQUEST_CADASTRO)
        }
    }

    override fun onResume() {
        super.onResume()
        carregaLista()
    }

    //salva o conteudo para nao perder qnd houver transição de tela (girar a tela)

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        outState?.putStringArrayList(LISTA, listaContatinhos as ArrayList<String>)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)

        if(savedInstanceState != null)
            listaContatinhos = savedInstanceState.getStringArrayList(LISTA)
    }


    fun carregaLista() {
        //ReciclyView
        val adapter = ContatinhoAdapter(listaContatinhos)
        val layoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(this, layoutManager.orientation)

        rvContatinhos.adapter = adapter
        rvContatinhos.layoutManager = layoutManager
        rvContatinhos.addItemDecoration(dividerItemDecoration)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == REQUEST_CADASTRO && resultCode == Activity.RESULT_OK){
            val novoContatinho: String? = data?.getStringExtra("nomeContatinho")
            if (novoContatinho != null) {
                listaContatinhos.add(novoContatinho)
            }
        }
    }
}

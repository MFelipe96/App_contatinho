package com.mfelipe.appcontatinhos

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.widget.PopupMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_cadastro.*
import java.io.File
import java.nio.file.attribute.FileAttributeView


class CadastraContatinhoActivity : AppCompatActivity() {


    //cria uma constante publica por ser usada em outra classe
    companion object {
        public const val NOME_CONTATINHO: String = "nomeContatinho"
        private const val REQUEST_PERMISSOES: Int = 3
        private const val REQUEST_CAMERA: Int = 10
    }

    var caminhoFoto:String? = null

    //apenas para poder chamar no else da permissao
    val efetuarLigacao = Intent(Intent.ACTION_CALL)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)

        btnCamera.setOnClickListener(){
            tirarFoto()
        }

        imagePerson.setOnClickListener(){

        }


        imageEmail.setOnClickListener(){

            enviaEmail()

        }

        imageTelefone.setOnClickListener() {view ->

            EnviaMsgOuTelefona(view)
        }
        imageMap.setOnClickListener(){

            abreMapa()

        }


    }

    private fun tirarFoto() {

        val tirarFoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if(tirarFoto.resolveActivity(packageManager) != null){

            val arquivoFoto = montaArquivoFoto()
            val uriFoto = FileProvider.getUriForFile(this, "$(BuildConfig.APPLICATION_ID).fileprovider", arquivoFoto)
            tirarFoto.putExtra(MediaStore.EXTRA_OUTPUT, uriFoto)

            startActivityForResult(tirarFoto, REQUEST_CAMERA)
        }else{
            Toast.makeText(this, "Não foi possível abrir a camera", Toast.LENGTH_SHORT).show()
        }
    }

    private fun EnviaMsgOuTelefona(view: View) {
        val popup = PopupMenu(this, view)
        popup.inflate(R.menu.menu_telefona_ou_msg)

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menuMensagem -> enviaMensagem()
                R.id.menuTelefona -> efetuaLigacao()
                else -> false
            }
        }

        popup.show()
    }

    private fun enviaEmail() {
        val enviarEmail = Intent(Intent.ACTION_VIEW)
        enviarEmail.data = Uri.parse("mailto:$(editEmail.text)")
        enviarEmail.putExtra(Intent.EXTRA_SUBJECT, "Oi, sumida!")

        if (enviarEmail.resolveActivity(packageManager) != null) {
            startActivity(enviarEmail)
        } else {
            Toast.makeText(this, "Impossível abrir e-mail", Toast.LENGTH_SHORT).show()
        }
    }

    private fun abreMapa() {
        val mostrarMapa = Intent(Intent.ACTION_VIEW)
        mostrarMapa.data = Uri.parse("geo: 0,0?q=$(editEnd.text )")

        if (mostrarMapa.resolveActivity(packageManager) != null) {
            startActivity(mostrarMapa)
        } else {
            Toast.makeText(this, "Impossível mostrar no mapa", Toast.LENGTH_SHORT).show()
        }
    }

    private fun enviaMensagem(): Boolean {
        val enviarsms = Intent(Intent.ACTION_VIEW)
        enviarsms.data = Uri.parse("sms:$(editTelefone.text)")
        enviarsms.putExtra("sms_body", "Oi, sumida!")

        if (enviarsms.resolveActivity(this.packageManager) != null) {
            startActivity(enviarsms)
            return true
        } else {
            Toast.makeText(this, "Impossível enviar mensagem", Toast.LENGTH_SHORT).show()
            return false
        }
    }

    private fun efetuaLigacao(): Boolean {

        // (global) val efetuarLigacao = Intent(Intent.ACTION_CALL)
        efetuarLigacao.data = Uri.parse("tel:$(editTelefone.text)")
        efetuarLigacao.putExtra("sms_body", "Oi, sumida!")

        //verifica se pode fazer a ligação
        if (efetuarLigacao.resolveActivity(this.packageManager) != null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                //verifica se tenho permissao para fazer ligação
                if (checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(arrayOf(Manifest.permission.CALL_PHONE), REQUEST_PERMISSOES)
                    return false
                } else {
                    startActivity(efetuarLigacao)
                    return true
                }
            } else {
                    startActivity(efetuarLigacao)
                    return true
            }

        }else{
            Toast.makeText(this, "Impossível efetuar ligação", Toast.LENGTH_SHORT).show()
            return false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_cadastro,menu);
        return super.onCreateOptionsMenu(menu)
    }
    //interagir com o botão "salvar"
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.menu_salvar -> salvaContatinho()
        }
        return super.onOptionsItemSelected(item)
    }

    //verifica se o app tem permissao
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == REQUEST_PERMISSOES && (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED)){

            Toast.makeText(this, "É necessário permissão para fazer ligação", Toast.LENGTH_SHORT).show()

        }
        else{
            startActivity(efetuarLigacao)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {

            GlideApp.with(this)
                    .load(caminhoFoto)
                    .placeholder(R.drawable.ic_person)
                    .centerCrop()
                    .into(image_person)
        }
    }

    private fun montaArquivoFoto(): File {

        val nomeArquivo = System.currentTimeMillis().toString()
        val diretorioArquivo = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        val arquivoFoto = File.createTempFile(nomeArquivo, "jpg", diretorioArquivo)

        caminhoFoto = arquivoFoto.absolutePath

        return arquivoFoto

    }


    private fun salvaContatinho() {

        val contatinho = Contatinho(editNome.text.toString(),
                                    editTelefone.text.toString())

        //Toast.makeText(this, contatinho.toString(), Toast.LENGTH_LONG).show()

        abreListaContatinhos(contatinho)
    }

    private fun abreListaContatinhos(contatinho: Contatinho) {
        val abreLista = Intent(this, ListaContatosActivity::class.java)
        abreLista.putExtra(NOME_CONTATINHO, contatinho.nome)
        setResult(Activity.RESULT_OK, abreLista)
        finish()
    }


}

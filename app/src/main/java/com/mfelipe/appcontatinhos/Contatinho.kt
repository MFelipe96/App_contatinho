package com.mfelipe.appcontatinhos

data class Contatinho(val nome: String,
                      val telefone: String,
                      val email: String? = null,
                      val endereco: String? = null,
                      val caminhoFoto: String? = null)
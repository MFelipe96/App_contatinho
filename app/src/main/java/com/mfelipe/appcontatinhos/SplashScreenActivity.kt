package com.mfelipe.appcontatinhos

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        GlideApp.with(this)
                .load("https://2.bp.blogspot.com/-Rp2iGcdIYOc/V1CHYDmZd8I/AAAAAAAAAFE/h-2IIRsPEAAnQMJNiXpvlVNO8Dddwio4ACKgB/s1600/3025185-slide-s-3-rel-emojis-creepy-pasta.jpg")
                .placeholder(R.drawable.ic_launcher_app)
                .centerCrop()
                .into(img_splash)
        Handler().postDelayed({
            val listaContatinho = Intent(this, ListaContatosActivity::class.java)
            startActivity(listaContatinho)
            finish()
        }, 2000)

        }
    }


package com.meuapp

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.os.Handler
import android.os.Looper

class ServicoBarra : Service() {
    private lateinit var wm: WindowManager
    private var minhaBarra: View? = null

    private val receber = object : BroadcastReceiver() {
        override fun onReceive(contexto: Context?, intent: Intent?) {
            if (intent?.action == "NOVA_MENSAGEM") {
                val quem = intent.getStringExtra("de") ?: ""
                val msg = intent.getStringExtra("texto") ?: ""
                mostrarMensagem(quem, msg)
            }
        }
    }

    override fun onCreate() {
        super.onCreate()
        wm = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        registerReceiver(receber, IntentFilter("NOVA_MENSAGEM"))
        criarBarra()
    }

    private fun criarBarra() {
        minhaBarra = LayoutInflater.from(this).inflate(R.layout.barra, null)
        val params = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            WindowManager.LayoutParams(
                200, 42,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSPARENT
            )
        } else {
            @Suppress("DEPRECATION")
            WindowManager.LayoutParams(200, 42, WindowManager.LayoutParams.TYPE_PHONE, 0, PixelFormat.TRANSPARENT)
        }
        params.gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
        params.y = 30
        wm.addView(minhaBarra, params)
    }

    private fun mostrarMensagem(quem: String, msg: String) {
        val bolinha = minhaBarra?.findViewById<View>(R.id.bolinha)
        val cima = minhaBarra?.findViewById<TextView>(R.id.texto_cima)
        val baixo = minhaBarra?.findViewById<TextView>(R.id.texto_baixo)

        bolinha?.visibility = View.VISIBLE
        cima?.text = "💬 $quem"
        baixo?.text = msg.take(15) + if (msg.length > 15) "..." else ""

        Handler(Looper.getMainLooper()).postDelayed({
            bolinha?.visibility = View.GONE
            cima?.text = ""
            baixo?.text = "✨ Pronto"
        }, 3500)
    }

    override fun onBind(intent: Intent?) = null
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receber)
        minhaBarra?.let { wm.removeView(it) }
    }
}

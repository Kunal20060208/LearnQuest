package com.example.learnquest

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.*
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val logo = findViewById<ImageView>(R.id.logo)
        val appName = findViewById<TextView>(R.id.appName)

        // 🔥 SCALE ANIMATION (LOGO)
        val scale = ScaleAnimation(
            0.5f, 1f,
            0.5f, 1f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        )
        scale.duration = 1200
        scale.fillAfter = true

        // 🔥 FADE IN TEXT
        val fade = AlphaAnimation(0f, 1f)
        fade.duration = 1500
        fade.fillAfter = true

        logo.startAnimation(scale)
        appName.startAnimation(fade)

        // ⏳ MOVE TO LOGIN
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }, 2000)
    }
}
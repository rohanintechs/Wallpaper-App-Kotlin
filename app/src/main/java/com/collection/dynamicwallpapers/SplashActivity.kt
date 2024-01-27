package com.collection.dynamicwallpapers

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Simulate a delay (e.g., 2 seconds) to display the splash screen
        val splashDuration = 3000L
        Thread {
            Thread.sleep(splashDuration)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }.start()
    }
}

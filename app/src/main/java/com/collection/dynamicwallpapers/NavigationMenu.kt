package com.collection.dynamicwallpapers

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class NavigationMenu : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation_menu)

        val aboutUsTextView = findViewById<TextView>(R.id.menu_about_us)
        aboutUsTextView.setOnClickListener {
            openAboutUsLink()
        }

        // Other initialization code
    }

    private fun openAboutUsLink() {
        val aboutUsUrl = "https://www.google.com" // Replace with your actual URL
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(aboutUsUrl))
        startActivity(intent)
    }
}

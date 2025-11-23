package com.example.ytblocker

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // REQUIRED – initialize secure preferences system
        Prefs.init(this)

        setContentView(R.layout.activity_main)

        // Buttons
        val btnStrict = findViewById<Button>(R.id.btnStrictMode)
        val btnPassword = findViewById<Button>(R.id.btnPassword)
        val btnEnable = findViewById<Button>(R.id.btnEnable)

        // Switches
        val swBlockApp = findViewById<Switch>(R.id.switch_block_yt_app)
        val swBlockWeb = findViewById<Switch>(R.id.switch_block_yt_web)

        // Restore switches state
        swBlockApp.isChecked = Prefs.getBlockYTApp()
        swBlockWeb.isChecked = Prefs.getBlockYTWeb()

        // Save switches
        swBlockApp.setOnCheckedChangeListener { _, isChecked ->
            Prefs.setBlockYTApp(isChecked)
        }
        swBlockWeb.setOnCheckedChangeListener { _, isChecked ->
            Prefs.setBlockYTWeb(isChecked)
        }

        // Open Strict Mode page
        btnStrict.setOnClickListener {
            println("Strict Mode button clicked")
            startActivity(Intent(this, StrictModeActivity::class.java))
        }

        // Open Password Settings page
        btnPassword.setOnClickListener {
            println("Password button clicked")
            startActivity(Intent(this, PasswordActivity::class.java))
        }

        // Open Accessibility Settings
        btnEnable.setOnClickListener {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }

        // Debug
        println("YTBlocker: strict mode = ${Prefs.getStrictMode()}")
        println("YTBlocker: password = ${Prefs.getPassword()}")
    }
}

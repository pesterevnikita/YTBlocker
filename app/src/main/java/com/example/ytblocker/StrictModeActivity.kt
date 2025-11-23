package com.example.ytblocker

import android.os.Bundle
import android.widget.Button
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class StrictModeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Prefs.init(this)
        setContentView(R.layout.activity_strict_mode)

        val switchStrict = findViewById<Switch>(R.id.switchStrictMode)
        val btnDisable = findViewById<Button>(R.id.btnDisableStrict)

        // Load current state
        switchStrict.isChecked = Prefs.getStrictMode()

        // Enable strict mode (no password required)
        switchStrict.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Prefs.setStrictMode(true)
                Toast.makeText(this, "Strict Mode Enabled", Toast.LENGTH_SHORT).show()
            }
        }

        // Disable strict mode — requires password
        btnDisable.setOnClickListener {
            if (!Prefs.getStrictMode()) {
                Toast.makeText(this, "Already disabled", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val dialog = PasswordManager(this)
            dialog.askPassword(
                onCorrect = {
                    Prefs.setStrictMode(false)
                    switchStrict.isChecked = false
                    Toast.makeText(this, "Strict Mode Disabled", Toast.LENGTH_SHORT).show()
                },
                onWrong = {
                    Toast.makeText(this, "Wrong password", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}

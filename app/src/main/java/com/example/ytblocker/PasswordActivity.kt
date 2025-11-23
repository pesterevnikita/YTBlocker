package com.example.ytblocker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PasswordActivity : AppCompatActivity() {

    private var etOld: EditText? = null
    private lateinit var etNew: EditText
    private lateinit var etNew2: EditText
    private lateinit var btnSave: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Prefs.init(this) // ensure prefs initialized
        setContentView(R.layout.activity_password)

        // find views (etOld may be absent in some layouts — handle safely)
        etOld = findViewById(R.id.etOldPassword)
        etNew = findViewById(R.id.etNewPassword)
        etNew2 = findViewById(R.id.etNewPassword2)
        btnSave = findViewById(R.id.btnSetPassword)

        // If no password has been set yet, hide the "old password" field (if it exists).
        val existing = Prefs.getPassword()
        if (existing == null) {
            etOld?.visibility = View.GONE
        } else {
            etOld?.visibility = View.VISIBLE
        }

        btnSave.setOnClickListener {
            val new1 = etNew.text?.toString() ?: ""
            val new2 = etNew2.text?.toString() ?: ""

            if (new1.isEmpty()) {
                Toast.makeText(this, "New password cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (new1 != new2) {
                Toast.makeText(this, "New passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // If password exists, require old password to match
            if (existing != null) {
                val oldValue = etOld?.text?.toString() ?: ""
                if (oldValue != existing) {
                    Toast.makeText(this, "Old password is incorrect", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            // Save new password
            Prefs.setPassword(new1)
            Toast.makeText(this, "Password saved", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}

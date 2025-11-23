package com.example.ytblocker

import android.app.AlertDialog
import android.content.Context
import android.widget.EditText

class PasswordManager(private val context: Context) {

    fun askPassword(onCorrect: () -> Unit, onWrong: () -> Unit) {
        val input = EditText(context)
        input.inputType = android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD

        AlertDialog.Builder(context)
            .setTitle("Enter Password")
            .setView(input)
            .setPositiveButton("OK") { _, _ ->
                val entered = input.text.toString()
                if (entered == Prefs.getPassword()) {
                    onCorrect()
                } else {
                    onWrong()
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}

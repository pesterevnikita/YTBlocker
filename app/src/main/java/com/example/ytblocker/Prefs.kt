package com.example.ytblocker

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

object Prefs {

    private const val PREFS_NAME = "secure_prefs"

    private lateinit var securePrefs: SharedPreferences

    fun init(context: Context) {
        if (this::securePrefs.isInitialized) return

        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        securePrefs = EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    // PASSWORD
    fun setPassword(pass: String) {
        securePrefs.edit().putString("password", pass).apply()
    }

    fun getPassword(): String? =
        securePrefs.getString("password", null)

    // STRICT MODE
    fun setStrictMode(enabled: Boolean) {
        securePrefs.edit().putBoolean("strict_mode", enabled).apply()
    }

    fun getStrictMode(): Boolean =
        securePrefs.getBoolean("strict_mode", false)

    fun setBlockYTApp(value: Boolean) =
        securePrefs.edit().putBoolean("block_yt_app", value).apply()

    fun getBlockYTApp(): Boolean =
        securePrefs.getBoolean("block_yt_app", true)

    fun setBlockYTWeb(value: Boolean) =
        securePrefs.edit().putBoolean("block_yt_web", value).apply()

    fun getBlockYTWeb(): Boolean =
        securePrefs.getBoolean("block_yt_web", true)

}

package com.example.ytblocker

import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.widget.Button
import android.widget.Switch
import android.widget.TextView
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
        val btnDeviceAdmin = findViewById<Button>(R.id.btnDeviceAdmin)

        // Status
        val tvStatus = findViewById<TextView>(R.id.tvStatus)
        val tvAdminStatus = findViewById<TextView>(R.id.tvAdminStatus)

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

        btnDeviceAdmin.setOnClickListener {
            val component = ComponentName(this, DeviceAdmin::class.java)
            val intent = Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).apply {
                putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, component)
                putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, getString(R.string.request_admin_explanation))
            }
            startActivity(intent)
        }

        refreshStatus(tvStatus, tvAdminStatus)

        // Debug
        println("YTBlocker: strict mode = ${Prefs.getStrictMode()}")
        println("YTBlocker: password = ${Prefs.getPassword()}")
    }

    override fun onResume() {
        super.onResume()
        val tvStatus = findViewById<TextView>(R.id.tvStatus)
        val tvAdminStatus = findViewById<TextView>(R.id.tvAdminStatus)
        refreshStatus(tvStatus, tvAdminStatus)
    }

    private fun refreshStatus(tvStatus: TextView, tvAdminStatus: TextView) {
        val strictText = if (Prefs.getStrictMode()) {
            getString(R.string.strict_mode_status_on)
        } else {
            getString(R.string.strict_mode_status_off)
        }

        val serviceText = if (isAccessibilityEnabled()) {
            getString(R.string.accessibility_status_on)
        } else {
            getString(R.string.accessibility_status_off)
        }

        tvStatus.text = "$strictText\n$serviceText"

        val devicePolicyManager = getSystemService(DEVICE_POLICY_SERVICE) as DevicePolicyManager
        val adminActive = devicePolicyManager.isAdminActive(ComponentName(this, DeviceAdmin::class.java))
        tvAdminStatus.text = if (adminActive) {
            getString(R.string.device_admin_status_on)
        } else {
            getString(R.string.device_admin_status_off)
        }
    }

    private fun isAccessibilityEnabled(): Boolean {
        val expectedComponent = ComponentName(this, BlockService::class.java)
        val enabledServices = Settings.Secure.getString(
            contentResolver,
            Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
        ) ?: return false

        val colonSplitter = TextUtils.SimpleStringSplitter(':')
        colonSplitter.setString(enabledServices)
        while (colonSplitter.hasNext()) {
            val componentName = colonSplitter.next()
            if (ComponentName.unflattenFromString(componentName) == expectedComponent) {
                return true
            }
        }
        return false
    }
}

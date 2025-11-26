package com.example.ytblocker

import android.app.admin.DeviceAdminReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class DeviceAdmin : DeviceAdminReceiver() {

    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    override fun onEnabled(context: Context, intent: Intent) {
        showToast(context, context.getString(R.string.device_admin_enabled))
    }

    override fun onDisabled(context: Context, intent: Intent) {
        showToast(context, context.getString(R.string.device_admin_disabled))
    }
}

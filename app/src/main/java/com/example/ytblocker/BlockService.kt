package com.example.ytblocker

import android.accessibilityservice.AccessibilityService
import android.view.accessibility.AccessibilityEvent

class BlockService : AccessibilityService() {

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        // Strict mode ON?
        if (!Prefs.getStrictMode()) return

        val pkg = event.packageName?.toString() ?: return

        // Debug log
        println("YTBlocker: opened package = $pkg")

        // Block YouTube app
        if (pkg == "com.google.android.youtube") {
            println("YTBlocker: Blocking YouTube app")
            goHome()
            return
        }

        // Block YouTube site inside browsers
        if (pkg.contains("chrome", ignoreCase = true) ||
            pkg.contains("firefox", ignoreCase = true) ||
            pkg.contains("edge", ignoreCase = true) ||
            pkg.contains("opera", ignoreCase = true) ||
            pkg.contains("browser", ignoreCase = true)
        ) {
            val text = event.text?.joinToString(" ")?.lowercase() ?: ""
            if (text.contains("youtube")) {
                println("YTBlocker: Blocking YouTube site in browser")
                goHome()
            }
        }
    }

    private fun goHome() {
        try {
            performGlobalAction(GLOBAL_ACTION_HOME)
            println("YTBlocker: Home action performed")
        } catch (e: Exception) {
            println("YTBlocker: ERROR performing home $e")
        }
    }

    override fun onInterrupt() {
        // Nothing needed
    }
}

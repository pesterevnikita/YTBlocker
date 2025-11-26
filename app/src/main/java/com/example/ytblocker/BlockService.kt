package com.example.ytblocker

import android.accessibilityservice.AccessibilityService
import android.util.Log
import android.view.accessibility.AccessibilityEvent

class BlockService : AccessibilityService() {

    companion object {
        private const val TAG = "YTBlocker"

        private val browserPackages = setOf(
            "com.android.chrome",
            "org.mozilla.firefox",
            "com.microsoft.emmx",
            "com.opera.browser",
            "com.brave.browser",
            "com.vivaldi.browser",
            "com.sec.android.app.sbrowser",
            "com.chrome.beta",
            "com.google.android.apps.chrome"
        )

        private val youtubePackages = setOf(
            "com.google.android.youtube",
            "com.google.android.apps.youtube.music",
            "com.google.android.youtube.tv",
            "com.google.android.apps.youtube.mango"
        )
    }

    override fun onServiceConnected() {
        super.onServiceConnected()
        Prefs.init(this)
        Log.i(TAG, "Accessibility service connected")
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null) return

        Prefs.init(this)

        // Strict mode ON?
        if (!Prefs.getStrictMode()) return

        val pkg = event.packageName?.toString() ?: return

        Log.d(TAG, "Opened package = $pkg")

        // Block YouTube app
        if (Prefs.getBlockYTApp() && pkg in youtubePackages) {
            Log.i(TAG, "Blocking YouTube app")
            goHome()
            return
        }

        // Block YouTube site inside browsers
        if (Prefs.getBlockYTWeb() && pkg in browserPackages) {
            val rawText = buildList {
                addAll(event.text?.map { it.toString() } ?: emptyList())
                event.contentDescription?.toString()?.let { add(it) }
            }.joinToString(" ").lowercase()

            if (rawText.contains("youtube") || rawText.contains("youtu.be")) {
                Log.i(TAG, "Blocking YouTube site in browser")
                goHome()
            }
        }
    }

    private fun goHome() {
        try {
            performGlobalAction(GLOBAL_ACTION_HOME)
            Log.d(TAG, "Home action performed")
        } catch (e: Exception) {
            Log.e(TAG, "Error performing home", e)
        }
    }

    override fun onInterrupt() {
        // Nothing needed
    }
}

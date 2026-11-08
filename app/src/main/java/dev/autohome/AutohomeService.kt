package dev.autohome

import android.accessibilityservice.AccessibilityService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.SystemClock
import android.view.accessibility.AccessibilityEvent

/**
 * Tracks how long the screen has been off. When the phone is turned back on after at
 * least the configured delay, presses Home so the user starts on the launcher instead
 * of whatever app was open before.
 */
class AutohomeService : AccessibilityService() {

    private var screenOffAt = 0L
    private var goHomeOnUnlock = false

    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                Intent.ACTION_SCREEN_OFF -> screenOffAt = SystemClock.elapsedRealtime()
                Intent.ACTION_SCREEN_ON -> {
                    val offFor = SystemClock.elapsedRealtime() - screenOffAt
                    if (screenOffAt != 0L && offFor >= Prefs.delaySeconds(context) * 1000L) {
                        goHomeOnUnlock = true
                        // Going home now, behind the lock screen, means the old app is
                        // never shown. Some devices ignore this while locked, so we
                        // repeat it on unlock.
                        performGlobalAction(GLOBAL_ACTION_HOME)
                    }
                }
                Intent.ACTION_USER_PRESENT -> {
                    if (goHomeOnUnlock) {
                        goHomeOnUnlock = false
                        performGlobalAction(GLOBAL_ACTION_HOME)
                    }
                }
            }
        }
    }

    override fun onServiceConnected() {
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_OFF)
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_USER_PRESENT)
        }
        registerReceiver(receiver, filter)
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent) {}

    override fun onInterrupt() {}
}

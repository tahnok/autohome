package dev.autohome

import android.content.Context

object Prefs {
    /** Selectable delays, in seconds. */
    val DELAYS = intArrayOf(30, 60, 2 * 60, 5 * 60, 10 * 60, 15 * 60, 20 * 60, 30 * 60, 45 * 60, 60 * 60)
    private const val DEFAULT_DELAY = 10 * 60
    private const val KEY_DELAY = "delay_seconds"

    private fun prefs(context: Context) =
        context.getSharedPreferences("autohome", Context.MODE_PRIVATE)

    fun delaySeconds(context: Context): Int = prefs(context).getInt(KEY_DELAY, DEFAULT_DELAY)

    fun setDelaySeconds(context: Context, seconds: Int) {
        prefs(context).edit().putInt(KEY_DELAY, seconds).apply()
    }

    fun format(seconds: Int): String = when {
        seconds < 60 -> "$seconds seconds"
        seconds == 60 -> "1 minute"
        else -> "${seconds / 60} minutes"
    }
}

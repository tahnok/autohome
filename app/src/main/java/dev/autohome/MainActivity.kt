package dev.autohome

import android.accessibilityservice.AccessibilityServiceInfo
import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.accessibility.AccessibilityManager
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView

class MainActivity : Activity() {

    private lateinit var status: TextView
    private lateinit var restrictedHint: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        status = findViewById(R.id.status)
        restrictedHint = findViewById(R.id.restricted_hint)
        findViewById<Button>(R.id.open_settings).setOnClickListener {
            startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
        }

        val label = findViewById<TextView>(R.id.delay_label)
        val seekBar = findViewById<SeekBar>(R.id.delay)
        val current = Prefs.delaySeconds(this)
        seekBar.max = Prefs.DELAYS.size - 1
        seekBar.progress = Prefs.DELAYS.indexOfFirst { it >= current }.coerceAtLeast(0)
        label.text = getString(R.string.delay_label, Prefs.format(Prefs.DELAYS[seekBar.progress]))
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(bar: SeekBar, progress: Int, fromUser: Boolean) {
                val seconds = Prefs.DELAYS[progress]
                Prefs.setDelaySeconds(this@MainActivity, seconds)
                label.text = getString(R.string.delay_label, Prefs.format(seconds))
            }

            override fun onStartTrackingTouch(bar: SeekBar) {}
            override fun onStopTrackingTouch(bar: SeekBar) {}
        })
    }

    override fun onResume() {
        super.onResume()
        val enabled = isServiceEnabled()
        status.setText(if (enabled) R.string.status_on else R.string.status_off)
        restrictedHint.visibility = if (enabled) View.GONE else View.VISIBLE
    }

    private fun isServiceEnabled(): Boolean {
        val ours = ComponentName(this, AutohomeService::class.java)
        return getSystemService(AccessibilityManager::class.java)
            .getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_ALL_MASK)
            .any { it.resolveInfo.serviceInfo.let { s -> ComponentName(s.packageName, s.name) } == ours }
    }
}

package com.pomonyang.mohanyang.data.local.device.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class LockScreenBroadcastReceiver(
    private var onScreenStateChanged: (Boolean) -> Unit = {}
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) return
        when (intent.action) {
            Intent.ACTION_SCREEN_ON -> onScreenStateChanged(false)
            Intent.ACTION_SCREEN_OFF -> onScreenStateChanged(true)
        }
    }
}

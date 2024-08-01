package com.pomonyang.mohanyang.data.local.device.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class LockScreenBroadcastReceiver(
    private var lockStateListener: (Boolean) -> Unit
) : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) return
        when (intent.action) {
            Intent.ACTION_SCREEN_ON -> lockStateListener(false)
            Intent.ACTION_SCREEN_OFF -> lockStateListener(true)
        }
    }
}

package com.pomonyang.mohanyang.data.local.device.util

import android.content.Context
import android.content.Context.RECEIVER_EXPORTED
import android.content.Context.RECEIVER_NOT_EXPORTED
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import com.pomonyang.mohanyang.data.local.device.receiver.LockScreenBroadcastReceiver
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

fun Context.lockScreenState() = callbackFlow {
    val lockScreenReceiver = LockScreenBroadcastReceiver { isLocked ->
        trySend(isLocked)
    }

    val screenEventIntentFilter = IntentFilter().apply {
        addAction(Intent.ACTION_SCREEN_ON)
        addAction(Intent.ACTION_SCREEN_OFF)
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        applicationContext.registerReceiver(
            lockScreenReceiver,
            screenEventIntentFilter,
            RECEIVER_NOT_EXPORTED
        )
    } else {
        applicationContext.registerReceiver(
            lockScreenReceiver,
            screenEventIntentFilter,
            RECEIVER_EXPORTED
        )
    }

    awaitClose {
        applicationContext.unregisterReceiver(lockScreenReceiver)
    }
}

package com.pomonyang.mohanyang.data.local.device.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import com.pomonyang.mohanyang.data.local.device.receiver.LockScreenBroadcastReceiver
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

interface LockScreenMonitor {
    val isLocked: Flow<Boolean>
}

@AndroidEntryPoint
internal class LockScreenMonitorImpl @Inject constructor(
    @ApplicationContext private val applicationContext: Context
) : Service(),
    LockScreenMonitor {

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    override val isLocked: Flow<Boolean> = callbackFlow {
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
                screenEventIntentFilter
            )
        }

        awaitClose {
            applicationContext.unregisterReceiver(lockScreenReceiver)
        }
    }

    override fun onBind(intent: Intent?): IBinder? = null
}

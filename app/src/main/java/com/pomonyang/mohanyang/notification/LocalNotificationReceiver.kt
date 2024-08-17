package com.pomonyang.mohanyang.notification

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pomonyang.mohanyang.R
import com.pomonyang.mohanyang.data.repository.user.UserRepository
import com.pomonyang.mohanyang.notification.util.defaultNotification
import com.pomonyang.mohanyang.notification.util.isNotificationGranted
import com.pomonyang.mohanyang.notification.util.summaryNotification
import com.pomonyang.mohanyang.presentation.model.cat.CatType
import com.pomonyang.mohanyang.presentation.util.MnNotificationManager
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LocalNotificationReceiver @Inject constructor() : BroadcastReceiver() {

    @Inject
    lateinit var userRepository: UserRepository

    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null || context == null) return

        scope.launch {
            val cat = CatType.safeValueOf(userRepository.getMyInfo().cat.type)

            when (intent.action) {
                MnNotificationManager.INTENT_SEND_MESSAGE -> {
                    val notificationId = intent.getIntExtra(context.getString(R.string.local_notification_id), 0)
                    val title = intent.getStringExtra(context.getString(R.string.local_notification_title)) ?: ""
                    val message = intent.getStringExtra(context.getString(R.string.local_notification_message)) ?: ""
                    notifyMessage(context, notificationId, title, message)
                }

                MnNotificationManager.INTENT_NOTIFY_REST_MESSAGE -> {
                    val id = UUID.randomUUID().hashCode()
                    notifyMessage(
                        context,
                        notificationId = id,
                        message = context.getString(cat.restEndPushContent)
                    )
                }

                MnNotificationManager.INTENT_NOTIFY_FOCUS_MESSAGE -> {
                    val id = UUID.randomUUID().hashCode()
                    notifyMessage(
                        context,
                        notificationId = id,
                        message = context.getString(cat.timerEndPushContent)
                    )
                }

                MnNotificationManager.INTENT_START_INTERRUPT_MESSAGE -> {
                    context.startService(Intent(context, FocusNotificationService::class.java))
                }

                MnNotificationManager.INTENT_STOP_INTERRUPT_MESSAGE -> {
                    context.stopService(Intent(context, FocusNotificationService::class.java))
                }
            }
        }
    }

    private fun notifyMessage(context: Context, notificationId: Int, title: String = context.getString(R.string.app_name), message: String) {
        if (!context.isNotificationGranted()) return

        val notificationIntent = context.packageManager.getLaunchIntentForPackage(context.packageName)?.apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        }

        val pendingIntent =
            PendingIntent.getActivity(
                context,
                notificationId,
                notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        val notification =
            context.defaultNotification(pendingIntent)
                .setContentTitle(title)
                .setContentText(message)
                .build()

        val summaryNotification =
            context.summaryNotification(pendingIntent)
                .setContentTitle(title)
                .setContentText(message)
                .build()

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.apply {
            notify(notificationId, notification)
            notify(SUMMARY_ID, summaryNotification)
        }
    }

    companion object {
        private const val SUMMARY_ID = 0
    }
}

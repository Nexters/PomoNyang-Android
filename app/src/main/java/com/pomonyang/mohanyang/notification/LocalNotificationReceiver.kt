package com.pomonyang.mohanyang.notification

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pomonyang.mohanyang.R
import com.pomonyang.mohanyang.data.repository.push.PushAlarmRepository
import com.pomonyang.mohanyang.data.repository.user.UserRepository
import com.pomonyang.mohanyang.notification.util.defaultNotification
import com.pomonyang.mohanyang.notification.util.isNotificationGranted
import com.pomonyang.mohanyang.notification.util.summaryNotification
import com.pomonyang.mohanyang.presentation.model.cat.CatType
import com.pomonyang.mohanyang.presentation.util.MnNotificationManager
import com.pomonyang.mohanyang.ui.ServiceHelper
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

    @Inject
    lateinit var pushAlarmRepository: PushAlarmRepository

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
                    val channelId = intent.getStringExtra(context.getString(R.string.local_notification_channel_id)) ?: context.getString(R.string.channel_id)

                    notifyMessage(
                        context,
                        notificationId = notificationId,
                        channelId = channelId,
                        title = title,
                        message = message,
                    )
                }

                MnNotificationManager.INTENT_NOTIFY_REST_MESSAGE -> {
                    if (pushAlarmRepository.isTimerNotificationEnabled()) {
                        val id = UUID.randomUUID().hashCode()
                        notifyMessage(
                            context,
                            notificationId = id,
                            channelId = context.getString(R.string.channel_id),
                            message = context.getString(cat.restEndPushContent),
                        )
                    }
                }

                MnNotificationManager.INTENT_NOTIFY_FOCUS_MESSAGE -> {
                    if (pushAlarmRepository.isTimerNotificationEnabled()) {
                        val id = UUID.randomUUID().hashCode()
                        notifyMessage(
                            context,
                            notificationId = id,
                            channelId = context.getString(R.string.channel_id),
                            message = context.getString(cat.timerEndPushContent),
                        )
                    }
                }

                MnNotificationManager.INTENT_START_INTERRUPT_MESSAGE -> {
                    if (pushAlarmRepository.isInterruptNotificationEnabled()) {
                        context.startService(Intent(context, FocusNotificationService::class.java))
                    }
                }

                MnNotificationManager.INTENT_STOP_INTERRUPT_MESSAGE -> {
                    context.stopService(Intent(context, FocusNotificationService::class.java))
                }
            }
        }
    }

    private fun notifyMessage(
        context: Context,
        notificationId: Int,
        channelId: String,
        title: String = context.getString(R.string.app_name),
        message: String,
    ) {
        if (!context.isNotificationGranted()) return

        val pendingIntent = ServiceHelper.clickPendingIntent(context, notificationId)

        val notification =
            context.defaultNotification(pendingIntent, channelId = channelId)
                .setContentTitle(title)
                .setContentText(message)
                .build()

        val summaryNotification =
            context.summaryNotification(pendingIntent, channelId = channelId)
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

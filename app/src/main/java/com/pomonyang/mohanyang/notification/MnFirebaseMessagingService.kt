package com.pomonyang.mohanyang.notification

import android.Manifest
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pomonyang.mohanyang.MainActivity
import com.pomonyang.mohanyang.R
import com.pomonyang.mohanyang.data.repository.push.PushAlarmRepository
import com.pomonyang.mohanyang.data.repository.user.UserRepository
import com.pomonyang.mohanyang.notification.util.defaultNotification
import com.pomonyang.mohanyang.notification.util.isNotificationGranted
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
internal class MnFirebaseMessagingService : FirebaseMessagingService() {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    @Inject
    lateinit var pushAlarmRepository: PushAlarmRepository

    @Inject
    lateinit var userRepository: UserRepository

    override fun onNewToken(token: String) {
        scope.launch {
            pushAlarmRepository.saveFcmToken(token)
            userRepository.getMyInfo().let {
                if (it.isPushEnabled) {
                    pushAlarmRepository.apply {
                        registerPushToken(token)
                    }
                }
            }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        if (!applicationContext.isNotificationGranted()) return

        val isPushEnabled = runBlocking { userRepository.getMyInfo().isPushEnabled }
        if (!isPushEnabled) return

        val notification = remoteMessage.notification ?: return

        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            UUID.randomUUID().hashCode(),
            Intent(applicationContext, MainActivity::class.java),
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE, // 일회용 펜딩 인텐트
        )

        val builder = applicationContext.defaultNotification(pendingIntent, getString(R.string.channel_id))

        builder.apply {
            setContentTitle(notification.title)
            setContentText(notification.body)
        }

        val id = UUID.randomUUID().hashCode()

        NotificationManagerCompat.from(this).notify(id, builder.build())
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }
}

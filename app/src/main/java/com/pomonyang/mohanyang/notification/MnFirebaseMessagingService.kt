package com.pomonyang.mohanyang.notification

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.pomonyang.mohanyang.data.repository.push.PushAlarmRepository
import com.pomonyang.mohanyang.notification.util.defaultNotification
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

@AndroidEntryPoint
internal class MnFirebaseMessagingService : FirebaseMessagingService() {
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    @Inject
    lateinit var pushAlarmRepository: PushAlarmRepository

    private val isPushEnabled = true // TODO 사용자 정보 가져오기

    override fun onNewToken(token: String) {
        if (isPushEnabled) {
            scope.launch {
                pushAlarmRepository.apply {
                    saveFcmToken(token)
                    registerPushToken(token)
                }
            }
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return
        }

        if (!isPushEnabled) return

        val notification = remoteMessage.notification ?: return

        val builder = applicationContext.defaultNotification()
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

package com.pomonyang.mohanyang.di

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.pomonyang.mohanyang.R
import com.pomonyang.mohanyang.presentation.di.PomodoroNotification
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.POMODORO_NOTIFICATION_CHANNEL_ID
import com.pomonyang.mohanyang.presentation.screen.PomodoroConstants.POMODORO_NOTIFICATION_ID
import com.pomonyang.mohanyang.ui.ServiceHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
internal object ServiceModule {

    @Provides
    @PomodoroNotification
    @ServiceScoped
    fun provideNotificationBuilder(
        @ApplicationContext context: Context
    ): NotificationCompat.Builder = NotificationCompat.Builder(context, POMODORO_NOTIFICATION_CHANNEL_ID)
        .setContentTitle(context.getString(R.string.app_name))
        .setSmallIcon(R.drawable.ic_app_notification)
        .setVibrate(null)
        .setOngoing(true)
        .setContentIntent(ServiceHelper.clickPendingIntent(context, POMODORO_NOTIFICATION_ID))

    @Provides
    @ServiceScoped
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
}

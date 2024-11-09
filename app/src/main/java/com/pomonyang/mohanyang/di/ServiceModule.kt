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
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object ServiceModule {

    @Provides
    @PomodoroNotification
    @Singleton
    fun provideNotificationBuilder(
        @ApplicationContext context: Context
    ): NotificationCompat.Builder = NotificationCompat.Builder(context, POMODORO_NOTIFICATION_CHANNEL_ID)
        .setContentTitle(context.getString(R.string.app_name))
        .setSmallIcon(R.drawable.ic_app_notification)
        .setVibrate(null)
        .setDefaults(0)
        .setOngoing(true)
        .setContentIntent(ServiceHelper.clickPendingIntent(context, POMODORO_NOTIFICATION_ID))

    @Provides
    @Singleton
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
}

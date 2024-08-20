package com.pomonyang.mohanyang.di

import android.app.AlarmManager
import android.content.Context
import com.pomonyang.mohanyang.notification.MnAlarmManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal object ManagerModule {
    @Provides
    internal fun provideMnAlarmManager(
        @ApplicationContext context: Context
    ): MnAlarmManager {
        val alarmManager =
            context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        return MnAlarmManager(context, alarmManager)
    }
}

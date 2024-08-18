package com.pomonyang.mohanyang.data.repository.pomodoro

import com.pomonyang.mohanyang.data.local.room.dao.PomodoroTimerDao
import com.pomonyang.mohanyang.data.local.room.enitity.PomodoroTimerEntity
import com.pomonyang.mohanyang.data.remote.model.request.PomodoroTimerRequest
import com.pomonyang.mohanyang.data.remote.service.MohaNyangService
import java.time.Duration
import java.time.Instant
import java.time.format.DateTimeFormatter
import javax.inject.Inject

internal class PomodoroTimerRepositoryImpl @Inject constructor(
    private val pomodoroTimerDao: PomodoroTimerDao,
    private val mohaNyangService: MohaNyangService
) : PomodoroTimerRepository {

    override suspend fun insertPomodoroTimerInitData(categoryNo: Int) {
        pomodoroTimerDao.insertPomodoroSettingData(
            PomodoroTimerEntity(
                categoryNo = categoryNo
            )
        )
    }

    override suspend fun updateFocusTime(focusedTime: Int) {
        pomodoroTimerDao.updateFocusedTime(focusedTime)
    }

    override suspend fun updateRestedTime(restedTime: Int) {
        pomodoroTimerDao.updateRestedTime(restedTime)
    }

    override suspend fun updatePomodoroDone() {
        pomodoroTimerDao.updateDoneAt(DateTimeFormatter.ISO_INSTANT.format(Instant.now()))
    }

    override suspend fun savePomodoroCacheData() {
        val pomodoroList = pomodoroTimerDao.getAllPomodoroTimers()
            .filter { it.focusedTime >= 60 }
            .map { pomodoro ->
                PomodoroTimerRequest(
                    clientFocusTimeId = pomodoro.focusTimeId,
                    categoryNo = pomodoro.categoryNo,
                    focusedTime = pomodoro.focusedTime.toDurationString(),
                    restedTime = pomodoro.restedTime.toDurationString(),
                    doneAt = pomodoro.doneAt
                )
            }

        mohaNyangService.saveFocusTime(pomodoroList).onSuccess {
            pomodoroTimerDao.deletePomodoroTimerAll()
        }
    }

    private fun Int.toDurationString(): String = Duration.ofMinutes((this / 60).toLong()).toString()
}
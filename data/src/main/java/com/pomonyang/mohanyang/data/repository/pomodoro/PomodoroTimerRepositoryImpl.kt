package com.pomonyang.mohanyang.data.repository.pomodoro

import com.pomonyang.mohanyang.data.local.room.dao.PomodoroTimerDao
import com.pomonyang.mohanyang.data.local.room.enitity.PomodoroTimerEntity
import com.pomonyang.mohanyang.data.remote.model.request.PomodoroTimerRequest
import com.pomonyang.mohanyang.data.remote.service.MohaNyangService
import java.time.Duration
import java.time.Instant
import java.time.format.DateTimeFormatter
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull

internal class PomodoroTimerRepositoryImpl @Inject constructor(
    private val pomodoroTimerDao: PomodoroTimerDao,
    private val mohaNyangService: MohaNyangService
) : PomodoroTimerRepository {

    override suspend fun insertPomodoroTimerInitData(categoryNo: Int, pomodoroTimerId: String) {
        pomodoroTimerDao.insertPomodoroSettingData(
            PomodoroTimerEntity(
                focusTimeId = pomodoroTimerId,
                categoryNo = categoryNo
            )
        )
    }

    override suspend fun incrementFocusedTime() {
        pomodoroTimerDao.incrementFocusedTime()
    }

    override suspend fun incrementRestedTime() {
        pomodoroTimerDao.incrementRestTime()
    }

    override suspend fun updatePomodoroDone(pomodoroTimerId: String) {
        pomodoroTimerDao.updateDoneAt(DateTimeFormatter.ISO_INSTANT.format(Instant.now()), pomodoroTimerId)
    }

    override suspend fun updateRecentPomodoroDone() {
        pomodoroTimerDao.updateDoneAtRecentPomodoro(DateTimeFormatter.ISO_INSTANT.format(Instant.now()))
    }

    override suspend fun savePomodoroData(pomodoroTimerId: String) {
        val pomodoro = pomodoroTimerDao.getTimer(pomodoroTimerId).firstOrNull()
        pomodoro?.let {
            mohaNyangService.saveFocusTime(
                listOf(
                    PomodoroTimerRequest(
                        clientFocusTimeId = pomodoro.focusTimeId,
                        categoryNo = pomodoro.categoryNo,
                        focusedTime = pomodoro.focusedTime.toDurationString(),
                        restedTime = pomodoro.restedTime.toDurationString(),
                        doneAt = pomodoro.doneAt
                    )
                )
            ).onSuccess {
                pomodoroTimerDao.deletePomodoroTimer(pomodoroTimerId)
            }
        }
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

    override fun getPomodoroTimer(timerId: String): Flow<PomodoroTimerEntity?> = pomodoroTimerDao.getTimer(timerId)

    private fun Int.toDurationString(): String = Duration.ofMinutes((this / 60).toLong()).toString()
}

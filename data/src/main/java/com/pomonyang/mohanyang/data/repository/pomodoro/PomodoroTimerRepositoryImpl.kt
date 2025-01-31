package com.pomonyang.mohanyang.data.repository.pomodoro

import com.pomonyang.mohanyang.data.local.room.dao.PomodoroTimerDao
import com.pomonyang.mohanyang.data.local.room.enitity.PomodoroTimerEntity
import com.pomonyang.mohanyang.data.local.room.enitity.toRequestModel
import com.pomonyang.mohanyang.data.remote.service.MohaNyangService
import com.pomonyang.mohanyang.data.repository.util.getCurrentIsoInstant
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import timber.log.Timber

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

    override suspend fun incrementFocusedTime(pomodoroTimerId: String) {
        pomodoroTimerDao.incrementFocusedTime(pomodoroTimerId)
    }

    override suspend fun incrementRestedTime(pomodoroTimerId: String) {
        pomodoroTimerDao.incrementRestTime(pomodoroTimerId)
    }

    override suspend fun updatePomodoroDone(pomodoroTimerId: String) {
        pomodoroTimerDao.updateDoneAtPomodoro(getCurrentIsoInstant(), pomodoroTimerId)
    }

    override suspend fun updateRecentPomodoroDone() {
        pomodoroTimerDao.updateDoneAtRecentPomodoro(getCurrentIsoInstant())
    }

    override suspend fun savePomodoroData(pomodoroTimerId: String) {
        pomodoroTimerDao.updateDoneAt(getCurrentIsoInstant(), pomodoroTimerId)

        pomodoroTimerDao.getTimer(pomodoroTimerId)
            .firstOrNull()
            ?.also { Timber.tag(TAG).d("savePomodoroData > $it") }
            ?.let { pomodoro ->
                when {
                    pomodoro.focusedTime >= 60 -> mohaNyangService.saveFocusTime(
                        listOf(pomodoro.toRequestModel())
                    ).onSuccess {
                        pomodoroTimerDao.deletePomodoroTimer(pomodoroTimerId)
                    }
                    else -> pomodoroTimerDao.deletePomodoroTimer(pomodoroTimerId)
                }
            }
    }


    override suspend fun savePomodoroCacheData() {
        pomodoroTimerDao.getAllPomodoroTimers()
            .partition { it.focusedTime >= 60 }
            .let { (validPomodoros, invalidPomodoros) ->
                invalidPomodoros
                    .map { it.focusTimeId }
                    .forEach {
                        Timber.tag(TAG).d("deletePomodoroTimer min 60s > $it")
                        pomodoroTimerDao.deletePomodoroTimer(it)
                    }

                validPomodoros
                    .map { it.toRequestModel() }
                    .let { pomodoroList ->
                        Timber.tag(TAG).d("savePomodoroCacheData > $pomodoroList")
                        mohaNyangService.saveFocusTime(pomodoroList)
                            .onSuccess { pomodoroTimerDao.deletePomodoroTimerAll() }
                            .onFailure {
                                Timber.tag(TAG).d("savePomodoroCacheData onFailure> $it")
                            }
                    }
            }
    }


    override fun getPomodoroTimer(timerId: String): Flow<PomodoroTimerEntity?> = pomodoroTimerDao.getTimer(timerId)

    companion object {
        private const val TAG = "PomodoroTimerRepository"
    }
}

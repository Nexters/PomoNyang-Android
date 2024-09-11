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

    override suspend fun incrementFocusedTime() {
        pomodoroTimerDao.incrementFocusedTime()
    }

    override suspend fun incrementRestedTime() {
        pomodoroTimerDao.incrementRestTime()
    }

    override suspend fun updateRecentPomodoroDone() {
        pomodoroTimerDao.updateDoneAtRecentPomodoro(getCurrentIsoInstant())
    }

    override suspend fun savePomodoroData(pomodoroTimerId: String) {
        pomodoroTimerDao.updateDoneAt(getCurrentIsoInstant(), pomodoroTimerId)
        val pomodoro = pomodoroTimerDao.getTimer(pomodoroTimerId).firstOrNull()
        Timber.tag(TAG).d("savePomodoroData > $pomodoro")
        pomodoro?.let {
            mohaNyangService.saveFocusTime(
                listOf(it.toRequestModel())
            ).onSuccess {
                pomodoroTimerDao.deletePomodoroTimer(pomodoroTimerId)
            }
        }
    }

    override suspend fun savePomodoroCacheData() {
        val pomodoroList = pomodoroTimerDao.getAllPomodoroTimers()
            .filter { it.focusedTime >= 60 }
            .map { pomodoro -> pomodoro.toRequestModel() }
        Timber.tag(TAG).d("savePomodoroCacheData > $pomodoroList")
        mohaNyangService.saveFocusTime(pomodoroList).onSuccess {
            pomodoroTimerDao.deletePomodoroTimerAll()
        }.onFailure {
            Timber.tag(TAG).d("savePomodoroCacheData onFailure> $it")
        }
    }

    override fun getPomodoroTimer(timerId: String): Flow<PomodoroTimerEntity?> = pomodoroTimerDao.getTimer(timerId)

    companion object {
        private const val TAG = "PomodoroTimerRepository"
    }
}

package com.practicum.habittracker.data.repository

import com.practicum.habittracker.data.dao.PomodoroDao
import com.practicum.habittracker.data.model.PomodoroStateEntity
import com.practicum.habittracker.domain.model.PomodoroMode
import com.practicum.habittracker.domain.model.PomodoroState
import com.practicum.habittracker.domain.repository.PomodoroRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PomodoroRepositoryImpl(
    private val pomodoroDao: PomodoroDao
) : PomodoroRepository {

    override fun getPomodoroState(): Flow<PomodoroState?> =
        pomodoroDao.getPomodoroState().map { entity ->
            entity?.let {
                PomodoroState(
                    timeLeft = it.timeLeft,
                    isRunning = it.isRunning,
                    mode = PomodoroMode.valueOf(it.mode)
                )
            }
        }

    override suspend fun savePomodoroState(state: PomodoroState) {
        pomodoroDao.insertPomodoroState(
            PomodoroStateEntity(
                id = 0,
                timeLeft = state.timeLeft,
                isRunning = state.isRunning,
                mode = state.mode.name
            )
        )
    }
}
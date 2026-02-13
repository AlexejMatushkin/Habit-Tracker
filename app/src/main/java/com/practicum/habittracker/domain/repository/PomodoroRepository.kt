package com.practicum.habittracker.domain.repository

import com.practicum.habittracker.domain.model.PomodoroState
import kotlinx.coroutines.flow.Flow

interface PomodoroRepository {
    fun getPomodoroState(): Flow<PomodoroState?>
    suspend fun savePomodoroState(state: PomodoroState)
}

package com.practicum.habittracker.domain.repository

import com.practicum.habittracker.domain.model.HabitCompletion
import kotlinx.coroutines.flow.Flow

interface HabitCompletionRepository {
    suspend fun insert(completion: HabitCompletion)
    fun getCompletions(habitId: Long, since: Long): Flow<List<HabitCompletion>>
    fun getAllCompletions(habitId: Long): Flow<List<HabitCompletion>>
}

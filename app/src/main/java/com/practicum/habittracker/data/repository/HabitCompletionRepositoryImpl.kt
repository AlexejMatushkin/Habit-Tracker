package com.practicum.habittracker.data.repository

import com.practicum.habittracker.data.dao.HabitCompletionDao
import com.practicum.habittracker.data.model.HabitCompletionEntity
import com.practicum.habittracker.domain.model.HabitCompletion
import com.practicum.habittracker.domain.repository.HabitCompletionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HabitCompletionRepositoryImpl(
    private val habitCompletionDao: HabitCompletionDao
) : HabitCompletionRepository {

    override suspend fun insert(completion: HabitCompletion) {
        habitCompletionDao.insert(
            HabitCompletionEntity(
                habitId = completion.habitId,
                date = completion.date,
                completed = completion.completed
            )
        )
    }

    override fun getCompletions(habitId: Long, since: Long): Flow<List<HabitCompletion>> =
        habitCompletionDao.getCompletions(habitId, since).map { entities ->
            entities.map { entity ->
                HabitCompletion(
                    habitId = entity.habitId,
                    date = entity.date,
                    completed = entity.completed
                )
            }
        }
}

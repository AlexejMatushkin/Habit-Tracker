package com.practicum.habittracker.data.repository

import com.practicum.habittracker.data.dao.HabitDao
import com.practicum.habittracker.data.model.HabitMapper
import com.practicum.habittracker.domain.model.Habit
import com.practicum.habittracker.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HabitRepositoryImpl(
    private val habitDao: HabitDao,
    private val mapper: HabitMapper
) : HabitRepository {
    override fun getAllHabits(): Flow<List<Habit>> =
        habitDao.getAllHabits().map { entities -> entities.map(mapper::toDomain) }

    override suspend fun addHabit(habit: Habit) {
        habitDao.insertHabit(mapper.toEntity(habit))
    }

    override suspend fun deleteHabit(habit: Habit) {
        habitDao.deleteHabit(mapper.toEntity(habit))
    }

    override suspend fun updateHabit(habit: Habit) {
        habitDao.updateHabit(mapper.toEntity(habit))
    }
}
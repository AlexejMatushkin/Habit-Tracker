package com.practicum.habittracker.data.di

import com.practicum.habittracker.data.dao.HabitDao
import com.practicum.habittracker.data.dao.PomodoroDao
import com.practicum.habittracker.data.model.HabitMapper
import com.practicum.habittracker.data.repository.HabitRepositoryImpl
import com.practicum.habittracker.data.repository.PomodoroRepositoryImpl
import com.practicum.habittracker.domain.repository.HabitRepository
import com.practicum.habittracker.domain.repository.PomodoroRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideHabitMapper(): HabitMapper = HabitMapper

    @Provides
    @Singleton
    fun provideHabitRepository(
        habitDao: HabitDao,
        mapper: HabitMapper
    ): HabitRepository = HabitRepositoryImpl(habitDao, mapper)

    @Provides
    @Singleton
    fun providePomodoroRepository(pomodoroDao: PomodoroDao): PomodoroRepository =
        PomodoroRepositoryImpl(pomodoroDao)
}

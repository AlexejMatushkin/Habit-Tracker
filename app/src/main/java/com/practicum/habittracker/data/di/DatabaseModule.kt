package com.practicum.habittracker.data.di

import android.content.Context
import androidx.room.Room
import com.practicum.habittracker.data.dao.HabitCompletionDao
import com.practicum.habittracker.data.dao.HabitDao
import com.practicum.habittracker.data.dao.PomodoroDao
import com.practicum.habittracker.data.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "habit_tracker.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideHabitDao(database: AppDatabase): HabitDao = database.habitDao()

    @Provides
    @Singleton
    fun providePomodoroDao(database: AppDatabase): PomodoroDao = database.pomodoroDao()

    @Provides
    @Singleton
    fun provideHabitCompletionDao(database: AppDatabase): HabitCompletionDao =
        database.habitCompletionDao()
}

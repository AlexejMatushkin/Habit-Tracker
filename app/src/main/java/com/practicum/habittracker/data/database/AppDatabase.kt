package com.practicum.habittracker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.practicum.habittracker.data.converter.DateListConverter
import com.practicum.habittracker.data.converter.PomodoroModeConverter
import com.practicum.habittracker.data.dao.HabitCompletionDao
import com.practicum.habittracker.data.dao.HabitDao
import com.practicum.habittracker.data.dao.PomodoroDao
import com.practicum.habittracker.data.model.HabitCompletionEntity
import com.practicum.habittracker.data.model.HabitEntity
import com.practicum.habittracker.data.model.PomodoroStateEntity

@Database(
    entities = [
        HabitEntity::class,
        PomodoroStateEntity::class,
        HabitCompletionEntity::class
    ],
    version = 4,
    exportSchema = false
)
@TypeConverters(
    DateListConverter::class,
    PomodoroModeConverter::class
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun pomodoroDao(): PomodoroDao
    abstract fun habitCompletionDao(): HabitCompletionDao
}

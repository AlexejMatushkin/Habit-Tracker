package com.practicum.habittracker.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.habittracker.data.dao.HabitDao
import com.practicum.habittracker.data.model.HabitEntity

@Database(
    entities = [HabitEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
}

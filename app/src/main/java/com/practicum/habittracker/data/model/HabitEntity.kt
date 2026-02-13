package com.practicum.habittracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val completedDates: List<Long>,
    val reminderEnabled: Boolean,
    val reminderTime: Long
)

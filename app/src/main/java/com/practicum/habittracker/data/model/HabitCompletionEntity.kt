package com.practicum.habittracker.data.model

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "habit_completions",
    primaryKeys = ["habitId", "date"],
    indices = [Index(value = ["date"])]
)
data class HabitCompletionEntity(
    val habitId: Long,
    val date: Long,
    val completed: Boolean
)

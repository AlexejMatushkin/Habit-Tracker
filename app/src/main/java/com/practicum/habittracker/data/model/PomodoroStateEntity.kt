package com.practicum.habittracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pomodoro_state")
data class PomodoroStateEntity(
    @PrimaryKey val id: Int = 0,
    val timeLeft: Int,
    val isRunning: Boolean,
    val mode: String
)

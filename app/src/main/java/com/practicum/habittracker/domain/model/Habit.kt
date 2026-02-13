package com.practicum.habittracker.domain.model

data class Habit(
    val id: Long,
    val title: String,
    val completedDates: List<Long> = emptyList(),
    val reminderEnabled: Boolean = false,
    val reminderTime: Long = 0L
)
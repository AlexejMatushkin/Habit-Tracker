package com.practicum.habittracker.domain.model

data class HabitCompletion(
    val habitId: Long,
    val date: Long,
    val completed: Boolean
)

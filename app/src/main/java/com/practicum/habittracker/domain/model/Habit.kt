package com.practicum.habittracker.domain.model

data class Habit(
    val id: Long,
    val title: String,
    val isCompleted: Boolean,
    val completedDates: List<Long> = emptyList(),
    val reminderEnabled: Boolean = false,
    val reminderTime: Long = 0L
) {

    companion object {

        val DEFAULT_HABITS = listOf(
            Habit(1, "Пить воду", false),
            Habit(2, "Прогулка", false),
            Habit(3, "Без телефона", false)
        )
    }
}
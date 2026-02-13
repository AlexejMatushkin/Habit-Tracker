package com.practicum.habittracker.domain.model

data class PomodoroState(
    val timeLeft: Int,
    val isRunning: Boolean,
    val mode: PomodoroMode
)

enum class PomodoroMode(val label: String) {
    WORK("Работа"), BREAK("Перерыв")
}

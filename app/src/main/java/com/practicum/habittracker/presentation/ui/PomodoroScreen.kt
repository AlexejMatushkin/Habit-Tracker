package com.practicum.habittracker.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
fun PomodoroScreen(
    onNavigateToHabits: () -> Unit,
    modifier: Modifier = Modifier
) {
    var timeLeft by remember { mutableIntStateOf(25 * 60) }
    var isRunning by remember { mutableStateOf(false) }
    var mode by remember { mutableStateOf(PomodoroMode.WORK) }

    LaunchedEffect(isRunning, mode) {
        if (isRunning) {
            while (timeLeft > 0 && isActive) {
                delay(1000)
                timeLeft--
            }
            if (timeLeft <= 0 && isActive) {
                // Переключаем режим
                mode = if (mode == PomodoroMode.WORK) PomodoroMode.BREAK else PomodoroMode.WORK
                timeLeft = if (mode == PomodoroMode.WORK) 25 * 60 else 5 * 60
                // Таймер продолжает идти автоматически
            }
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Text(
            text = "${timeLeft / 60}:${String.format("%02d", timeLeft % 60)}",
            style = MaterialTheme.typography.displayLarge
        )
        Text(text = mode.label, style = MaterialTheme.typography.titleMedium)
        Row {
            Button(onClick = { isRunning = !isRunning }) {
                Text(if (isRunning) "Пауза" else "Старт")
            }
            Button(onClick = {
                timeLeft = if (mode == PomodoroMode.WORK) 25 * 60 else 5 * 60
                isRunning = false
            }) {
                Text("Сброс")
            }
        }

        Button(onClick = onNavigateToHabits) {
            Text("К привычкам")
        }
    }
}

enum class PomodoroMode(val label: String) {
    WORK("Работа"), BREAK("Перерыв")
}

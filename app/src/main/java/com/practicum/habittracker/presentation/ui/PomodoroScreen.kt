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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.practicum.habittracker.domain.model.PomodoroMode
import com.practicum.habittracker.presentation.viewmodel.PomodoroViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive

@Composable
fun PomodoroScreen(
    viewModel: PomodoroViewModel,
    onNavigateToHabits: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()

    state?.let { s ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text("${s.timeLeft / 60}:${String.format("%02d", s.timeLeft % 60)}")
            Text(s.mode.label)

            Row {
                Button(onClick = { viewModel.toggleTimer() }) {
                    Text(if (s.isRunning) "Пауза" else "Старт")
                }
                Button(onClick = { viewModel.resetTimer() }) {
                    Text("Сброс")
                }
            }

            Button(onClick = onNavigateToHabits) {
                Text("К привычкам")
            }
        }
    }
}

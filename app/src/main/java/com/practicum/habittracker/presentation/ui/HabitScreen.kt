package com.practicum.habittracker.presentation.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.practicum.habittracker.domain.model.Habit
import com.practicum.habittracker.presentation.viewmodel.HabitViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HabitScreen(
    onNavigateToPomodoro: () -> Unit,
    viewModel: HabitViewModel = hiltViewModel()
) {
    val habits by viewModel.habits
    var text by remember { mutableStateOf("") }
    var habitToDelete by remember { mutableStateOf<Habit?>(null) }

    Column {
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(habits, key = { it.id }) { habit ->

                val weeklyCompletions by viewModel.getWeeklyCompletions(habit.id).collectAsState()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .combinedClickable(
                            onClick = {
                                viewModel.toggleHabit(habit.id)
                            },
                            onLongClick = {
                                habitToDelete = habit
                            }
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = habit.isCompleted,
                        onCheckedChange = null
                    )
                    Column {
                        Text(text = habit.title)
                        WeeklyStreak(
                            completions = weeklyCompletions,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }

        TextField(
            value = text,
            onValueChange = { newText ->
                text = newText
            },
            label = { Text("Введите название") }
        )

        Button(
            onClick = {
                viewModel.addHabit(text)
                text = ""
            }
        ) {
            Text("Добавить")
        }

        Button(onClick = onNavigateToPomodoro) {
            Text("Pomodoro-таймер")
        }
    }

    // Диалог удаления
    habitToDelete?.let { habit ->
        AlertDialog(
            onDismissRequest = { habitToDelete = null },
            title = { Text("Удалить привычку?") },
            text = { Text("Вы уверены, что хотите удалить «${habit.title}»?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteHabit(habit.id)
                        habitToDelete = null
                    }
                ) {
                    Text("Да")
                }
            },
            dismissButton = {
                TextButton(onClick = { habitToDelete = null }) {
                    Text("Нет")
                }
            }
        )
    }
}

@Composable
fun WeeklyStreak(completions: List<Boolean>, modifier: Modifier = Modifier) {
    Row(modifier = modifier, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        completions.forEach { completed ->
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(
                        color = if (completed) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outline,
                        shape = CircleShape
                    )
            )
        }
    }
}

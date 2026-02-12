package com.practicum.habittracker

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.practicum.habittracker.presentation.viewmodel.HabitViewModel
import com.practicum.habittracker.presentation.viewmodel.HabitViewModelFactory

@Composable
fun HabitScreen(
    viewModel: HabitViewModel,
    onNavigateToPomodoro: () -> Unit
) {

    val habits by viewModel.habits
    var text by remember { mutableStateOf("") }

    Column {
        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(habits, key = { it.id }) { habit ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = habit.isCompleted,
                        onCheckedChange = {
                            viewModel.toggleHabit(habit.id)
                        }
                    )
                    Text(text = habit.title)

                }
            }
        }

        TextField(
            value = text,
            onValueChange = { newText ->
                text = newText
            },
            label = {
                Text("Введите название")
            },
            placeholder = {
                Text("Название не может быть пустым")
            }
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

}

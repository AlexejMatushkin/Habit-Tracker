package com.practicum.habittracker.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.practicum.habittracker.domain.model.Habit

@Composable
fun HabitActionsDialog(
    habit: Habit,
    onDismiss: () -> Unit,
    onDelete: () -> Unit,
    onReminderToggled: (Boolean, Long) -> Unit,
    onViewStatistics: () -> Unit
) {
    var reminderEnabled by remember { mutableStateOf(habit.reminderEnabled) }
    var showTimePicker by remember { mutableStateOf(false) }
    var selectedTime by remember {
        mutableStateOf(
            if (habit.reminderTime > 0) habit.reminderTime else 20 * 60 * 60 * 1000L
        )
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Действия с «${habit.title}»") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                // Напоминание
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Напоминание")
                    Switch(
                        checked = reminderEnabled,
                        onCheckedChange = {
                            reminderEnabled = it
                            if (it) {
                                showTimePicker = true
                            } else {
                                // Не сохраняем сразу — ждём "Готово"
                            }
                        }
                    )
                }

                if (reminderEnabled && !showTimePicker) {
                    val timeStr = formatTime(selectedTime)
                    Text("Время: $timeStr", color = MaterialTheme.colorScheme.primary)
                }

                // Статистика
                TextButton(
                    onClick = {
                        onViewStatistics()
                        onDismiss()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("📊 Статистика", textAlign = TextAlign.Start)
                }
            }
        },
        confirmButton = {
            // Кнопка "Готово" только если включено напоминание
            if (reminderEnabled) {
                TextButton(
                    onClick = {
                        onReminderToggled(true, selectedTime)
                        onDismiss()
                    }
                ) {
                    Text("Готово")
                }
            } else {
                // Если напоминание выключено — просто закрываем
                TextButton(onClick = onDismiss) {
                    Text("Закрыть")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDelete) {
                Text("🗑️ Удалить", color = MaterialTheme.colorScheme.error)
            }
        }
    )

    // TimePicker
    if (showTimePicker) {
        val initialHour = (selectedTime / (60 * 60 * 1000L)).toInt()
        val initialMinute = (selectedTime % (60 * 60 * 1000L) / (60 * 1000L)).toInt()

        TimePickerDialog(
            onDismissRequest = { showTimePicker = false },
            onTimeSelected = { hour, minute ->
                selectedTime = (hour * 60 * 60 + minute * 60).toLong() * 1000L
                showTimePicker = false
            },
            initialHour = initialHour,
            initialMinute = initialMinute
        )
    }
}

@Composable
private fun formatTime(timeInMillis: Long): String {
    val hours = (timeInMillis / (60 * 60 * 1000L)).toInt()
    val minutes = (timeInMillis % (60 * 60 * 1000L) / (60 * 1000L)).toInt()
    return String.format("%02d:%02d", hours, minutes)
}
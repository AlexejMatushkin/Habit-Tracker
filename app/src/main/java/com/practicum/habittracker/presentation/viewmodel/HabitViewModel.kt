package com.practicum.habittracker.presentation.viewmodel

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.practicum.habittracker.domain.model.Habit
import com.practicum.habittracker.domain.model.HabitCompletion
import com.practicum.habittracker.domain.repository.HabitCompletionRepository
import com.practicum.habittracker.domain.repository.HabitRepository
import com.practicum.habittracker.worker.ReminderWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val repository: HabitRepository,
    private val habitCompletionRepository: HabitCompletionRepository,
    @param:ApplicationContext private val context: Context
) : ViewModel() {

    private val _habits = mutableStateOf<List<Habit>>(emptyList())

    val habits: State<List<Habit>> = _habits

    init {
        repository.getAllHabits()
            .onEach { habitList -> _habits.value = habitList }
            .launchIn(viewModelScope)
    }

    private val weeklyCompletionsMap = mutableMapOf<Long, StateFlow<List<Boolean>>>()

    fun getWeeklyCompletions(habitId: Long): StateFlow<List<Boolean>> {
        return weeklyCompletionsMap.getOrPut(habitId) {
            val flow = habitCompletionRepository
                .getCompletions(habitId, getStartOfDay(System.currentTimeMillis()) - 7 * 24 * 60 * 60 * 1000L)
                .map { completions ->
                    val completionMap = completions.associateBy { it.date }

                    val today = Calendar.getInstance().apply {
                        timeInMillis = System.currentTimeMillis()
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }

                    val daysToSubtract = when (val dayOfWeek = today.get(Calendar.DAY_OF_WEEK)) {
                        Calendar.SUNDAY -> 6
                        else -> dayOfWeek - Calendar.MONDAY
                    }
                    today.add(Calendar.DAY_OF_YEAR, -daysToSubtract)

                    val weekDays = (0 until 7).map { i ->
                        val dayCal = Calendar.getInstance().apply {
                            timeInMillis = today.timeInMillis
                            add(Calendar.DAY_OF_YEAR, i)
                        }
                        val dayStart = dayCal.timeInMillis
                        completionMap[dayStart]?.completed ?: false
                    }

                    weekDays
                }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(5000),
                    initialValue = List(7) { false }
                )
            flow
        }
    }

    fun toggleHabit(id: Long) {
        val today = getStartOfDay(System.currentTimeMillis())
        val current = _habits.value.find { it.id == id } ?: return
        val wasCompleted = current.completedDates.contains(today)
        val newCompleted = !wasCompleted

        val updatedDates = if (newCompleted) {
            current.completedDates + today
        } else {
            current.completedDates.filter { it != today }
        }

        val updatedHabit = current.copy(completedDates = updatedDates)
        _habits.value = _habits.value.map { if (it.id == id) updatedHabit else it }

        viewModelScope.launch {
            repository.updateHabit(updatedHabit)
            habitCompletionRepository.insert(HabitCompletion(id, today, newCompleted))
        }
    }

    fun addHabit(title: String) {
        if (title.isBlank()) return
        val newHabit = Habit(
            id = (_habits.value.maxOfOrNull { it.id }?.inc() ?: 1L),
            title = title.trim()
        )
        viewModelScope.launch {
            repository.addHabit(newHabit)
        }
    }

    fun deleteHabit(id: Long) {
        val habitToDelete = _habits.value.find { it.id == id } ?: return
        viewModelScope.launch {
            repository.deleteHabit(habitToDelete)
        }
    }

    private fun getStartOfDay(timestamp: Long): Long {
        val cal = Calendar.getInstance().apply {
            timeInMillis = timestamp
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }
        return cal.timeInMillis
    }

    fun setReminder(habitId: Long, enabled: Boolean, timeInMillis: Long) {
        val habit = _habits.value.find { it.id == habitId } ?: return
        val updated = habit.copy(
            reminderEnabled = enabled,
            reminderTime = if (enabled) timeInMillis else 0L
        )
        _habits.value = _habits.value.map { if (it.id == habitId) updated else it }

        viewModelScope.launch {
            repository.updateHabit(updated)
            if (enabled) {
                scheduleReminder(habitId, updated.title, timeInMillis)
            } else {
                cancelReminder(habitId)
            }
        }
    }

    private fun scheduleReminder(habitId: Long, title: String, timeInMillis: Long) {

        WorkManager.getInstance(context).cancelUniqueWork("reminder_$habitId")

        val now = System.currentTimeMillis()
        val todayStart = getStartOfDay(now)
        var firstRun = todayStart + timeInMillis
        if (firstRun <= now) firstRun += 24 * 60 * 60 * 1000L

        val data = Data.Builder()
            .putLong("habitId", habitId)
            .putString("title", title)
            .build()

        val request = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInputData(data)
            .setInitialDelay(firstRun - now, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "reminder_$habitId",
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    private fun cancelReminder(habitId: Long) {
        WorkManager.getInstance(context).cancelUniqueWork("reminder_$habitId")
    }
}

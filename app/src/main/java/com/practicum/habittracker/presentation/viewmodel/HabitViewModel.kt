package com.practicum.habittracker.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.habittracker.domain.model.Habit
import com.practicum.habittracker.domain.model.HabitCompletion
import com.practicum.habittracker.domain.repository.HabitCompletionRepository
import com.practicum.habittracker.domain.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar
import javax.inject.Inject

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val repository: HabitRepository,
    private val habitCompletionRepository: HabitCompletionRepository
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
                    val today = getStartOfDay(System.currentTimeMillis())
                    val days = (0..6).map { i ->
                        val day = today - i * 24 * 60 * 60 * 1000L
                        completions.any { it.date == day && it.completed }
                    }.reversed()
                    days
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
        val current = _habits.value.find { it.id == id } ?: return
        val newCompleted = !current.isCompleted
        val updated = current.copy(isCompleted = newCompleted)

        _habits.value = _habits.value.map { if (it.id == id) updated else it }

        viewModelScope.launch {
            repository.updateHabit(updated)

            val todayStart = getStartOfDay(System.currentTimeMillis())
            habitCompletionRepository.insert(
                HabitCompletion(id, todayStart, newCompleted)
            )
        }
    }

    fun addHabit(title: String) {
        if (title.isBlank()) return
        val newHabit = Habit(
            id = (_habits.value.maxOfOrNull { it.id }?.inc() ?: 1L),
            title = title.trim(),
            isCompleted = false
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
}

package com.practicum.habittracker.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.habittracker.domain.model.Habit
import com.practicum.habittracker.domain.repository.HabitRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitViewModel @Inject constructor(
    private val repository: HabitRepository
) : ViewModel() {

    private val _habits = mutableStateOf<List<Habit>>(emptyList())

    val habits: State<List<Habit>> = _habits

    init {
        repository.getAllHabits()
            .onEach { habitList -> _habits.value = habitList }
            .launchIn(viewModelScope)
    }

    fun toggleHabit(id: Long) {
        val currentHabit = _habits.value.find { it.id == id } ?: return
        val updatedHabit = currentHabit.copy(isCompleted = !currentHabit.isCompleted)
        viewModelScope.launch {
            repository.updateHabit(updatedHabit)
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
}
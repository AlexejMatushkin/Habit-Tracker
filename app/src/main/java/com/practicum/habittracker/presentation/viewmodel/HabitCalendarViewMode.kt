package com.practicum.habittracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.habittracker.domain.model.HabitCompletion
import com.practicum.habittracker.domain.repository.HabitCompletionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HabitCalendarViewModel @Inject constructor(
    private val habitCompletionRepository: HabitCompletionRepository
) : ViewModel() {

    private val _completions = MutableStateFlow<List<HabitCompletion>>(emptyList())
    val completions: StateFlow<List<HabitCompletion>> = _completions

    fun loadCompletions(habitId: Long) {
        viewModelScope.launch {
            habitCompletionRepository.getAllCompletions(habitId).collect {
                _completions.value = it
            }
        }
    }
}

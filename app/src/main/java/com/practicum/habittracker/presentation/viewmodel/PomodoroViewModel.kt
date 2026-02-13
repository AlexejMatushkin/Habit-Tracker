package com.practicum.habittracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.habittracker.domain.model.PomodoroMode
import com.practicum.habittracker.domain.model.PomodoroState
import com.practicum.habittracker.domain.repository.PomodoroRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PomodoroViewModel @Inject constructor(
    private val repository: PomodoroRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<PomodoroState?>(null)
    val uiState: StateFlow<PomodoroState?> = _uiState

    private var timerJob: Job? = null

    init {
        viewModelScope.launch {
            repository.getPomodoroState().collect { state ->
                if (state != null) {
                    _uiState.value = state
                    if (state.isRunning) {
                        startTimer()
                    }
                } else {
                    val default = PomodoroState(25 * 60, false, PomodoroMode.WORK)
                    _uiState.value = default
                    saveState(default)
                }
            }
        }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(1000)
                val currentState = _uiState.value ?: break
                if (!currentState.isRunning || currentState.timeLeft <= 0) {
                    if (currentState.timeLeft <= 0) {
                        val newMode = if (currentState.mode == PomodoroMode.WORK)
                            PomodoroMode.BREAK else PomodoroMode.WORK
                        val newTime = if (newMode == PomodoroMode.WORK) 25 * 60 else 5 * 60
                        val newState = PomodoroState(newTime, true, newMode)
                        _uiState.value = newState
                        saveState(newState)
                    }
                    break
                } else {
                    val newState = currentState.copy(timeLeft = currentState.timeLeft - 1)
                    _uiState.value = newState
                    saveState(newState)
                }
            }
        }
    }

    fun toggleTimer() {
        val currentState = _uiState.value ?: return
        val newState = currentState.copy(isRunning = !currentState.isRunning)
        _uiState.value = newState
        saveState(newState)

        if (newState.isRunning) {
            startTimer()
        } else {
            timerJob?.cancel()
        }
    }

    fun resetTimer() {
        val currentState = _uiState.value ?: return
        val newTime = if (currentState.mode == PomodoroMode.WORK) 25 * 60 else 5 * 60
        val newState = currentState.copy(timeLeft = newTime, isRunning = false)
        _uiState.value = newState
        saveState(newState)
        timerJob?.cancel()
    }

    private fun saveState(state: PomodoroState) {
        viewModelScope.launch {
            repository.savePomodoroState(state)
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}

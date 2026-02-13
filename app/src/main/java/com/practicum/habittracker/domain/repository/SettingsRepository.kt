package com.practicum.habittracker.domain.repository

import kotlinx.coroutines.flow.Flow

interface SettingsRepository {
    suspend fun setThemeMode(mode: String)
    fun getThemeMode(): Flow<String>
}
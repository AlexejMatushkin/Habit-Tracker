package com.practicum.habittracker.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.practicum.habittracker.data.preferences.SettingsPreferences
import com.practicum.habittracker.domain.repository.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : SettingsRepository {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override suspend fun setThemeMode(mode: String) {
        context.dataStore.edit { prefs ->
            prefs[SettingsPreferences.THEME_MODE] = mode
        }
    }

    override fun getThemeMode(): Flow<String> = context.dataStore.data
        .map { prefs -> prefs[SettingsPreferences.THEME_MODE] ?: "system" }
}
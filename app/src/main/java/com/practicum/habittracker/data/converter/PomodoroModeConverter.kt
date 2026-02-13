package com.practicum.habittracker.data.converter

import androidx.room.TypeConverter
import com.practicum.habittracker.domain.model.PomodoroMode

class PomodoroModeConverter {
    @TypeConverter
    fun toMode(value: String) = PomodoroMode.valueOf(value)

    @TypeConverter
    fun fromMode(mode: PomodoroMode) = mode.name
}
package com.practicum.habittracker.data.converter

import androidx.room.TypeConverter

class DateListConverter {

    @TypeConverter
    fun fromTimestamps(value: String?): List<Long> {
        return value?.split(",")?.mapNotNull { it.toLongOrNull() } ?: emptyList()
    }

    @TypeConverter
    fun toTimestamps(list: List<Long>?): String? {
        return list?.joinToString(",")
    }
}

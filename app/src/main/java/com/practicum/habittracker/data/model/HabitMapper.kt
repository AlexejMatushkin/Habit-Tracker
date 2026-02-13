package com.practicum.habittracker.data.model

import com.practicum.habittracker.domain.model.Habit

object HabitMapper {
    fun toEntity(domain: Habit): HabitEntity = HabitEntity(
        id = domain.id,
        title = domain.title,
        completedDates = domain.completedDates,
        reminderEnabled = domain.reminderEnabled,
        reminderTime = domain.reminderTime
    )

    fun toDomain(entity: HabitEntity): Habit = Habit(
        id = entity.id,
        title = entity.title,
        completedDates = entity.completedDates,
        reminderEnabled = entity.reminderEnabled,
        reminderTime = entity.reminderTime
    )
}

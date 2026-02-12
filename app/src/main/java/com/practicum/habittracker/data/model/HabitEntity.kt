package com.practicum.habittracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Сущность базы данных, представляющая привычку в локальном хранилище.
 *
 * Используется Room для сохранения и извлечения данных о привычках из SQLite.
 *
 * @property id Уникальный идентификатор привычки. Также является первичным ключом в таблице.
 * @property title Название привычки (например, "Пить воду").
 * @property isCompleted Флаг, указывающий, выполнена ли привычка сегодня.
 */
@Entity(tableName = "habits")
data class HabitEntity(
    @PrimaryKey val id: Long,
    val title: String,
    val isCompleted: Boolean
)

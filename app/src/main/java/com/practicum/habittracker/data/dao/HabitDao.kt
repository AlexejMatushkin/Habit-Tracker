package com.practicum.habittracker.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.practicum.habittracker.data.model.HabitEntity
import kotlinx.coroutines.flow.Flow

/**
 * Интерфейс доступа к данным (DAO) для работы с привычками в локальной базе данных.
 *
 * Обеспечивает абстракцию над операциями чтения и записи для сущности [HabitEntity].
 * Использует библиотеку Room для маппинга SQL-запросов в Kotlin-методы.
 *
 * Все методы помечены как `suspend`, за исключением [getAllHabits], который возвращает [Flow],
 * что позволяет реагировать на изменения данных в реальном времени.
 */
@Dao
interface HabitDao {

    /**
     * Получает список всех привычек, отсортированных по убыванию ID (новые — сверху).
     *
     * Возвращает [Flow], чтобы автоматически уведомлять подписчиков при любых изменениях в таблице `habits`.
     * Используется в репозитории и ViewModel для реактивного обновления UI.
     *
     * @return Flow<List<HabitEntity>> — поток списка привычек
     */
    @Query("SELECT * FROM habits ORDER BY id DESC")
    fun getAllHabits(): Flow<List<HabitEntity>>

    /**
     * Добавляет новую привычку в базу данных.
     *
     * Если привычка с таким же `id` уже существует, она будет заменена ([OnConflictStrategy.REPLACE]).
     * Операция асинхронная, должна вызываться в корутине.
     *
     * @param habit Объект [HabitEntity], который нужно вставить
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHabit(habit: HabitEntity)

    /**
     * Обновляет существующую привычку в базе данных.
     *
     * Находит привычку по `id` и заменяет её поля на новые значения.
     * Требует, чтобы привычка уже существовала (по первичному ключу).
     * Операция асинхронная.
     *
     * @param habit Объект [HabitEntity] с обновлёнными данными
     */
    @Update
    suspend fun updateHabit(habit: HabitEntity)

    /**
     * Удаляет указанную привычку из базы данных.
     *
     * Удаление происходит по первичному ключу (`id`).
     * Операция асинхронная.
     *
     * @param habit Объект [HabitEntity], который нужно удалить
     */
    @Delete
    suspend fun deleteHabit(habit: HabitEntity)
}
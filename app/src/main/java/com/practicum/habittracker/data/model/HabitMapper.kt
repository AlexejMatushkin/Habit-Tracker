package com.practicum.habittracker.data.model

import com.practicum.habittracker.domain.model.Habit

/**
 * Объект для преобразования данных между уровнями приложения:
 * - Из доменной модели [Habit] в сущность базы данных [HabitEntity]
 * - Из [HabitEntity] обратно в [Habit]
 *
 * Используется для соблюдения принципа разделения слоёв (clean architecture):
 * - UI работает с [Habit]
 * - База данных работает с [HabitEntity]
 * - [HabitMapper] обеспечивает безопасное и явное преобразование между ними
 */
object HabitMapper {

    /**
     * Преобразует доменную модель привычки в сущность базы данных.
     *
     * @param domain Экземпляр [Habit] из слоя домена
     * @return Новый экземпляр [HabitEntity], готовый к сохранению в БД
     */
    fun toEntity(domain: Habit): HabitEntity = HabitEntity(
        id = domain.id,
        title = domain.title,
        isCompleted = domain.isCompleted
    )

    /**
     * Преобразует сущность базы данных в доменную модель.
     *
     * Используется при загрузке данных из Room, чтобы передать их в UI.
     *
     * @param entity Экземпляр [HabitEntity], загруженный из базы данных
     * @return Новый экземпляр [Habit], пригодный для отображения в интерфейсе
     */
    fun toDomain(entity: HabitEntity): Habit = Habit(
        id = entity.id,
        title = entity.title,
        isCompleted = entity.isCompleted
    )
}

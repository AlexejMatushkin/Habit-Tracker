package com.practicum.habittracker.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.practicum.habittracker.data.model.PomodoroStateEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PomodoroDao {
    @Query("SELECT * FROM pomodoro_state LIMIT 1")
    fun getPomodoroState(): Flow<PomodoroStateEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPomodoroState(state: PomodoroStateEntity)

    @Update
    suspend fun updatePomodoroState(state: PomodoroStateEntity)
}

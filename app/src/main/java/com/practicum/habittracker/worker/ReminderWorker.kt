package com.practicum.habittracker.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.practicum.habittracker.util.NotificationHelper

class ReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val habitId = inputData.getLong("habitId", 0L)
        val title = inputData.getString("title") ?: "Привычка"

        // Показываем уведомление
        NotificationHelper.showReminder(
            context = applicationContext,
            habitId = habitId,
            title = title
        )

        return Result.success()
    }
}
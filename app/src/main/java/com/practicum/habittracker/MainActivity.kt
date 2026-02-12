package com.practicum.habittracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.practicum.habittracker.data.database.AppDatabase
import com.practicum.habittracker.data.model.HabitMapper
import com.practicum.habittracker.data.repository.HabitRepositoryImpl
import com.practicum.habittracker.presentation.navigation.Screens
import com.practicum.habittracker.presentation.ui.PomodoroScreen
import com.practicum.habittracker.presentation.viewmodel.HabitViewModel
import com.practicum.habittracker.presentation.viewmodel.HabitViewModelFactory
import com.practicum.habittracker.ui.theme.HabitTrackerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "habit_tracker.db"
        ).fallbackToDestructiveMigration()
            .build()

        val repository = HabitRepositoryImpl(database.habitDao(), HabitMapper)
        val habitViewModelFactory = HabitViewModelFactory(repository)

        setContent {
            HabitTrackerTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screens.HABITS
                ) {
                    composable(Screens.HABITS) {
                        val viewModel: HabitViewModel = viewModel(factory = habitViewModelFactory)
                        HabitScreen(
                            viewModel = viewModel,
                            onNavigateToPomodoro = { navController.navigate(Screens.POMODORO) }
                        )
                    }
                    composable(Screens.POMODORO) {
                        PomodoroScreen(
                            onNavigateToHabits = { navController.navigate(Screens.HABITS) }
                        )
                    }
                }
            }
        }
    }
}

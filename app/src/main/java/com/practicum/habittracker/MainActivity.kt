package com.practicum.habittracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.practicum.habittracker.presentation.navigation.Screens
import com.practicum.habittracker.presentation.ui.HabitScreen
import com.practicum.habittracker.presentation.ui.PomodoroScreen
import com.practicum.habittracker.presentation.viewmodel.PomodoroViewModel
import com.practicum.habittracker.ui.theme.HabitTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            HabitTrackerTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screens.HABITS
                ) {
                    composable(Screens.HABITS) {
                        HabitScreen(
                            onNavigateToPomodoro = { navController.navigate(Screens.POMODORO) }
                        )
                    }
                    composable(Screens.POMODORO) {
                        val viewModel: PomodoroViewModel = hiltViewModel()
                        PomodoroScreen(
                            viewModel = viewModel,
                            onNavigateToHabits = { navController.navigate(Screens.HABITS) }
                        )
                    }
                }
            }
        }
    }
}

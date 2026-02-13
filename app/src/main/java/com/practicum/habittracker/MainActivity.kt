package com.practicum.habittracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.practicum.habittracker.presentation.navigation.Screens
import com.practicum.habittracker.presentation.ui.HabitCalendarScreen
import com.practicum.habittracker.presentation.ui.HabitScreen
import com.practicum.habittracker.presentation.ui.NotificationPermissionHandler
import com.practicum.habittracker.presentation.ui.PomodoroScreen
import com.practicum.habittracker.presentation.ui.SettingsScreen
import com.practicum.habittracker.presentation.viewmodel.HabitCalendarViewModel
import com.practicum.habittracker.presentation.viewmodel.HabitViewModel
import com.practicum.habittracker.presentation.viewmodel.PomodoroViewModel
import com.practicum.habittracker.presentation.viewmodel.SettingsViewModel
import com.practicum.habittracker.ui.theme.HabitTrackerTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val settingsViewModel: SettingsViewModel = hiltViewModel()
            val themeMode by settingsViewModel.themeMode.collectAsState()
            HabitTrackerTheme(themeMode = themeMode) {
                val context = LocalContext.current
                NotificationPermissionHandler(context)

                val navController = rememberNavController()

                key(themeMode) {
                    NavHost(
                        navController = navController,
                        startDestination = Screens.HABITS,
                    ) {
                        composable(Screens.HABITS) {
                            val viewModel: HabitViewModel = hiltViewModel()
                            HabitScreen(
                                navController = navController,
                                onNavigateToPomodoro = { navController.navigate(Screens.POMODORO) },
                                viewModel = viewModel
                            )
                        }
                        composable(Screens.POMODORO) {
                            val viewModel: PomodoroViewModel = hiltViewModel()
                            PomodoroScreen(
                                onNavigateToHabits = { navController.navigate(Screens.HABITS) },
                                viewModel = viewModel,
                            )
                        }
                        composable(
                            route = "${Screens.HABIT_CALENDAR}/{habitId}?habitTitle={habitTitle}",
                            arguments = listOf(
                                navArgument("habitId") { type = NavType.LongType },
                                navArgument("habitTitle") {
                                    type = NavType.StringType; defaultValue = "Привычка"
                                }
                            )
                        ) { backStackEntry ->
                            val habitId = backStackEntry.arguments?.getLong("habitId") ?: 0L
                            val habitTitle =
                                backStackEntry.arguments?.getString("habitTitle") ?: "Привычка"

                            val viewModel: HabitCalendarViewModel = hiltViewModel()
                            viewModel.loadCompletions(habitId)

                            HabitCalendarScreen(
                                habitTitle = habitTitle,
                                viewModel = viewModel,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        composable(Screens.SETTINGS) {
                            val viewModel: SettingsViewModel = hiltViewModel()
                            SettingsScreen(
                                viewModel = viewModel,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}

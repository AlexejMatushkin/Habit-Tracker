package com.practicum.habittracker.presentation.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import com.practicum.habittracker.R
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.practicum.habittracker.presentation.viewmodel.HabitCalendarViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitCalendarScreen(
    habitTitle: String,
    viewModel: HabitCalendarViewModel,
    onNavigateBack: () -> Unit
) {
    val completions by viewModel.completions.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Статистика: $habitTitle") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        if (completions.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Нет данных")
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(completions.size) { index ->
                    val completion = completions[index]
                    val dateStr = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                        .format(Date(completion.date))

                    ListItem(
                        headlineContent = { Text(dateStr) },
                        leadingContent = {
                            if (completion.completed) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Выполнено",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            } else {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_circle_24),
                                    contentDescription = "Не выполнено",
                                    tint = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

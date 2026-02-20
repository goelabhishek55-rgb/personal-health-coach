package com.personalcoach.presentation.workout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personalcoach.domain.model.WorkoutSession
import com.personalcoach.util.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutHistoryScreen(
    onBack: () -> Unit,
    viewModel: WorkoutViewModel = hiltViewModel()
) {
    val sessions by viewModel.allSessions.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Workout History") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (sessions.isEmpty()) {
                item {
                    Text(
                        "No workouts logged yet.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            items(sessions) { session ->
                WorkoutSessionCard(session)
            }
        }
    }
}

@Composable
private fun WorkoutSessionCard(session: WorkoutSession) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(session.category, fontWeight = FontWeight.Bold)
                Text(
                    DateUtils.formatDate(session.date),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (session.durationMinutes != null) {
                Text("Duration: ${session.durationMinutes} min", style = MaterialTheme.typography.bodySmall)
            }
            if (session.caloriesBurned != null) {
                Text("Calories: ${session.caloriesBurned} kcal", style = MaterialTheme.typography.bodySmall)
            }
            Text("Quality: ${session.sessionQuality}/10", style = MaterialTheme.typography.bodySmall)
        }
    }
}

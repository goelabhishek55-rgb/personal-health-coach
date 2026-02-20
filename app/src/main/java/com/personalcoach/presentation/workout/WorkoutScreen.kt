package com.personalcoach.presentation.workout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personalcoach.domain.model.WorkoutExercise
import com.personalcoach.util.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutScreen(
    onNavigateToHistory: () -> Unit,
    viewModel: WorkoutViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) viewModel.resetSaved()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Log Workout") },
                actions = {
                    IconButton(onClick = onNavigateToHistory) {
                        Icon(Icons.Default.History, contentDescription = "History")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Category selection
            Text("Category", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Constants.WORKOUT_CATEGORIES.forEach { cat ->
                    FilterChip(
                        selected = uiState.category == cat,
                        onClick = { viewModel.setCategory(cat) },
                        label = { Text(cat) }
                    )
                }
            }

            // Injury warnings
            uiState.injuryWarnings.forEach { warning ->
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                        Text(
                            text = "Warning: ${warning.conditionName} - " +
                                (warning.riskyCategory?.let { "risky category: $it" }
                                    ?: "risky exercise: ${warning.riskyExercise}"),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            // Exercises
            Text("Exercises", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            uiState.exercises.forEachIndexed { index, exercise ->
                ExerciseRow(exercise = exercise, onRemove = { viewModel.removeExercise(index) })
            }
            AddExerciseForm(
                sessionId = 0,
                onAdd = { viewModel.addExercise(it) }
            )

            // Session quality sliders
            Text("Motivation Before", style = MaterialTheme.typography.bodyMedium)
            Slider(
                value = uiState.motivationBefore.toFloat(),
                onValueChange = { viewModel.setMotivation(it.toInt()) },
                valueRange = 1f..10f,
                steps = 8
            )
            Text("Session Quality", style = MaterialTheme.typography.bodyMedium)
            Slider(
                value = uiState.sessionQuality.toFloat(),
                onValueChange = { viewModel.setQuality(it.toInt()) },
                valueRange = 1f..10f,
                steps = 8
            )

            OutlinedTextField(
                value = uiState.notes,
                onValueChange = viewModel::setNotes,
                label = { Text("Session notes (optional)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            Button(
                onClick = viewModel::saveSession,
                modifier = Modifier.fillMaxWidth(),
                enabled = uiState.category.isNotBlank()
            ) {
                Text("Save Workout")
            }
        }
    }
}

@Composable
private fun ExerciseRow(exercise: WorkoutExercise, onRemove: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(exercise.exerciseName, fontWeight = FontWeight.Medium)
                val details = buildString {
                    exercise.sets?.let { append("${it}x") }
                    exercise.reps?.let { append("${it} reps ") }
                    exercise.weightKg?.let { append("@ ${it}kg") }
                }
                if (details.isNotBlank()) Text(details, style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = onRemove) {
                Icon(Icons.Default.Delete, contentDescription = "Remove")
            }
        }
    }
}

@Composable
private fun AddExerciseForm(sessionId: Int, onAdd: (WorkoutExercise) -> Unit) {
    var name by remember { mutableStateOf("") }
    var sets by remember { mutableStateOf("") }
    var reps by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Add Exercise", fontWeight = FontWeight.Medium)
            OutlinedTextField(
                value = name, onValueChange = { name = it },
                label = { Text("Exercise name") }, modifier = Modifier.fillMaxWidth(), singleLine = true
            )
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = sets, onValueChange = { sets = it },
                    label = { Text("Sets") }, modifier = Modifier.weight(1f), singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = reps, onValueChange = { reps = it },
                    label = { Text("Reps") }, modifier = Modifier.weight(1f), singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                OutlinedTextField(
                    value = weight, onValueChange = { weight = it },
                    label = { Text("kg") }, modifier = Modifier.weight(1f), singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                )
            }
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onAdd(
                            WorkoutExercise(
                                sessionId = sessionId,
                                exerciseName = name,
                                sets = sets.toIntOrNull(),
                                reps = reps.toIntOrNull(),
                                weightKg = weight.toFloatOrNull()
                            )
                        )
                        name = ""
                        sets = ""
                        reps = ""
                        weight = ""
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) { Text("Add") }
        }
    }
}

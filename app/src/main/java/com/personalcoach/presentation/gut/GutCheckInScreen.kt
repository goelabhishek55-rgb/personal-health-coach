package com.personalcoach.presentation.gut

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personalcoach.util.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GutCheckInScreen(
    onNavigateToHistory: () -> Unit,
    viewModel: GutViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.isSaved) {
        if (uiState.isSaved) viewModel.resetSaved()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gut Check-In") },
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text("Overall Gut Feel", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text("${uiState.overallGutFeel}/10", style = MaterialTheme.typography.headlineSmall)
            Slider(
                value = uiState.overallGutFeel.toFloat(),
                onValueChange = { viewModel.setGutFeel(it.toInt()) },
                valueRange = 1f..10f,
                steps = 8
            )

            Text("Symptoms", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Constants.DEFAULT_GUT_SYMPTOMS.forEach { symptom ->
                    FilterChip(
                        selected = uiState.selectedSymptoms.containsKey(symptom),
                        onClick = { viewModel.toggleSymptom(symptom) },
                        label = { Text(symptom) }
                    )
                }
            }

            // Severity for selected symptoms
            uiState.selectedSymptoms.forEach { (symptom, severity) ->
                Column {
                    Text("$symptom severity: $severity/10", style = MaterialTheme.typography.bodyMedium)
                    Slider(
                        value = severity.toFloat(),
                        onValueChange = { viewModel.setSymptomSeverity(symptom, it.toInt()) },
                        valueRange = 1f..10f,
                        steps = 8
                    )
                }
            }

            OutlinedTextField(
                value = uiState.notes,
                onValueChange = viewModel::setNotes,
                label = { Text("Notes (optional)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2
            )

            Button(
                onClick = viewModel::saveCheckIn,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save Check-In")
            }
        }
    }
}

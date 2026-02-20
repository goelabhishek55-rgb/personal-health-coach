package com.personalcoach.presentation.gut

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
import com.personalcoach.domain.model.GutCheckIn
import com.personalcoach.util.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GutHistoryScreen(
    onBack: () -> Unit,
    viewModel: GutViewModel = hiltViewModel()
) {
    val checkIns by viewModel.allCheckIns.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Gut History") },
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
            if (checkIns.isEmpty()) {
                item { Text("No gut check-ins yet.", color = MaterialTheme.colorScheme.onSurfaceVariant) }
            }
            items(checkIns) { checkIn ->
                GutCheckInCard(checkIn)
            }
        }
    }
}

@Composable
private fun GutCheckInCard(checkIn: GutCheckIn) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Gut Feel: ${checkIn.overallGutFeel}/10", fontWeight = FontWeight.Bold)
                Text(
                    DateUtils.formatDateTime(checkIn.dateTime),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            if (checkIn.symptoms.isNotEmpty()) {
                Text(
                    "Symptoms: ${checkIn.symptoms.joinToString { "${it.symptomName}(${it.severity})" }}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            checkIn.notes?.let {
                Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

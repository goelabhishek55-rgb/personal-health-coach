package com.personalcoach.presentation.weight

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personalcoach.domain.model.WeightRecord
import com.personalcoach.util.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeightScreen(
    onBack: () -> Unit,
    viewModel: WeightViewModel = hiltViewModel()
) {
    val records by viewModel.weightRecords.collectAsStateWithLifecycle()
    val savedRecord by viewModel.savedRecord.collectAsStateWithLifecycle()
    var weightText by remember { mutableStateOf("") }
    var bodyFatText by remember { mutableStateOf("") }

    LaunchedEffect(savedRecord) {
        if (savedRecord) viewModel.resetSaved()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weight Tracker") },
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
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Entry form
            item {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Log Weight", fontWeight = FontWeight.SemiBold)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = weightText, onValueChange = { weightText = it },
                                label = { Text("Weight (kg)") }, modifier = Modifier.weight(1f), singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                            )
                            OutlinedTextField(
                                value = bodyFatText, onValueChange = { bodyFatText = it },
                                label = { Text("Body Fat %") }, modifier = Modifier.weight(1f), singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                            )
                        }
                        Button(
                            onClick = {
                                weightText.toFloatOrNull()?.let { kg ->
                                    viewModel.logWeight(kg, bodyFatText.toFloatOrNull())
                                    weightText = ""; bodyFatText = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = weightText.toFloatOrNull() != null
                        ) { Text("Save") }
                    }
                }
            }

            // Trend chart
            if (records.size >= 2) {
                item {
                    Text("Trend", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    WeightTrendChart(records = records.takeLast(14).reversed())
                }
            }

            // History
            if (records.isNotEmpty()) {
                item { Text("History", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold) }
            }
            items(records) { record ->
                WeightRecordCard(record)
            }
        }
    }
}

@Composable
private fun WeightTrendChart(records: List<WeightRecord>) {
    val primaryColor = MaterialTheme.colorScheme.primary
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
    ) {
        if (records.size < 2) return@Canvas
        val minWeight = records.minOf { it.weightKg }
        val maxWeight = records.maxOf { it.weightKg }
        val range = (maxWeight - minWeight).coerceAtLeast(1f)
        val stepX = size.width / (records.size - 1)

        val points = records.mapIndexed { index, record ->
            Offset(
                x = index * stepX,
                y = size.height - ((record.weightKg - minWeight) / range) * size.height
            )
        }
        for (i in 0 until points.size - 1) {
            drawLine(color = primaryColor, start = points[i], end = points[i + 1], strokeWidth = 3f)
        }
        points.forEach { point ->
            drawCircle(color = primaryColor, radius = 6f, center = point)
        }
    }
}

@Composable
private fun WeightRecordCard(record: WeightRecord) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("${record.weightKg} kg", fontWeight = FontWeight.Medium)
            record.bodyFatPercent?.let { Text("${it}% BF", style = MaterialTheme.typography.bodySmall) }
            Text(DateUtils.formatDate(record.dateTime), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

package com.personalcoach.presentation.analytics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personalcoach.util.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnalyticsScreen(
    viewModel: AnalyticsViewModel = hiltViewModel()
) {
    val analytics by viewModel.weeklyAnalytics.collectAsStateWithLifecycle()
    val weekStart by viewModel.selectedWeekStart.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Analytics") })
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
            // Week navigation
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = viewModel::previousWeek) {
                    Icon(Icons.Default.ChevronLeft, contentDescription = "Previous week")
                }
                Text(
                    "Week of ${DateUtils.formatDate(weekStart)}",
                    style = MaterialTheme.typography.titleMedium
                )
                IconButton(onClick = viewModel::nextWeek) {
                    Icon(Icons.Default.ChevronRight, contentDescription = "Next week")
                }
            }

            analytics?.let { data ->
                // Summary cards
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "Workouts",
                        value = data.totalWorkouts.toString()
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "Avg Gut Feel",
                        value = String.format("%.1f", data.avgGutFeel)
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "Avg Calories/Day",
                        value = "${data.avgCaloriesPerDay.toInt()} kcal"
                    )
                    StatCard(
                        modifier = Modifier.weight(1f),
                        title = "Avg Water/Day",
                        value = "${data.avgWaterMlPerDay.toInt()} ml"
                    )
                }

                // Weight trend
                if (data.weightRecords.isNotEmpty()) {
                    Text("Weight Entries This Week", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                    data.weightRecords.forEach { record ->
                        Text(
                            "${record.weightKg} kg on ${DateUtils.formatDate(record.dateTime)}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            } ?: run {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}

@Composable
private fun StatCard(modifier: Modifier = Modifier, title: String, value: String) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
    }
}

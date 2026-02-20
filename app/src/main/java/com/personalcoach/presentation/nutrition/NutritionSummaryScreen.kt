package com.personalcoach.presentation.nutrition

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NutritionSummaryScreen(
    onBack: () -> Unit,
    viewModel: NutritionViewModel = hiltViewModel()
) {
    val summary by viewModel.todaySummary.collectAsStateWithLifecycle()
    val meals by viewModel.todayMeals.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Nutrition Summary") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
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
            val totalCalories = meals.sumOf { it.calories }
            val totalProtein = meals.sumOf { it.proteinG.toDouble() }.toFloat()
            val totalCarbs = meals.sumOf { it.carbsG.toDouble() }.toFloat()
            val totalFats = meals.sumOf { it.fatsG.toDouble() }.toFloat()

            Text("Today's Summary", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)

            NutrientRow("Calories", totalCalories.toFloat(), summary?.targetCalories?.toFloat() ?: 2000f, "kcal")
            NutrientRow("Protein", totalProtein, summary?.targetProteinG ?: 150f, "g")
            NutrientRow("Carbs", totalCarbs, summary?.targetCarbsG ?: 250f, "g")
            NutrientRow("Fats", totalFats, summary?.targetFatsG ?: 65f, "g")
        }
    }
}

@Composable
private fun NutrientRow(label: String, actual: Float, target: Float, unit: String) {
    val progress = if (target > 0) (actual / target).coerceIn(0f, 1f) else 0f
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(label, fontWeight = FontWeight.Medium)
                Text("${actual.toInt()} / ${target.toInt()} $unit", style = MaterialTheme.typography.bodySmall)
            }
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

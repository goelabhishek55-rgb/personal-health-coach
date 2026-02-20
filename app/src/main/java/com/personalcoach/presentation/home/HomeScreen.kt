package com.personalcoach.presentation.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personalcoach.presentation.components.QuickActionCard
import com.personalcoach.presentation.components.WaterProgressBar
import com.personalcoach.util.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToWorkout: () -> Unit,
    onNavigateToGut: () -> Unit,
    onNavigateToMeal: () -> Unit,
    onNavigateToWeight: () -> Unit,
    onNavigateToWater: () -> Unit,
    onNavigateToSettings: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()
    val waterIntake by viewModel.todayWaterIntake.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Personal Health Coach") },
                actions = {
                    IconButton(onClick = onNavigateToSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.logGlass() }) {
                Icon(Icons.Default.WaterDrop, contentDescription = "Log Water")
            }
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
            // Greeting
            val greeting = when (java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)) {
                in 5..11 -> "Good morning"
                in 12..16 -> "Good afternoon"
                else -> "Good evening"
            }
            Text(
                text = "$greeting, ${userProfile?.name?.ifEmpty { "there" } ?: "there"}!",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = DateUtils.formatDate(System.currentTimeMillis()),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Water progress
            waterIntake?.let { water ->
                WaterProgressBar(
                    totalMl = water.totalMl,
                    targetMl = water.targetMl,
                    onAddWater = { onNavigateToWater() }
                )
            } ?: WaterProgressBar(
                totalMl = 0,
                targetMl = userProfile?.dailyWaterTargetMl ?: 2500,
                onAddWater = { onNavigateToWater() }
            )

            // Quick Actions
            Text(
                text = "Quick Actions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickActionCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.FitnessCenter,
                    label = "Log Workout",
                    onClick = onNavigateToWorkout
                )
                QuickActionCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Spa,
                    label = "Gut Check-In",
                    onClick = onNavigateToGut
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                QuickActionCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Restaurant,
                    label = "Log Meal",
                    onClick = onNavigateToMeal
                )
                QuickActionCard(
                    modifier = Modifier.weight(1f),
                    icon = Icons.Default.Monitor,
                    label = "Log Weight",
                    onClick = onNavigateToWeight
                )
            }
        }
    }
}

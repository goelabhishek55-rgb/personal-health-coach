package com.personalcoach.presentation.water

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WaterScreen(
    onBack: () -> Unit,
    viewModel: WaterViewModel = hiltViewModel()
) {
    val water by viewModel.todayWater.collectAsStateWithLifecycle()
    val targetMl by viewModel.targetMl.collectAsStateWithLifecycle()
    val glassSizeMl by viewModel.glassSizeMl.collectAsStateWithLifecycle()
    var customAmount by remember { mutableStateOf("") }

    val totalMl = water?.totalMl ?: 0
    val progress = if (targetMl > 0) (totalMl.toFloat() / targetMl).coerceIn(0f, 1f) else 0f

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Water Intake") },
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
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                "$totalMl ml / $targetMl ml",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(12.dp)
            )
            Text(
                "${water?.glassCount ?: 0} glasses",
                style = MaterialTheme.typography.bodyLarge
            )
            Button(
                onClick = { viewModel.logGlass() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.WaterDrop, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Add ${glassSizeMl}ml glass")
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = customAmount,
                    onValueChange = { customAmount = it },
                    label = { Text("Custom amount (ml)") },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                Button(
                    onClick = {
                        customAmount.toIntOrNull()?.let {
                            viewModel.logCustomAmount(it)
                            customAmount = ""
                        }
                    }
                ) { Text("Add") }
            }
        }
    }
}

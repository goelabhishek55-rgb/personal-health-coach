package com.personalcoach.presentation.nutrition

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personalcoach.domain.model.MealLog
import com.personalcoach.util.Constants
import com.personalcoach.util.DateUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MealLogScreen(
    onNavigateToSummary: () -> Unit,
    viewModel: NutritionViewModel = hiltViewModel()
) {
    val meals by viewModel.todayMeals.collectAsStateWithLifecycle()
    val searchResults by viewModel.searchResults.collectAsStateWithLifecycle()
    val isSearching by viewModel.isSearching.collectAsStateWithLifecycle()
    val savedMeal by viewModel.savedMeal.collectAsStateWithLifecycle()

    LaunchedEffect(savedMeal) {
        if (savedMeal) viewModel.resetSaved()
    }

    var searchQuery by remember { mutableStateOf("") }
    var selectedMealType by remember { mutableStateOf("Breakfast") }
    var description by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }
    var carbs by remember { mutableStateOf("") }
    var fats by remember { mutableStateOf("") }
    var showForm by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meal Log") },
                actions = {
                    TextButton(onClick = onNavigateToSummary) { Text("Summary") }
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
            // Search food
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Search food (Open Food Facts)") },
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        IconButton(onClick = { viewModel.searchFood(searchQuery) }) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }
                    },
                    singleLine = true
                )
            }

            if (isSearching) {
                item { CircularProgressIndicator(modifier = Modifier.padding(16.dp)) }
            }

            if (searchResults.isNotEmpty()) {
                item { Text("Search Results", style = MaterialTheme.typography.titleSmall) }
                items(searchResults) { product ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            description = product.productName ?: ""
                            calories = product.nutriments?.energyKcal100g?.toInt()?.toString() ?: ""
                            protein = product.nutriments?.proteins100g?.toString() ?: ""
                            carbs = product.nutriments?.carbohydrates100g?.toString() ?: ""
                            fats = product.nutriments?.fat100g?.toString() ?: ""
                            showForm = true
                        }
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text(product.productName ?: "Unknown", fontWeight = FontWeight.Medium)
                            product.brands?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
                        }
                    }
                }
            }

            // Manual entry form
            item {
                OutlinedButton(
                    onClick = { showForm = !showForm },
                    modifier = Modifier.fillMaxWidth()
                ) { Text(if (showForm) "Hide Manual Entry" else "Manual Entry") }
            }

            if (showForm) {
                item {
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Meal Type", style = MaterialTheme.typography.bodyMedium)
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Constants.MEAL_TYPES.forEach { type ->
                                FilterChip(
                                    selected = selectedMealType == type,
                                    onClick = { selectedMealType = type },
                                    label = { Text(type) }
                                )
                            }
                        }
                        OutlinedTextField(
                            value = description, onValueChange = { description = it },
                            label = { Text("Description") }, modifier = Modifier.fillMaxWidth(), singleLine = true
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = calories, onValueChange = { calories = it },
                                label = { Text("kcal") }, modifier = Modifier.weight(1f), singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                            )
                            OutlinedTextField(
                                value = protein, onValueChange = { protein = it },
                                label = { Text("Protein g") }, modifier = Modifier.weight(1f), singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            OutlinedTextField(
                                value = carbs, onValueChange = { carbs = it },
                                label = { Text("Carbs g") }, modifier = Modifier.weight(1f), singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                            )
                            OutlinedTextField(
                                value = fats, onValueChange = { fats = it },
                                label = { Text("Fats g") }, modifier = Modifier.weight(1f), singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                            )
                        }
                        Button(
                            onClick = {
                                viewModel.logMeal(
                                    mealType = selectedMealType,
                                    description = description.ifBlank { null },
                                    calories = calories.toIntOrNull() ?: 0,
                                    proteinG = protein.toFloatOrNull() ?: 0f,
                                    carbsG = carbs.toFloatOrNull() ?: 0f,
                                    fatsG = fats.toFloatOrNull() ?: 0f,
                                    fiberG = null
                                )
                                description = ""
                                calories = ""
                                protein = ""
                                carbs = ""
                                fats = ""
                                showForm = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) { Text("Log Meal") }
                    }
                }
            }

            // Today's meals
            if (meals.isNotEmpty()) {
                item {
                    Text(
                        "Today's Meals",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                items(meals) { meal ->
                    MealCard(meal = meal, onDelete = { viewModel.deleteMeal(meal) })
                }
            }
        }
    }
}

@Composable
private fun MealCard(meal: MealLog, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(meal.mealType, fontWeight = FontWeight.Medium)
                meal.description?.let { Text(it, style = MaterialTheme.typography.bodySmall) }
                Text(
                    "${meal.calories} kcal · P:${meal.proteinG}g C:${meal.carbsG}g F:${meal.fatsG}g",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(DateUtils.formatTime(meal.dateTime), style = MaterialTheme.typography.bodySmall)
            }
        }
    }
}

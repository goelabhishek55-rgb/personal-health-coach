package com.personalcoach.presentation.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.personalcoach.util.Constants

@Composable
fun OnboardingScreen(
    onFinished: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    val step by viewModel.currentStep.collectAsStateWithLifecycle()
    val profile by viewModel.profile.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        LinearProgressIndicator(
            progress = { (step + 1) / 6f },
            modifier = Modifier.fillMaxWidth()
        )
        Text(
            text = "Step ${step + 1} of 6",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        when (step) {
            0 -> Step1Name(
                name = profile.name,
                onNameChange = viewModel::updateName,
                onNext = viewModel::nextStep
            )
            1 -> Step2Demographics(
                age = profile.age,
                sex = profile.sex,
                heightCm = profile.heightCm,
                onAgeChange = viewModel::updateAge,
                onSexChange = viewModel::updateSex,
                onHeightChange = viewModel::updateHeight,
                onNext = viewModel::nextStep,
                onBack = viewModel::prevStep
            )
            2 -> Step3Goals(
                selectedGoals = profile.primaryGoals,
                onGoalsChange = viewModel::updateGoals,
                onNext = viewModel::nextStep,
                onBack = viewModel::prevStep
            )
            3 -> Step4NutritionTargets(
                calorieTarget = profile.dailyCalorieTarget,
                proteinTarget = profile.dailyProteinTargetG,
                carbsTarget = profile.dailyCarbsTargetG,
                fatsTarget = profile.dailyFatsTargetG,
                onCalorieChange = viewModel::updateCalorieTarget,
                onProteinChange = viewModel::updateProteinTarget,
                onCarbsChange = viewModel::updateCarbsTarget,
                onFatsChange = viewModel::updateFatsTarget,
                onNext = viewModel::nextStep,
                onBack = viewModel::prevStep
            )
            4 -> Step5Water(
                waterTargetMl = profile.dailyWaterTargetMl,
                onWaterTargetChange = viewModel::updateWaterTarget,
                onNext = viewModel::nextStep,
                onBack = viewModel::prevStep
            )
            5 -> Step6Finish(
                name = profile.name,
                onFinish = { viewModel.finishOnboarding(onFinished) },
                onBack = viewModel::prevStep
            )
        }
    }
}

@Composable
private fun Step1Name(
    name: String,
    onNameChange: (String) -> Unit,
    onNext: () -> Unit
) {
    Text("Welcome!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
    Text("What's your name?", style = MaterialTheme.typography.bodyLarge)
    OutlinedTextField(
        value = name,
        onValueChange = onNameChange,
        label = { Text("Your name") },
        modifier = Modifier.fillMaxWidth(),
        singleLine = true
    )
    Button(
        onClick = onNext,
        modifier = Modifier.fillMaxWidth(),
        enabled = name.isNotBlank()
    ) {
        Text("Continue")
    }
}

@Composable
private fun Step2Demographics(
    age: Int, sex: String, heightCm: Float,
    onAgeChange: (Int) -> Unit, onSexChange: (String) -> Unit,
    onHeightChange: (Float) -> Unit, onNext: () -> Unit, onBack: () -> Unit
) {
    Text("About You", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

    var ageText by remember { mutableStateOf(age.toString()) }
    var heightText by remember { mutableStateOf(heightCm.toInt().toString()) }

    OutlinedTextField(
        value = ageText,
        onValueChange = { ageText = it; it.toIntOrNull()?.let(onAgeChange) },
        label = { Text("Age") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true
    )
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        listOf("Male", "Female", "Other").forEach { option ->
            FilterChip(
                selected = sex == option,
                onClick = { onSexChange(option) },
                label = { Text(option) }
            )
        }
    }
    OutlinedTextField(
        value = heightText,
        onValueChange = { heightText = it; it.toFloatOrNull()?.let(onHeightChange) },
        label = { Text("Height (cm)") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true
    )
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) { Text("Back") }
        Button(onClick = onNext, modifier = Modifier.weight(1f)) { Text("Continue") }
    }
}

@Composable
private fun Step3Goals(
    selectedGoals: List<String>,
    onGoalsChange: (List<String>) -> Unit,
    onNext: () -> Unit, onBack: () -> Unit
) {
    Text("Your Goals", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
    Text("Select all that apply:", style = MaterialTheme.typography.bodyLarge)
    Constants.PRIMARY_GOALS.forEach { goal ->
        val selected = goal in selectedGoals
        FilterChip(
            selected = selected,
            onClick = {
                onGoalsChange(
                    if (selected) selectedGoals - goal else selectedGoals + goal
                )
            },
            label = { Text(goal) },
            modifier = Modifier.fillMaxWidth()
        )
    }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) { Text("Back") }
        Button(onClick = onNext, modifier = Modifier.weight(1f)) { Text("Continue") }
    }
}

@Composable
private fun Step4NutritionTargets(
    calorieTarget: Int, proteinTarget: Float, carbsTarget: Float, fatsTarget: Float,
    onCalorieChange: (Int) -> Unit, onProteinChange: (Float) -> Unit,
    onCarbsChange: (Float) -> Unit, onFatsChange: (Float) -> Unit,
    onNext: () -> Unit, onBack: () -> Unit
) {
    Text("Nutrition Targets", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)

    var calText by remember { mutableStateOf(calorieTarget.toString()) }
    var proText by remember { mutableStateOf(proteinTarget.toInt().toString()) }
    var carbText by remember { mutableStateOf(carbsTarget.toInt().toString()) }
    var fatText by remember { mutableStateOf(fatsTarget.toInt().toString()) }

    listOf(
        Triple("Daily Calories (kcal)", calText) { v: String -> calText = v; v.toIntOrNull()?.let(onCalorieChange) },
        Triple("Protein (g)", proText) { v: String -> proText = v; v.toFloatOrNull()?.let(onProteinChange) },
        Triple("Carbs (g)", carbText) { v: String -> carbText = v; v.toFloatOrNull()?.let(onCarbsChange) },
        Triple("Fats (g)", fatText) { v: String -> fatText = v; v.toFloatOrNull()?.let(onFatsChange) }
    ).forEach { (label, value, onChange) ->
        OutlinedTextField(
            value = value,
            onValueChange = { onChange(it) },
            label = { Text(label) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true
        )
    }
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) { Text("Back") }
        Button(onClick = onNext, modifier = Modifier.weight(1f)) { Text("Continue") }
    }
}

@Composable
private fun Step5Water(
    waterTargetMl: Int,
    onWaterTargetChange: (Int) -> Unit,
    onNext: () -> Unit, onBack: () -> Unit
) {
    Text("Water Goal", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
    var waterText by remember { mutableStateOf(waterTargetMl.toString()) }
    OutlinedTextField(
        value = waterText,
        onValueChange = { waterText = it; it.toIntOrNull()?.let(onWaterTargetChange) },
        label = { Text("Daily water target (ml)") },
        modifier = Modifier.fillMaxWidth(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true
    )
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) { Text("Back") }
        Button(onClick = onNext, modifier = Modifier.weight(1f)) { Text("Continue") }
    }
}

@Composable
private fun Step6Finish(
    name: String,
    onFinish: () -> Unit,
    onBack: () -> Unit
) {
    Text("You're all set, $name!", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
    Text(
        "Your profile is ready. Start tracking your health journey today.",
        style = MaterialTheme.typography.bodyLarge
    )
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        OutlinedButton(onClick = onBack, modifier = Modifier.weight(1f)) { Text("Back") }
        Button(onClick = onFinish, modifier = Modifier.weight(1f)) { Text("Get Started!") }
    }
}

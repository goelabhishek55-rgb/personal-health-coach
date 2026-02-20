package com.personalcoach.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personalcoach.domain.model.UserProfile
import com.personalcoach.domain.usecase.GetOrCreateUserProfileUseCase
import com.personalcoach.domain.usecase.SaveUserProfileUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val getOrCreateUserProfileUseCase: GetOrCreateUserProfileUseCase,
    private val saveUserProfileUseCase: SaveUserProfileUseCase
) : ViewModel() {

    private val _profile = MutableStateFlow(UserProfile())
    val profile: StateFlow<UserProfile> = _profile

    private val _currentStep = MutableStateFlow(0)
    val currentStep: StateFlow<Int> = _currentStep

    init {
        viewModelScope.launch {
            _profile.value = getOrCreateUserProfileUseCase()
        }
    }

    fun updateName(name: String) { _profile.value = _profile.value.copy(name = name) }
    fun updateAge(age: Int) { _profile.value = _profile.value.copy(age = age) }
    fun updateSex(sex: String) { _profile.value = _profile.value.copy(sex = sex) }
    fun updateHeight(heightCm: Float) { _profile.value = _profile.value.copy(heightCm = heightCm) }
    fun updateGoals(goals: List<String>) { _profile.value = _profile.value.copy(primaryGoals = goals) }
    fun updateCalorieTarget(cal: Int) { _profile.value = _profile.value.copy(dailyCalorieTarget = cal) }
    fun updateProteinTarget(g: Float) { _profile.value = _profile.value.copy(dailyProteinTargetG = g) }
    fun updateCarbsTarget(g: Float) { _profile.value = _profile.value.copy(dailyCarbsTargetG = g) }
    fun updateFatsTarget(g: Float) { _profile.value = _profile.value.copy(dailyFatsTargetG = g) }
    fun updateWaterTarget(ml: Int) { _profile.value = _profile.value.copy(dailyWaterTargetMl = ml) }

    fun nextStep() { _currentStep.value++ }
    fun prevStep() { if (_currentStep.value > 0) _currentStep.value-- }

    fun finishOnboarding(onDone: () -> Unit) {
        viewModelScope.launch {
            saveUserProfileUseCase(_profile.value.copy(onboardingCompleted = true))
            onDone()
        }
    }
}

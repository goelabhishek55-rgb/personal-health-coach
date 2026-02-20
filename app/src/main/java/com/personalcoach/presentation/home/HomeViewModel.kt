package com.personalcoach.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personalcoach.domain.model.DailySummary
import com.personalcoach.domain.model.UserProfile
import com.personalcoach.domain.model.WaterIntake
import com.personalcoach.domain.repository.UserProfileRepository
import com.personalcoach.domain.repository.WaterRepository
import com.personalcoach.domain.usecase.LogWaterIntakeUseCase
import com.personalcoach.util.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
    private val waterRepository: WaterRepository,
    private val logWaterIntakeUseCase: LogWaterIntakeUseCase
) : ViewModel() {

    val userProfile: StateFlow<UserProfile?> = userProfileRepository.getUserProfile()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val onboardingCompleted: StateFlow<Boolean?> = userProfile
        .map { it?.onboardingCompleted }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val todayWaterIntake: StateFlow<WaterIntake?> = waterRepository
        .getWaterIntakeForDay(DateUtils.todayStartMs())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun logGlass() {
        viewModelScope.launch {
            val glassSizeMl = userProfile.value?.waterGlassSizeMl ?: 250
            logWaterIntakeUseCase(glassSizeMl)
        }
    }
}

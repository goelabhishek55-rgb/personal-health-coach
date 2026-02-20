package com.personalcoach.presentation.water

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
class WaterViewModel @Inject constructor(
    private val waterRepository: WaterRepository,
    private val userProfileRepository: UserProfileRepository,
    private val logWaterIntakeUseCase: LogWaterIntakeUseCase
) : ViewModel() {

    val todayWater: StateFlow<WaterIntake?> = waterRepository
        .getWaterIntakeForDay(DateUtils.todayStartMs())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val glassSizeMl: StateFlow<Int> = userProfileRepository.getUserProfile()
        .map { it?.waterGlassSizeMl ?: 250 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 250)

    val targetMl: StateFlow<Int> = userProfileRepository.getUserProfile()
        .map { it?.dailyWaterTargetMl ?: 2500 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 2500)

    fun logGlass() {
        viewModelScope.launch {
            logWaterIntakeUseCase(glassSizeMl.value)
        }
    }

    fun logCustomAmount(ml: Int) {
        viewModelScope.launch {
            logWaterIntakeUseCase(ml)
        }
    }
}

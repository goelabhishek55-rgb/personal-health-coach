package com.personalcoach.presentation.nutrition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personalcoach.data.remote.dto.ProductDto
import com.personalcoach.domain.model.MealLog
import com.personalcoach.domain.model.NutritionSummary
import com.personalcoach.domain.repository.NutritionRepository
import com.personalcoach.domain.usecase.GetDailyNutritionSummaryUseCase
import com.personalcoach.domain.usecase.LogMealUseCase
import com.personalcoach.domain.usecase.SearchFoodUseCase
import com.personalcoach.util.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NutritionViewModel @Inject constructor(
    private val nutritionRepository: NutritionRepository,
    private val logMealUseCase: LogMealUseCase,
    private val searchFoodUseCase: SearchFoodUseCase,
    private val getDailyNutritionSummaryUseCase: GetDailyNutritionSummaryUseCase
) : ViewModel() {

    val todayMeals: StateFlow<List<MealLog>> = nutritionRepository
        .getMealsForDay(DateUtils.todayStartMs(), DateUtils.todayStartMs() + DateUtils.DAY_MS)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val todaySummary: StateFlow<NutritionSummary?> = getDailyNutritionSummaryUseCase()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _searchResults = MutableStateFlow<List<ProductDto>>(emptyList())
    val searchResults: StateFlow<List<ProductDto>> = _searchResults

    private val _isSearching = MutableStateFlow(false)
    val isSearching: StateFlow<Boolean> = _isSearching

    private val _savedMeal = MutableStateFlow(false)
    val savedMeal: StateFlow<Boolean> = _savedMeal

    fun searchFood(query: String) {
        viewModelScope.launch {
            _isSearching.value = true
            _searchResults.value = searchFoodUseCase(query)
            _isSearching.value = false
        }
    }

    fun logMeal(
        mealType: String, description: String?, calories: Int,
        proteinG: Float, carbsG: Float, fatsG: Float, fiberG: Float?
    ) {
        viewModelScope.launch {
            logMealUseCase(
                MealLog(
                    dateTime = System.currentTimeMillis(),
                    mealType = mealType,
                    description = description,
                    calories = calories,
                    proteinG = proteinG,
                    carbsG = carbsG,
                    fatsG = fatsG,
                    fiberG = fiberG
                )
            )
            _savedMeal.value = true
        }
    }

    fun resetSaved() { _savedMeal.value = false }

    fun deleteMeal(meal: MealLog) {
        viewModelScope.launch { nutritionRepository.deleteMeal(meal) }
    }
}

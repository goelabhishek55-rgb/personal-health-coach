package com.personalcoach.presentation.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personalcoach.domain.usecase.GetWeeklyAnalyticsUseCase
import com.personalcoach.domain.usecase.WeeklyAnalytics
import com.personalcoach.util.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class AnalyticsViewModel @Inject constructor(
    private val getWeeklyAnalyticsUseCase: GetWeeklyAnalyticsUseCase
) : ViewModel() {

    private val _selectedWeekStart = MutableStateFlow(DateUtils.startOfWeek(System.currentTimeMillis()))

    val weeklyAnalytics: StateFlow<WeeklyAnalytics?> = _selectedWeekStart.flatMapLatest { weekStart ->
        getWeeklyAnalyticsUseCase(weekStart)
    }.map { it }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val selectedWeekStart: StateFlow<Long> = _selectedWeekStart

    fun previousWeek() {
        _selectedWeekStart.value -= 7 * DateUtils.DAY_MS
    }

    fun nextWeek() {
        val next = _selectedWeekStart.value + 7 * DateUtils.DAY_MS
        if (next <= System.currentTimeMillis()) {
            _selectedWeekStart.value = next
        }
    }
}

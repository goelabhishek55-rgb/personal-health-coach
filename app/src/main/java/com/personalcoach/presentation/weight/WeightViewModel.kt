package com.personalcoach.presentation.weight

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personalcoach.domain.model.WeightRecord
import com.personalcoach.domain.repository.WeightRepository
import com.personalcoach.domain.usecase.LogWeightUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeightViewModel @Inject constructor(
    private val weightRepository: WeightRepository,
    private val logWeightUseCase: LogWeightUseCase
) : ViewModel() {

    val weightRecords: StateFlow<List<WeightRecord>> = weightRepository.getAllWeightRecords()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _savedRecord = MutableStateFlow(false)
    val savedRecord: StateFlow<Boolean> = _savedRecord

    fun logWeight(weightKg: Float, bodyFatPercent: Float?) {
        viewModelScope.launch {
            logWeightUseCase(
                WeightRecord(
                    dateTime = System.currentTimeMillis(),
                    weightKg = weightKg,
                    bodyFatPercent = bodyFatPercent
                )
            )
            _savedRecord.value = true
        }
    }

    fun resetSaved() { _savedRecord.value = false }

    fun deleteRecord(record: WeightRecord) {
        viewModelScope.launch { weightRepository.deleteWeightRecord(record) }
    }
}

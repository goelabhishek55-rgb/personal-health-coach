package com.personalcoach.presentation.gut

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personalcoach.domain.model.GutCheckIn
import com.personalcoach.domain.model.GutSymptom
import com.personalcoach.domain.repository.GutRepository
import com.personalcoach.domain.usecase.LogGutCheckInUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class GutUiState(
    val overallGutFeel: Int = 5,
    val selectedSymptoms: Map<String, Int> = emptyMap(),
    val notes: String = "",
    val isSaved: Boolean = false
)

@HiltViewModel
class GutViewModel @Inject constructor(
    private val gutRepository: GutRepository,
    private val logGutCheckInUseCase: LogGutCheckInUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GutUiState())
    val uiState: StateFlow<GutUiState> = _uiState

    val allCheckIns: StateFlow<List<GutCheckIn>> = gutRepository.getAllCheckIns()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setGutFeel(value: Int) { _uiState.update { it.copy(overallGutFeel = value) } }

    fun toggleSymptom(symptom: String) {
        _uiState.update { state ->
            val updated = state.selectedSymptoms.toMutableMap()
            if (updated.containsKey(symptom)) updated.remove(symptom) else updated[symptom] = 5
            state.copy(selectedSymptoms = updated)
        }
    }

    fun setSymptomSeverity(symptom: String, severity: Int) {
        _uiState.update { state ->
            state.copy(selectedSymptoms = state.selectedSymptoms + (symptom to severity))
        }
    }

    fun setNotes(notes: String) { _uiState.update { it.copy(notes = notes) } }

    fun saveCheckIn() {
        viewModelScope.launch {
            val state = _uiState.value
            val checkIn = GutCheckIn(
                dateTime = System.currentTimeMillis(),
                overallGutFeel = state.overallGutFeel,
                notes = state.notes.ifBlank { null },
                symptoms = state.selectedSymptoms.map { (name, severity) ->
                    GutSymptom(symptomName = name, severity = severity)
                }
            )
            logGutCheckInUseCase(checkIn)
            _uiState.value = GutUiState(isSaved = true)
        }
    }

    fun resetSaved() { _uiState.update { it.copy(isSaved = false) } }
}

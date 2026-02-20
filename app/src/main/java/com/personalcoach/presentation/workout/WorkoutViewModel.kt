package com.personalcoach.presentation.workout

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.personalcoach.domain.model.WorkoutExercise
import com.personalcoach.domain.model.WorkoutSession
import com.personalcoach.domain.repository.WorkoutRepository
import com.personalcoach.domain.usecase.CheckInjuryRiskUseCase
import com.personalcoach.domain.usecase.InjuryWarning
import com.personalcoach.domain.usecase.LogWorkoutSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

data class WorkoutUiState(
    val category: String = "",
    val exercises: List<WorkoutExercise> = emptyList(),
    val motivationBefore: Int = 5,
    val sessionQuality: Int = 5,
    val notes: String = "",
    val injuryWarnings: List<InjuryWarning> = emptyList(),
    val isSaved: Boolean = false,
    val isLoading: Boolean = false
)

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val workoutRepository: WorkoutRepository,
    private val logWorkoutSessionUseCase: LogWorkoutSessionUseCase,
    private val checkInjuryRiskUseCase: CheckInjuryRiskUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(WorkoutUiState())
    val uiState: StateFlow<WorkoutUiState> = _uiState

    val allSessions: StateFlow<List<WorkoutSession>> = workoutRepository.getAllSessions()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setCategory(category: String) {
        _uiState.update { it.copy(category = category) }
        checkRisk()
    }

    fun addExercise(exercise: WorkoutExercise) {
        _uiState.update { state ->
            state.copy(exercises = state.exercises + exercise)
        }
        checkRisk()
    }

    fun removeExercise(index: Int) {
        _uiState.update { state ->
            state.copy(exercises = state.exercises.toMutableList().also { it.removeAt(index) })
        }
    }

    fun setMotivation(value: Int) { _uiState.update { it.copy(motivationBefore = value) } }
    fun setQuality(value: Int) { _uiState.update { it.copy(sessionQuality = value) } }
    fun setNotes(notes: String) { _uiState.update { it.copy(notes = notes) } }

    private fun checkRisk() {
        viewModelScope.launch {
            val state = _uiState.value
            val warnings = checkInjuryRiskUseCase.check(
                state.category,
                state.exercises.map { it.exerciseName }
            )
            _uiState.update { it.copy(injuryWarnings = warnings) }
        }
    }

    fun saveSession() {
        viewModelScope.launch {
            val state = _uiState.value
            val session = WorkoutSession(
                date = System.currentTimeMillis(),
                category = state.category,
                motivationBefore = state.motivationBefore,
                sessionQuality = state.sessionQuality,
                sessionNotes = state.notes.ifBlank { null },
                exercises = state.exercises
            )
            logWorkoutSessionUseCase(session)
            _uiState.value = WorkoutUiState(isSaved = true)
        }
    }

    fun resetSaved() { _uiState.update { it.copy(isSaved = false) } }
}

package com.personalcoach.domain.usecase

import com.personalcoach.domain.model.WorkoutSession
import com.personalcoach.domain.repository.WorkoutRepository
import javax.inject.Inject

class LogWorkoutSessionUseCase @Inject constructor(
    private val repository: WorkoutRepository
) {
    suspend operator fun invoke(session: WorkoutSession): Long {
        return repository.insertSession(session)
    }
}

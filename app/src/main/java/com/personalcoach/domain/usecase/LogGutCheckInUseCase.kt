package com.personalcoach.domain.usecase

import com.personalcoach.domain.model.GutCheckIn
import com.personalcoach.domain.repository.GutRepository
import javax.inject.Inject

class LogGutCheckInUseCase @Inject constructor(
    private val repository: GutRepository
) {
    suspend operator fun invoke(checkIn: GutCheckIn): Long {
        return repository.insertCheckIn(checkIn)
    }
}

package com.personalcoach.domain.usecase

import com.personalcoach.domain.model.WeightRecord
import com.personalcoach.domain.repository.WeightRepository
import javax.inject.Inject

class LogWeightUseCase @Inject constructor(
    private val repository: WeightRepository
) {
    suspend operator fun invoke(record: WeightRecord): Long {
        return repository.insertWeightRecord(record)
    }
}

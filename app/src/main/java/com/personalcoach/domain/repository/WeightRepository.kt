package com.personalcoach.domain.repository

import com.personalcoach.domain.model.WeightRecord
import kotlinx.coroutines.flow.Flow

interface WeightRepository {
    fun getAllWeightRecords(): Flow<List<WeightRecord>>
    suspend fun getLatestWeightRecord(): WeightRecord?
    fun getWeightRecordsInRange(startDate: Long, endDate: Long): Flow<List<WeightRecord>>
    suspend fun insertWeightRecord(record: WeightRecord): Long
    suspend fun updateWeightRecord(record: WeightRecord)
    suspend fun deleteWeightRecord(record: WeightRecord)
}

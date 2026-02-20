package com.personalcoach.data.repository

import com.personalcoach.data.local.dao.WeightRecordDao
import com.personalcoach.data.local.entity.WeightRecordEntity
import com.personalcoach.domain.model.WeightRecord
import com.personalcoach.domain.repository.WeightRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WeightRepositoryImpl @Inject constructor(
    private val dao: WeightRecordDao
) : WeightRepository {

    override fun getAllWeightRecords(): Flow<List<WeightRecord>> =
        dao.getAllWeightRecords().map { list -> list.map { it.toDomain() } }

    override suspend fun getLatestWeightRecord(): WeightRecord? =
        dao.getLatestWeightRecord()?.toDomain()

    override fun getWeightRecordsInRange(startDate: Long, endDate: Long): Flow<List<WeightRecord>> =
        dao.getWeightRecordsInRange(startDate, endDate).map { list -> list.map { it.toDomain() } }

    override suspend fun insertWeightRecord(record: WeightRecord): Long =
        dao.insert(record.toEntity())

    override suspend fun updateWeightRecord(record: WeightRecord) =
        dao.update(record.toEntity())

    override suspend fun deleteWeightRecord(record: WeightRecord) =
        dao.delete(record.toEntity())

    private fun WeightRecordEntity.toDomain() = WeightRecord(
        id = id, dateTime = dateTime, weightKg = weightKg,
        bodyFatPercent = bodyFatPercent, bmr = bmr, source = source
    )

    private fun WeightRecord.toEntity() = WeightRecordEntity(
        id = id, dateTime = dateTime, weightKg = weightKg,
        bodyFatPercent = bodyFatPercent, bmr = bmr, source = source
    )
}

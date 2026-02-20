package com.personalcoach.data.repository

import com.personalcoach.data.local.dao.WaterIntakeDao
import com.personalcoach.data.local.entity.WaterIntakeEntity
import com.personalcoach.domain.model.WaterIntake
import com.personalcoach.domain.repository.WaterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WaterRepositoryImpl @Inject constructor(
    private val dao: WaterIntakeDao
) : WaterRepository {

    override fun getWaterIntakeForDay(date: Long): Flow<WaterIntake?> =
        dao.getWaterIntakeForDay(date).map { it?.toDomain() }

    override suspend fun getWaterIntakeForDayOnce(date: Long): WaterIntake? =
        dao.getWaterIntakeForDayOnce(date)?.toDomain()

    override fun getWaterIntakeInRange(startDate: Long, endDate: Long): Flow<List<WaterIntake>> =
        dao.getWaterIntakeInRange(startDate, endDate).map { list -> list.map { it.toDomain() } }

    override suspend fun saveWaterIntake(waterIntake: WaterIntake) {
        dao.insertOrUpdate(waterIntake.toEntity())
    }

    private fun WaterIntakeEntity.toDomain() = WaterIntake(
        id = id, date = date, totalMl = totalMl, targetMl = targetMl, glassCount = glassCount
    )

    private fun WaterIntake.toEntity() = WaterIntakeEntity(
        id = id, date = date, totalMl = totalMl, targetMl = targetMl, glassCount = glassCount
    )
}

package com.personalcoach.data.repository

import com.personalcoach.data.local.dao.GutCheckInDao
import com.personalcoach.data.local.dao.GutSymptomDao
import com.personalcoach.data.local.entity.GutCheckInEntity
import com.personalcoach.data.local.entity.GutSymptomEntity
import com.personalcoach.domain.model.GutCheckIn
import com.personalcoach.domain.model.GutSymptom
import com.personalcoach.domain.repository.GutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GutRepositoryImpl @Inject constructor(
    private val checkInDao: GutCheckInDao,
    private val symptomDao: GutSymptomDao
) : GutRepository {

    override fun getAllCheckIns(): Flow<List<GutCheckIn>> =
        checkInDao.getAllCheckIns().map { list -> list.map { it.toDomain() } }

    override fun getCheckInsForDay(startOfDay: Long, endOfDay: Long): Flow<List<GutCheckIn>> =
        checkInDao.getCheckInsForDay(startOfDay, endOfDay).map { list -> list.map { it.toDomain() } }

    override fun getCheckInsInRange(startDate: Long, endDate: Long): Flow<List<GutCheckIn>> =
        checkInDao.getCheckInsInRange(startDate, endDate).map { list -> list.map { it.toDomain() } }

    override suspend fun getCheckInById(id: Int): GutCheckIn? {
        val checkIn = checkInDao.getCheckInById(id) ?: return null
        val symptoms = symptomDao.getSymptomsForCheckInOnce(id).map { it.toDomain() }
        return checkIn.toDomain().copy(symptoms = symptoms)
    }

    override suspend fun insertCheckIn(checkIn: GutCheckIn): Long {
        val checkInId = checkInDao.insert(checkIn.toEntity()).toInt()
        symptomDao.insertAll(checkIn.symptoms.map { it.copy(checkInId = checkInId).toEntity() })
        return checkInId.toLong()
    }

    override suspend fun updateCheckIn(checkIn: GutCheckIn) {
        checkInDao.update(checkIn.toEntity())
        symptomDao.deleteAllForCheckIn(checkIn.id)
        symptomDao.insertAll(checkIn.symptoms.map { it.toEntity() })
    }

    override suspend fun deleteCheckIn(checkIn: GutCheckIn) {
        checkInDao.delete(checkIn.toEntity())
    }

    private fun GutCheckInEntity.toDomain() = GutCheckIn(
        id = id, dateTime = dateTime, overallGutFeel = overallGutFeel,
        notes = notes, createdAt = createdAt
    )

    private fun GutCheckIn.toEntity() = GutCheckInEntity(
        id = id, dateTime = dateTime, overallGutFeel = overallGutFeel,
        notes = notes, createdAt = createdAt
    )

    private fun GutSymptomEntity.toDomain() = GutSymptom(
        id = id, checkInId = checkInId, symptomName = symptomName, severity = severity
    )

    private fun GutSymptom.toEntity() = GutSymptomEntity(
        id = id, checkInId = checkInId, symptomName = symptomName, severity = severity
    )
}

package com.personalcoach.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.personalcoach.data.local.dao.WorkoutExerciseDao
import com.personalcoach.data.local.dao.WorkoutSessionDao
import com.personalcoach.data.local.entity.WorkoutExerciseEntity
import com.personalcoach.data.local.entity.WorkoutSessionEntity
import com.personalcoach.domain.model.WorkoutExercise
import com.personalcoach.domain.model.WorkoutSession
import com.personalcoach.domain.repository.WorkoutRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WorkoutRepositoryImpl @Inject constructor(
    private val sessionDao: WorkoutSessionDao,
    private val exerciseDao: WorkoutExerciseDao
) : WorkoutRepository {

    private val gson = Gson()

    override fun getAllSessions(): Flow<List<WorkoutSession>> =
        sessionDao.getAllSessions().map { list -> list.map { it.toDomain() } }

    override fun getSessionsForDay(startOfDay: Long, endOfDay: Long): Flow<List<WorkoutSession>> =
        sessionDao.getSessionsForDay(startOfDay, endOfDay).map { list -> list.map { it.toDomain() } }

    override fun getSessionsInRange(startDate: Long, endDate: Long): Flow<List<WorkoutSession>> =
        sessionDao.getSessionsInRange(startDate, endDate).map { list -> list.map { it.toDomain() } }

    override suspend fun getSessionById(id: Int): WorkoutSession? {
        val session = sessionDao.getSessionById(id) ?: return null
        val exercises = exerciseDao.getExercisesForSessionOnce(id).map { it.toDomain() }
        return session.toDomain().copy(exercises = exercises)
    }

    override suspend fun insertSession(session: WorkoutSession): Long {
        val sessionId = sessionDao.insert(session.toEntity()).toInt()
        exerciseDao.insertAll(session.exercises.mapIndexed { i, e ->
            e.copy(sessionId = sessionId, orderIndex = i).toEntity()
        })
        return sessionId.toLong()
    }

    override suspend fun updateSession(session: WorkoutSession) {
        sessionDao.update(session.toEntity())
    }

    override suspend fun deleteSession(session: WorkoutSession) {
        sessionDao.delete(session.toEntity())
    }

    private fun WorkoutSessionEntity.toDomain() = WorkoutSession(
        id = id, date = date, category = category,
        tags = gson.fromJson(tags, object : TypeToken<List<String>>() {}.type) ?: emptyList(),
        motivationBefore = motivationBefore, sessionQuality = sessionQuality,
        sessionNotes = sessionNotes, durationMinutes = durationMinutes,
        averageHeartRate = averageHeartRate, caloriesBurned = caloriesBurned,
        distanceMeters = distanceMeters, createdAt = createdAt
    )

    private fun WorkoutSession.toEntity() = WorkoutSessionEntity(
        id = id, date = date, category = category, tags = gson.toJson(tags),
        motivationBefore = motivationBefore, sessionQuality = sessionQuality,
        sessionNotes = sessionNotes, durationMinutes = durationMinutes,
        averageHeartRate = averageHeartRate, caloriesBurned = caloriesBurned,
        distanceMeters = distanceMeters, createdAt = createdAt
    )

    private fun WorkoutExerciseEntity.toDomain() = WorkoutExercise(
        id = id, sessionId = sessionId, exerciseName = exerciseName, sets = sets,
        reps = reps, weightKg = weightKg, durationSeconds = durationSeconds,
        distanceMeters = distanceMeters, notes = notes, orderIndex = orderIndex
    )

    private fun WorkoutExercise.toEntity() = WorkoutExerciseEntity(
        id = id, sessionId = sessionId, exerciseName = exerciseName, sets = sets,
        reps = reps, weightKg = weightKg, durationSeconds = durationSeconds,
        distanceMeters = distanceMeters, notes = notes, orderIndex = orderIndex
    )
}

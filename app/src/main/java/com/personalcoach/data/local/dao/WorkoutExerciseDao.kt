package com.personalcoach.data.local.dao

import androidx.room.*
import com.personalcoach.data.local.entity.WorkoutExerciseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutExerciseDao {
    @Query("SELECT * FROM workout_exercise WHERE sessionId = :sessionId ORDER BY orderIndex ASC")
    fun getExercisesForSession(sessionId: Int): Flow<List<WorkoutExerciseEntity>>

    @Query("SELECT * FROM workout_exercise WHERE sessionId = :sessionId ORDER BY orderIndex ASC")
    suspend fun getExercisesForSessionOnce(sessionId: Int): List<WorkoutExerciseEntity>

    @Insert
    suspend fun insert(exercise: WorkoutExerciseEntity): Long

    @Insert
    suspend fun insertAll(exercises: List<WorkoutExerciseEntity>)

    @Update
    suspend fun update(exercise: WorkoutExerciseEntity)

    @Delete
    suspend fun delete(exercise: WorkoutExerciseEntity)

    @Query("SELECT DISTINCT exerciseName FROM workout_exercise ORDER BY exerciseName ASC")
    suspend fun getAllExerciseNames(): List<String>
}

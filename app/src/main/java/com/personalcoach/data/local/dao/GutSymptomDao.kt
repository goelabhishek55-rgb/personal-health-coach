package com.personalcoach.data.local.dao

import androidx.room.*
import com.personalcoach.data.local.entity.GutSymptomEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GutSymptomDao {
    @Query("SELECT * FROM gut_symptom WHERE checkInId = :checkInId")
    fun getSymptomsForCheckIn(checkInId: Int): Flow<List<GutSymptomEntity>>

    @Query("SELECT * FROM gut_symptom WHERE checkInId = :checkInId")
    suspend fun getSymptomsForCheckInOnce(checkInId: Int): List<GutSymptomEntity>

    @Insert
    suspend fun insert(symptom: GutSymptomEntity): Long

    @Insert
    suspend fun insertAll(symptoms: List<GutSymptomEntity>)

    @Delete
    suspend fun delete(symptom: GutSymptomEntity)

    @Query("DELETE FROM gut_symptom WHERE checkInId = :checkInId")
    suspend fun deleteAllForCheckIn(checkInId: Int)
}

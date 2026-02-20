package com.personalcoach.data.local.dao

import androidx.room.*
import com.personalcoach.data.local.entity.CustomGutSymptomEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomGutSymptomDao {
    @Query("SELECT * FROM custom_gut_symptom WHERE isActive = 1")
    fun getActiveSymptoms(): Flow<List<CustomGutSymptomEntity>>

    @Query("SELECT * FROM custom_gut_symptom")
    fun getAllSymptoms(): Flow<List<CustomGutSymptomEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(symptom: CustomGutSymptomEntity): Long

    @Update
    suspend fun update(symptom: CustomGutSymptomEntity)

    @Delete
    suspend fun delete(symptom: CustomGutSymptomEntity)
}

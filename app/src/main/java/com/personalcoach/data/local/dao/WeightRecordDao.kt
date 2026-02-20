package com.personalcoach.data.local.dao

import androidx.room.*
import com.personalcoach.data.local.entity.WeightRecordEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WeightRecordDao {
    @Query("SELECT * FROM weight_record ORDER BY dateTime DESC")
    fun getAllWeightRecords(): Flow<List<WeightRecordEntity>>

    @Query("SELECT * FROM weight_record ORDER BY dateTime DESC LIMIT 1")
    suspend fun getLatestWeightRecord(): WeightRecordEntity?

    @Query("SELECT * FROM weight_record WHERE dateTime >= :startDate AND dateTime <= :endDate ORDER BY dateTime ASC")
    fun getWeightRecordsInRange(startDate: Long, endDate: Long): Flow<List<WeightRecordEntity>>

    @Insert
    suspend fun insert(record: WeightRecordEntity): Long

    @Update
    suspend fun update(record: WeightRecordEntity)

    @Delete
    suspend fun delete(record: WeightRecordEntity)
}

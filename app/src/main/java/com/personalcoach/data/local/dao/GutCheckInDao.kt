package com.personalcoach.data.local.dao

import androidx.room.*
import com.personalcoach.data.local.entity.GutCheckInEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface GutCheckInDao {
    @Query("SELECT * FROM gut_check_in ORDER BY dateTime DESC")
    fun getAllCheckIns(): Flow<List<GutCheckInEntity>>

    @Query("SELECT * FROM gut_check_in WHERE dateTime >= :startOfDay AND dateTime < :endOfDay ORDER BY dateTime DESC")
    fun getCheckInsForDay(startOfDay: Long, endOfDay: Long): Flow<List<GutCheckInEntity>>

    @Query("SELECT * FROM gut_check_in WHERE id = :id")
    suspend fun getCheckInById(id: Int): GutCheckInEntity?

    @Query("SELECT * FROM gut_check_in WHERE dateTime >= :startDate AND dateTime <= :endDate ORDER BY dateTime DESC")
    fun getCheckInsInRange(startDate: Long, endDate: Long): Flow<List<GutCheckInEntity>>

    @Insert
    suspend fun insert(checkIn: GutCheckInEntity): Long

    @Update
    suspend fun update(checkIn: GutCheckInEntity)

    @Delete
    suspend fun delete(checkIn: GutCheckInEntity)
}

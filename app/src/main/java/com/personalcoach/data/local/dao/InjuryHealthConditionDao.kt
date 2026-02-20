package com.personalcoach.data.local.dao

import androidx.room.*
import com.personalcoach.data.local.entity.InjuryHealthConditionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface InjuryHealthConditionDao {
    @Query("SELECT * FROM injury_health_condition WHERE isActive = 1")
    fun getActiveConditions(): Flow<List<InjuryHealthConditionEntity>>

    @Query("SELECT * FROM injury_health_condition")
    fun getAllConditions(): Flow<List<InjuryHealthConditionEntity>>

    @Query("SELECT * FROM injury_health_condition WHERE isActive = 1")
    suspend fun getActiveConditionsOnce(): List<InjuryHealthConditionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(condition: InjuryHealthConditionEntity): Long

    @Update
    suspend fun update(condition: InjuryHealthConditionEntity)

    @Delete
    suspend fun delete(condition: InjuryHealthConditionEntity)
}

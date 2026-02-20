package com.personalcoach.domain.repository

import com.personalcoach.domain.model.InjuryHealthCondition
import kotlinx.coroutines.flow.Flow

interface InjuryHealthConditionRepository {
    fun getActiveConditions(): Flow<List<InjuryHealthCondition>>
    fun getAllConditions(): Flow<List<InjuryHealthCondition>>
    suspend fun getActiveConditionsOnce(): List<InjuryHealthCondition>
    suspend fun insert(condition: InjuryHealthCondition): Long
    suspend fun update(condition: InjuryHealthCondition)
    suspend fun delete(condition: InjuryHealthCondition)
}

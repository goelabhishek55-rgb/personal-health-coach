package com.personalcoach.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.personalcoach.data.local.dao.InjuryHealthConditionDao
import com.personalcoach.data.local.entity.InjuryHealthConditionEntity
import com.personalcoach.domain.model.InjuryHealthCondition
import com.personalcoach.domain.repository.InjuryHealthConditionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class InjuryHealthConditionRepositoryImpl @Inject constructor(
    private val dao: InjuryHealthConditionDao
) : InjuryHealthConditionRepository {

    private val gson = Gson()

    override fun getActiveConditions(): Flow<List<InjuryHealthCondition>> =
        dao.getActiveConditions().map { list -> list.map { it.toDomain() } }

    override fun getAllConditions(): Flow<List<InjuryHealthCondition>> =
        dao.getAllConditions().map { list -> list.map { it.toDomain() } }

    override suspend fun getActiveConditionsOnce(): List<InjuryHealthCondition> =
        dao.getActiveConditionsOnce().map { it.toDomain() }

    override suspend fun insert(condition: InjuryHealthCondition): Long =
        dao.insert(condition.toEntity())

    override suspend fun update(condition: InjuryHealthCondition) =
        dao.update(condition.toEntity())

    override suspend fun delete(condition: InjuryHealthCondition) =
        dao.delete(condition.toEntity())

    private fun InjuryHealthConditionEntity.toDomain() = InjuryHealthCondition(
        id = id, name = name, type = type, affectedArea = affectedArea,
        severity = severity, notes = notes,
        riskyExerciseCategories = gson.fromJson(riskyExerciseCategories, object : TypeToken<List<String>>() {}.type) ?: emptyList(),
        riskyExerciseNames = gson.fromJson(riskyExerciseNames, object : TypeToken<List<String>>() {}.type) ?: emptyList(),
        isActive = isActive, createdAt = createdAt, updatedAt = updatedAt
    )

    private fun InjuryHealthCondition.toEntity() = InjuryHealthConditionEntity(
        id = id, name = name, type = type, affectedArea = affectedArea,
        severity = severity, notes = notes,
        riskyExerciseCategories = gson.toJson(riskyExerciseCategories),
        riskyExerciseNames = gson.toJson(riskyExerciseNames),
        isActive = isActive, createdAt = createdAt, updatedAt = updatedAt
    )
}

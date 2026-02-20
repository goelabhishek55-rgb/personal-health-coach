package com.personalcoach.data.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.personalcoach.data.local.dao.UserProfileDao
import com.personalcoach.data.local.entity.UserProfileEntity
import com.personalcoach.domain.model.UserProfile
import com.personalcoach.domain.repository.UserProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserProfileRepositoryImpl @Inject constructor(
    private val dao: UserProfileDao
) : UserProfileRepository {

    private val gson = Gson()

    override fun getUserProfile(): Flow<UserProfile?> =
        dao.getUserProfile().map { it?.toDomain() }

    override suspend fun getUserProfileOnce(): UserProfile? =
        dao.getUserProfileOnce()?.toDomain()

    override suspend fun saveUserProfile(profile: UserProfile) {
        dao.insertOrUpdate(profile.toEntity())
    }

    private fun UserProfileEntity.toDomain() = UserProfile(
        id = id, name = name, age = age, sex = sex, heightCm = heightCm,
        primaryGoals = gson.fromJson(primaryGoals, object : TypeToken<List<String>>() {}.type) ?: emptyList(),
        defaultUnits = defaultUnits, dailyCalorieTarget = dailyCalorieTarget,
        dailyProteinTargetG = dailyProteinTargetG, dailyCarbsTargetG = dailyCarbsTargetG,
        dailyFatsTargetG = dailyFatsTargetG, dailyFiberTargetG = dailyFiberTargetG,
        dailyWaterTargetMl = dailyWaterTargetMl, waterGlassSizeMl = waterGlassSizeMl,
        samsungHealthEnabled = samsungHealthEnabled, onboardingCompleted = onboardingCompleted
    )

    private fun UserProfile.toEntity() = UserProfileEntity(
        id = id, name = name, age = age, sex = sex, heightCm = heightCm,
        primaryGoals = gson.toJson(primaryGoals), defaultUnits = defaultUnits,
        dailyCalorieTarget = dailyCalorieTarget, dailyProteinTargetG = dailyProteinTargetG,
        dailyCarbsTargetG = dailyCarbsTargetG, dailyFatsTargetG = dailyFatsTargetG,
        dailyFiberTargetG = dailyFiberTargetG, dailyWaterTargetMl = dailyWaterTargetMl,
        waterGlassSizeMl = waterGlassSizeMl, samsungHealthEnabled = samsungHealthEnabled,
        onboardingCompleted = onboardingCompleted
    )
}

package com.personalcoach.domain.repository

import com.personalcoach.domain.model.UserProfile
import kotlinx.coroutines.flow.Flow

interface UserProfileRepository {
    fun getUserProfile(): Flow<UserProfile?>
    suspend fun getUserProfileOnce(): UserProfile?
    suspend fun saveUserProfile(profile: UserProfile)
}

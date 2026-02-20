package com.personalcoach.domain.usecase

import com.personalcoach.domain.model.UserProfile
import com.personalcoach.domain.repository.UserProfileRepository
import javax.inject.Inject

class GetOrCreateUserProfileUseCase @Inject constructor(
    private val repository: UserProfileRepository
) {
    suspend operator fun invoke(): UserProfile {
        return repository.getUserProfileOnce() ?: UserProfile()
    }
}

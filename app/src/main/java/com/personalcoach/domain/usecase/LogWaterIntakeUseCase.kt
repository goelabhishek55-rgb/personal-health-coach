package com.personalcoach.domain.usecase

import com.personalcoach.domain.model.WaterIntake
import com.personalcoach.domain.repository.UserProfileRepository
import com.personalcoach.domain.repository.WaterRepository
import com.personalcoach.util.DateUtils
import javax.inject.Inject

class LogWaterIntakeUseCase @Inject constructor(
    private val waterRepository: WaterRepository,
    private val userProfileRepository: UserProfileRepository
) {
    suspend operator fun invoke(additionalMl: Int) {
        val today = DateUtils.startOfDay(System.currentTimeMillis())
        val profile = userProfileRepository.getUserProfileOnce()
        val targetMl = profile?.dailyWaterTargetMl ?: 2500
        val glassSizeMl = profile?.waterGlassSizeMl ?: 250

        val existing = waterRepository.getWaterIntakeForDayOnce(today)
        if (existing != null) {
            waterRepository.saveWaterIntake(
                existing.copy(
                    totalMl = existing.totalMl + additionalMl,
                    glassCount = existing.glassCount + (additionalMl / glassSizeMl)
                )
            )
        } else {
            waterRepository.saveWaterIntake(
                WaterIntake(
                    date = today,
                    totalMl = additionalMl,
                    targetMl = targetMl,
                    glassCount = additionalMl / glassSizeMl
                )
            )
        }
    }
}

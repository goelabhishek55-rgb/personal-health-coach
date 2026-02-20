package com.personalcoach.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.personalcoach.data.local.converter.Converters
import com.personalcoach.data.local.dao.*
import com.personalcoach.data.local.entity.*

@Database(
    entities = [
        UserProfileEntity::class,
        InjuryHealthConditionEntity::class,
        WorkoutSessionEntity::class,
        WorkoutExerciseEntity::class,
        GutCheckInEntity::class,
        GutSymptomEntity::class,
        CustomGutSymptomEntity::class,
        MealLogEntity::class,
        DailyNutritionSummaryEntity::class,
        WaterIntakeEntity::class,
        WeightRecordEntity::class,
        DailySummaryEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun injuryHealthConditionDao(): InjuryHealthConditionDao
    abstract fun workoutSessionDao(): WorkoutSessionDao
    abstract fun workoutExerciseDao(): WorkoutExerciseDao
    abstract fun gutCheckInDao(): GutCheckInDao
    abstract fun gutSymptomDao(): GutSymptomDao
    abstract fun customGutSymptomDao(): CustomGutSymptomDao
    abstract fun mealLogDao(): MealLogDao
    abstract fun dailyNutritionSummaryDao(): DailyNutritionSummaryDao
    abstract fun waterIntakeDao(): WaterIntakeDao
    abstract fun weightRecordDao(): WeightRecordDao
    abstract fun dailySummaryDao(): DailySummaryDao
}

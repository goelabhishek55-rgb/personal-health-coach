package com.personalcoach.di

import android.content.Context
import androidx.room.Room
import com.personalcoach.data.local.AppDatabase
import com.personalcoach.data.local.dao.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "personal_coach.db").build()

    @Provides
    fun provideUserProfileDao(db: AppDatabase): UserProfileDao = db.userProfileDao()

    @Provides
    fun provideInjuryHealthConditionDao(db: AppDatabase): InjuryHealthConditionDao =
        db.injuryHealthConditionDao()

    @Provides
    fun provideWorkoutSessionDao(db: AppDatabase): WorkoutSessionDao = db.workoutSessionDao()

    @Provides
    fun provideWorkoutExerciseDao(db: AppDatabase): WorkoutExerciseDao = db.workoutExerciseDao()

    @Provides
    fun provideGutCheckInDao(db: AppDatabase): GutCheckInDao = db.gutCheckInDao()

    @Provides
    fun provideGutSymptomDao(db: AppDatabase): GutSymptomDao = db.gutSymptomDao()

    @Provides
    fun provideCustomGutSymptomDao(db: AppDatabase): CustomGutSymptomDao = db.customGutSymptomDao()

    @Provides
    fun provideMealLogDao(db: AppDatabase): MealLogDao = db.mealLogDao()

    @Provides
    fun provideDailyNutritionSummaryDao(db: AppDatabase): DailyNutritionSummaryDao =
        db.dailyNutritionSummaryDao()

    @Provides
    fun provideWaterIntakeDao(db: AppDatabase): WaterIntakeDao = db.waterIntakeDao()

    @Provides
    fun provideWeightRecordDao(db: AppDatabase): WeightRecordDao = db.weightRecordDao()

    @Provides
    fun provideDailySummaryDao(db: AppDatabase): DailySummaryDao = db.dailySummaryDao()
}

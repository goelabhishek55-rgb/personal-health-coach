package com.personalcoach.di

import android.content.Context
import com.personalcoach.data.health.HealthConnectDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object HealthModule {

    @Provides
    @Singleton
    fun provideHealthConnectDataSource(@ApplicationContext context: Context): HealthConnectDataSource =
        HealthConnectDataSource(context)
}

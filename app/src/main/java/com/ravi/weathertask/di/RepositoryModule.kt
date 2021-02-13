package com.ravi.weathertask.di


import com.ravi.weathertask.NetworkApiService
import com.ravi.weathertask.repository.WeatherRepository
import com.ravi.weathertask.repository.local.WeatherDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Singleton
    @Provides
    fun provideSnsRepository(
        networkApiService: NetworkApiService,
        weatherDao :WeatherDao
    ): WeatherRepository {
        return WeatherRepository(networkApiService,weatherDao)
    }
}
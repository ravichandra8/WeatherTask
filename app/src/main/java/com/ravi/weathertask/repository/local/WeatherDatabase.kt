package com.ravi.weathertask.repository.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LocationEntity::class], version = 3)
abstract class WeatherDatabase: RoomDatabase() {
    abstract fun weatherDao(): WeatherDao

    companion object{
        const val DATABASE_NAME: String = "wealth_db"
    }
}
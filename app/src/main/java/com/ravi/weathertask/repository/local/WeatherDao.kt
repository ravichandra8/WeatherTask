package com.ravi.weathertask.repository.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(locationEntity : LocationEntity): Long

    @Query("SELECT * FROM location")
    suspend fun getcityList(): List<LocationEntity>

    @Query("Delete FROM location WHERE id = :id")
    suspend fun deleteCityBasedOnId(id:Int)

}
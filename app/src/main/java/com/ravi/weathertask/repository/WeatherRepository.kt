package com.ravi.weathertask.repository

import com.ravi.weathertask.repository.remote.NetworkApiService
import com.ravi.weathertask.repository.local.LocationEntity
import com.ravi.weathertask.repository.local.WeatherDao
import com.ravi.weathertask.repository.remote.ForecastParentResponse
import retrofit2.Response

class WeatherRepository
    constructor(private val networkApiService: NetworkApiService, private val weatherDao: WeatherDao) {

        suspend fun addCity(locationEntity: LocationEntity):Long {
         return   weatherDao.insert(locationEntity)
        }


        suspend fun getCityList() = weatherDao.getcityList()

        suspend fun deleteCityBasedOnId(id:Int) = weatherDao.deleteCityBasedOnId(id)

        suspend fun deleteAllCities() = weatherDao.deleteAllCities()

        suspend fun getForecastDetails(lat:String,lng:String) :Response<ForecastParentResponse> =
            networkApiService.getForecastInformation(lat,lng)
}
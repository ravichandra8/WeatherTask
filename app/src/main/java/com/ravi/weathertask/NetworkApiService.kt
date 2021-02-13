package com.ravi.weathertask

import com.ravi.weathertask.repository.remote.ForecastParentResponse
import com.ravi.weathertask.utils.Constants
import retrofit2.Response
import retrofit2.http.*

interface NetworkApiService{

    @GET("data/2.5/forecast? ")
    suspend fun getForecastInformation(@Query("lat")  lat:String,@Query("lon") lon:String,@Query("appid") appId:String=Constants.APP_ID) : Response<ForecastParentResponse>

}
package com.ravi.weathertask.repository.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ForecastParentResponse(
    @SerializedName("cod")
    @Expose
    val cod: String,
    @SerializedName("message")
    @Expose
    val message: Int,
    @SerializedName("cnt")
    @Expose
    val cnt: Int,
    @SerializedName("list")
    @Expose
    val weatherList: List<WeatherList> ? = null
)
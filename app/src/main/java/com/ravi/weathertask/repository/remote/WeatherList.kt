package com.ravi.weathertask.repository.remote

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class WeatherList(
    @SerializedName("dt")
    @Expose
    val dt: Int,
    @SerializedName("main")
    @Expose
    val main: Main,
    @SerializedName("weather")
    @Expose
    val weather: List<Weather>? = null,
    @SerializedName("wind")
    @Expose
    val wind: Wind,
    @SerializedName("visibility")
    @Expose
    val visibility: Int,
    @SerializedName("dt_txt")
    @Expose
    val dtTxt: String
)

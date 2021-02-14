package com.ravi.weathertask.ui.fragment.city

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravi.weathertask.repository.WeatherRepository
import com.ravi.weathertask.repository.remote.ForecastParentResponse
import com.ravi.weathertask.repository.remote.WeatherList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class CityViewModel
@Inject
constructor(private val weatherRepository: WeatherRepository) : ViewModel() {
    var pos: Int = 0

     val weatherForestParentResponse: MutableLiveData<ForecastParentResponse> =
        MutableLiveData<ForecastParentResponse>()
    var weatherResponse: MutableLiveData<WeatherList> = MutableLiveData<WeatherList>()
    var showBackNavButton: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var hideForwardNavButton: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    fun getForecastInfo(lat: String, lng: String) {
        if(weatherForestParentResponse.value == null) {
            viewModelScope.launch {
                val response: Response<ForecastParentResponse> =
                    weatherRepository.getForecastDetails(lat, lng)
                if (response.isSuccessful) {
                    weatherForestParentResponse.value = response.body()
                    weatherResponse.value = response.body()?.weatherList?.get(pos)
                    showBackNavButton.value = false
                    hideForwardNavButton.value = response.body()?.weatherList?.size == 0
                } else {
                    Log.d("response",response.message())
                }
            }
        }
    }

    fun getNextForecastInfo() {
        pos = pos.plus(1)
        if (pos < weatherForestParentResponse.value?.weatherList?.size!!) {
            weatherResponse.value = weatherForestParentResponse.value?.weatherList?.get(pos)
            showBackNavButton.value = true
        }

        hideForwardNavButton.value = (pos == weatherForestParentResponse.value?.weatherList?.size?.minus(1)!!)

    }

    fun getBackForecastInfo() {

        pos = pos.minus(1)
        if (pos >= 0) {
            weatherResponse.value = weatherForestParentResponse.value!!.weatherList?.get(pos)
            hideForwardNavButton.value = false
        }
        if (pos == 0) {
            showBackNavButton.value = false
        }
    }
}
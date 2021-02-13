package com.ravi.weathertask.ui.fragment.city

import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravi.weathertask.repository.WeatherRepository
import com.ravi.weathertask.repository.local.LocationEntity
import com.ravi.weathertask.repository.remote.ForecastParentResponse
import com.ravi.weathertask.repository.remote.Weather
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
    init {
        pos = 0
    }

    private val weatherResponseList: MutableLiveData<ForecastParentResponse> =
        MutableLiveData<ForecastParentResponse>()
    var weatherResponse: MutableLiveData<WeatherList> = MutableLiveData<WeatherList>()
    var hideBackNavButton: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    var hideForwardNavButton: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    fun getForecastInfo(lat: String, lng: String) {
        if(weatherResponseList.value == null) {
            viewModelScope.launch {
                val response: Response<ForecastParentResponse> =
                    weatherRepository.getForecastDetails(lat, lng)
                if (response.isSuccessful) {
                    weatherResponseList.value = response.body()
                    weatherResponse.value = response.body()?.weatherList?.get(pos)
                    hideBackNavButton.value = false
                    hideForwardNavButton.value = response.body()?.weatherList?.size == 0
                }
            }
        }
    }

    fun getNextForecastInfo() {
        if (pos <= weatherResponseList.value?.weatherList?.size!!) {
            pos = pos.plus(1)
            weatherResponse.value = weatherResponseList.value?.weatherList?.get(pos)
            if (pos == weatherResponseList.value?.weatherList?.size?.minus(1)!!)
                hideForwardNavButton.value = true
        }
        hideBackNavButton.value = true
    }

    fun getBackForecastInfo() {
        if (pos >= 0) {
            pos = pos.minus(1)
            weatherResponse.value = weatherResponseList.value!!.weatherList?.get(pos)
            if (pos == 0) {
                hideBackNavButton.value = false
            }
            hideForwardNavButton.value = false
        }
    }
}
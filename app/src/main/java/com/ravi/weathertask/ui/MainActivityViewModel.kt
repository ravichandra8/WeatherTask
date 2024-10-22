package com.ravi.weathertask.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ravi.weathertask.repository.WeatherRepository
import com.ravi.weathertask.repository.local.LocationEntity
import com.ravi.weathertask.repository.remote.ForecastParentResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class MainActivityViewModel
@Inject
constructor(private val weatherRepository: WeatherRepository) : ViewModel(){

    val cityListMutableLiveData = MutableLiveData<List<LocationEntity>>()
    val bookmarkEmpty = MutableLiveData<Boolean>()

    fun saveLocation(locationEntity: LocationEntity){
             viewModelScope.launch {
           weatherRepository.addCity(locationEntity)
       }
    }

    fun getLocations() {
        viewModelScope.launch {
           val locationList :List<LocationEntity> = weatherRepository.getCityList()
            if(locationList.isNotEmpty()){
                cityListMutableLiveData.value = locationList
            }
            checkCityListEmpty()
        }
    }

    fun deleteBookmark(id:Int){
        viewModelScope.launch {
            weatherRepository.deleteCityBasedOnId(id)
            checkCityListEmpty()
        }
    }

    fun checkCityListEmpty() {
       viewModelScope.launch {
           val locationList: List<LocationEntity> = weatherRepository.getCityList()
           bookmarkEmpty.value = locationList.isEmpty()
       }
    }

    fun deleteAllBookmarks() {
        viewModelScope.launch {
            weatherRepository.deleteAllCities()
            checkCityListEmpty()
        }
    }

}
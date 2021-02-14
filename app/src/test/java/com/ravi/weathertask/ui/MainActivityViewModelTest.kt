package com.ravi.weathertask.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.ravi.weathertask.repository.WeatherRepository
import com.ravi.weathertask.repository.local.LocationEntity
import com.ravi.weathertask.utils.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainActivityViewModelTest{

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var weatherRepository: WeatherRepository

    private lateinit var mainActivityViewModel: MainActivityViewModel

    @Mock
    private lateinit var cityListObserver: Observer<List<LocationEntity>>

    @Mock
    private lateinit var bookmarkEmptyObserver: Observer<Boolean>

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Before
    fun setUp(){
         mainActivityViewModel = MainActivityViewModel(weatherRepository)
        mainActivityViewModel.cityListMutableLiveData.observeForever(cityListObserver)
        mainActivityViewModel.bookmarkEmpty.observeForever(bookmarkEmptyObserver)

    }

    @Test
    fun test_saveLocation(){
        testCoroutineRule.runBlockingTest {
            val locationEntity = LocationEntity(latitude = 17.45,longitude = 78.45,city="Hyd")

            mainActivityViewModel.saveLocation(locationEntity)
            verify(weatherRepository).addCity(locationEntity)
        }
    }

    @Test
    fun test_getLocations_WithCityList(){
        testCoroutineRule.runBlockingTest {

            val locationEntity = LocationEntity(city = "Hyd", latitude = 17.45, longitude = 78.45)
            val locationList = mutableListOf<LocationEntity>()
            locationList.add(locationEntity)

            Mockito.doReturn(locationList)
                .`when`(weatherRepository)
                .getCityList()

            mainActivityViewModel.getLocations()
            verify(weatherRepository, times(2)).getCityList()

            verify(cityListObserver).onChanged(locationList)
            verify(bookmarkEmptyObserver).onChanged(false)

        }
    }

    @Test
    fun test_getLocations_EmptyCityList(){
        testCoroutineRule.runBlockingTest {

            val locationList = mutableListOf<LocationEntity>()

            Mockito.doReturn(locationList)
                .`when`(weatherRepository)
                .getCityList()

            mainActivityViewModel.getLocations()
            verify(weatherRepository, times(2)).getCityList()

            verify(bookmarkEmptyObserver).onChanged(true)

        }
    }
    @Test
    fun test_deleteLocation_EmptyCityList(){
        testCoroutineRule.runBlockingTest {

            val locationList = mutableListOf<LocationEntity>()

            Mockito.doReturn(locationList)
                .`when`(weatherRepository)
                .getCityList()

            mainActivityViewModel.deleteBookmark(1)
            verify(weatherRepository).deleteCityBasedOnId(1)

            verify(bookmarkEmptyObserver).onChanged(true)

        }
    }

    @Test
    fun test_deleteLocation_WithList(){
        testCoroutineRule.runBlockingTest {

            val locationEntity = LocationEntity(city = "Hyd", latitude = 17.45, longitude = 78.45)
            val locationList = mutableListOf<LocationEntity>()
            locationList.add(locationEntity)

            Mockito.doReturn(locationList)
                .`when`(weatherRepository)
                .getCityList()

            mainActivityViewModel.deleteBookmark(1)
            verify(weatherRepository).deleteCityBasedOnId(1)

            verify(bookmarkEmptyObserver).onChanged(false)
        }
    }

    @Test
    fun test_deleteAllLocations(){
        testCoroutineRule.runBlockingTest {

            val locationList = mutableListOf<LocationEntity>()

            Mockito.doReturn(locationList)
                .`when`(weatherRepository)
                .getCityList()

            mainActivityViewModel.deleteAllBookmarks()
            verify(weatherRepository).deleteAllCities()

            verify(bookmarkEmptyObserver).onChanged(true)

        }
    }
}
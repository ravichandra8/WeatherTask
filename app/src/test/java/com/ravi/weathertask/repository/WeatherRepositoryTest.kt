package com.ravi.weathertask.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ravi.weathertask.repository.remote.NetworkApiService
import com.ravi.weathertask.repository.local.LocationEntity
import com.ravi.weathertask.repository.local.WeatherDao
import com.ravi.weathertask.repository.remote.ForecastParentResponse
import com.ravi.weathertask.utils.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.doReturn
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class WeatherRepositoryTest {

    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var networkApiService: NetworkApiService
    @Mock
    private lateinit var weatherDao: WeatherDao

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    private lateinit var weatherRepository: WeatherRepository

    @Before
    fun setup(){
        weatherRepository = WeatherRepository(networkApiService,weatherDao)
    }

    @Test
    fun test_addcity(){
       testCoroutineRule.runBlockingTest {

           val locationEntity = LocationEntity(city = "Hyd", latitude = 17.45, longitude = 78.45)

           doReturn(1)
               .`when`(weatherDao)
               .insert(locationEntity)

           Assert.assertEquals(1, weatherRepository.addCity(locationEntity))

       }
    }
    @Test
    fun test_getCity(){
        testCoroutineRule.runBlockingTest {

            val locationEntity = LocationEntity(city = "Hyd", latitude = 17.45, longitude = 78.45)
            val locationList = mutableListOf<LocationEntity>()
            locationList.add(locationEntity)

            doReturn(locationList)
                .`when`(weatherDao)
                .getcityList()

            Assert.assertEquals("Hyd", weatherRepository.getCityList()[0].city)

        }
    }
    @Test
    fun test_deleteCityBasedOnId(){
        testCoroutineRule.runBlockingTest {
            weatherRepository.deleteCityBasedOnId(1)
            verify(weatherDao).deleteCityBasedOnId(1)
        }
    }

    @Test
    fun test_deleteAllCities(){
        testCoroutineRule.runBlockingTest {
            weatherRepository.deleteAllCities()
            verify(weatherDao).deleteAllCities()
        }
    }
    @Test
    fun test_getForecastDetails(){
        testCoroutineRule.runBlockingTest {
            val forecastParentResponse = ForecastParentResponse("200",1,1)

            doReturn(forecastParentResponse)
                .`when`(networkApiService)
                .getForecastInformation("17.3453","78.098")

            Assert.assertEquals(forecastParentResponse, weatherRepository.getForecastDetails("17.3453","78.098"))

        }
    }

}
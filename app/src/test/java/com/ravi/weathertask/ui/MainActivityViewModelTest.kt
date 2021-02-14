package com.ravi.weathertask.ui

import android.location.Location
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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

//    @Mock
//    private lateinit var locationEntity: LocationEntity

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Before
    fun setUp(){
         mainActivityViewModel = MainActivityViewModel(weatherRepository)
    }

    @Test
    fun test_saveLocation(){
        testCoroutineRule.runBlockingTest {
            val locationEntity = LocationEntity(latitude = 17.45,longitude = 78.45,city="Hyd")

            mainActivityViewModel.saveLocation(locationEntity)
            verify(weatherRepository).addCity(locationEntity)
        }

    }

}
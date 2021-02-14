package com.ravi.weathertask.ui.fragment.city

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.ravi.weathertask.repository.WeatherRepository
import com.ravi.weathertask.repository.remote.ForecastParentResponse
import com.ravi.weathertask.repository.remote.Main
import com.ravi.weathertask.repository.remote.WeatherList
import com.ravi.weathertask.repository.remote.Wind
import com.ravi.weathertask.utils.TestCoroutineRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CityViewModelTest {
    @get:Rule
    val testInstantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var weatherRepository: WeatherRepository

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()
    @Mock
    private lateinit var weatherForestParentResponseObserver: Observer<ForecastParentResponse>

    @Mock
    private lateinit var weatherResponseObserver: Observer<WeatherList>

    @Mock
    private lateinit var showBackNavButtonObserver: Observer<Boolean>

    @Mock
    private lateinit var hideForwardNavButtonObserver: Observer<Boolean>

    private lateinit var cityViewModel: CityViewModel

    @Before
    fun setUp(){
        cityViewModel = CityViewModel(weatherRepository)
        cityViewModel.weatherResponse.observeForever(weatherResponseObserver)
        cityViewModel.hideForwardNavButton.observeForever(hideForwardNavButtonObserver)
        cityViewModel.showBackNavButton.observeForever(showBackNavButtonObserver)
        cityViewModel.weatherForestParentResponse.observeForever(weatherForestParentResponseObserver)
    }

    @Test
    fun test_getForecastInfo_success(){
        testCoroutineRule.runBlockingTest {

              Mockito.doReturn(Response.success(200,getForecastParentResponse()))
                .`when`(weatherRepository)
                .getForecastDetails("17.453", "78.34")

            cityViewModel.getForecastInfo("17.453", "78.34")
            verify(weatherRepository).getForecastDetails("17.453", "78.34")
            verify(weatherForestParentResponseObserver).onChanged(getForecastParentResponse())
            verify(showBackNavButtonObserver).onChanged(false)
            verify(hideForwardNavButtonObserver).onChanged(false)

        }

    }

    @Test
    fun test_getNextForecastInfo(){
        test_getForecastInfo_success()
        cityViewModel.getNextForecastInfo()
        verify(showBackNavButtonObserver).onChanged(true)
        verify(hideForwardNavButtonObserver).onChanged(false)

    }

    @Test
    fun test_getBackForecastInfo(){
        test_getForecastInfo_success()
        cityViewModel.getBackForecastInfo()
        verify(hideForwardNavButtonObserver).onChanged(false)
    }


    private fun getForecastParentResponse(): ForecastParentResponse {
        val main = Main(76.5, 55.4, 34.3, 55.3, 3, 3, 3, 3, 3.3)
        val weatherList = WeatherList(1, main, null, Wind(65.4, 3), 5, "23-2-2018")
        val weatherList1 = WeatherList(1, main, null, Wind(65.4, 3), 5, "23-2-2018")
        var weatherMutableList: MutableList<WeatherList> = mutableListOf(weatherList)
           weatherMutableList.add(weatherList1)
        return ForecastParentResponse("200", 1, 1, weatherMutableList)

    }

}
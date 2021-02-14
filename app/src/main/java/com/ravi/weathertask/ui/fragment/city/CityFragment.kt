package com.ravi.weathertask.ui.fragment.city

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.ravi.weathertask.R
import com.ravi.weathertask.databinding.FragmentCityBinding
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class CityFragment : Fragment() {

    private lateinit var cityViewModel: CityViewModel
    private lateinit var fragmentCityBinding: FragmentCityBinding
    val args: CityFragmentArgs by navArgs()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentCityBinding = FragmentCityBinding.inflate(inflater, container, false)
        return fragmentCityBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        cityViewModel = ViewModelProvider(this).get(CityViewModel::class.java)


        lifecycleScope.launchWhenStarted {
            cityViewModel.getForecastInfo(args.latitude, args.longitude)

            fragmentCityBinding.back.setOnClickListener {
                cityViewModel.getBackForecastInfo()
            }
            fragmentCityBinding.forward.setOnClickListener {
                cityViewModel.getNextForecastInfo()
            }
            activity?.let {
                cityViewModel.weatherResponse.observe(it, { response ->

                    if (response.weather?.get(0)?.description == "clear sky" || response.weather?.get(
                            0
                        )?.description == "mist"
                    ) {
                        fragmentCityBinding.image.setImageResource(R.drawable.clouds)

                    } else if (response.weather?.get(0)?.description == "rain") {

                        fragmentCityBinding.image.setImageResource(R.drawable.rain)
                    } else {
                        fragmentCityBinding.image.setImageResource(R.drawable.haze)
                    }
                    fragmentCityBinding.description.text =
                        response.weather?.get(0)?.description
//                    fragmentCityBinding.name.text=response.name
                    fragmentCityBinding.degree.text =
                        response.wind.deg.toString()
                    fragmentCityBinding.speed.text =
                        response.wind.speed.toString()
                    fragmentCityBinding.temp.text =
                        response.main.temp.toString()
                    fragmentCityBinding.humidity.text =
                        response.main.humidity.toString()
                    fragmentCityBinding.date.text = response.dtTxt.split(" ").toTypedArray()[0]
                    fragmentCityBinding.timeTxt.text = getFormatedDateTime(
                        response.dtTxt.split(" ").toTypedArray()[1],
                        "HH:mm:ss",
                        "HH:mm"
                    )
                })
                cityViewModel.showBackNavButton.observe(it, { state ->
                    fragmentCityBinding.back.isClickable = state
                    if (state) {
                        fragmentCityBinding.back.setImageResource(R.drawable.ic_baseline_arrow_back_white_ios_24)
                    } else {
                        fragmentCityBinding.back.setImageResource(R.drawable.ic_baseline_arrow_back_gray_ios_24)
                    }
                })

                cityViewModel.hideForwardNavButton.observe(it, { state ->
                    fragmentCityBinding.forward.isClickable = !state
                    if (!state) {
                        fragmentCityBinding.forward.setImageResource(R.drawable.ic_baseline_arrow_forward_white_ios_24)
                    } else {
                        fragmentCityBinding.forward.setImageResource(R.drawable.ic_baseline_arrow_forward_gray_ios_24)
                    }
                })
            }
        }
    }

    private fun getFormatedDateTime(
        dateStr: String,
        strReadFormat: String,
        strWriteFormat: String
    ): String {

        var formattedDate: String = dateStr

        val readFormat: DateFormat = SimpleDateFormat(strReadFormat, Locale.getDefault());
        val writeFormat: DateFormat = SimpleDateFormat(strWriteFormat, Locale.getDefault());

        var date: Date? = null

        readFormat.parse(dateStr).also { date = it }

        if (date != null) {
            formattedDate = writeFormat.format(date);
        }

        return formattedDate;
    }
}
package com.ravi.weathertask.ui.fragment.addlocations

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.ravi.weathertask.ui.MainActivityViewModel
import com.ravi.weathertask.R
import com.ravi.weathertask.utils.LocationAddress
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class AddLocationFragment : Fragment() {
    private lateinit var googleMap: GoogleMap
    private lateinit var mainActivityViewModel: MainActivityViewModel
    private val callback = OnMapReadyCallback { googleMap ->

        this.googleMap = googleMap
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        val sydney = LatLng(17.403926158764616,78.47301498055457)
//        googleMap.uiSettings.isScrollGesturesEnabled = true
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,7f))
        googleMap.setOnMapClickListener {
            Log.d("sagu",it.latitude.toString())
            googleMap.addMarker(MarkerOptions().position(it).title("B1"))
            getAddress(it)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_add_location, container, false)

        mainActivityViewModel  = ViewModelProvider(this).get(MainActivityViewModel::class.java)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        mainActivityViewModel.getLocations()

        activity?.let {
            mainActivityViewModel.mutableLiveData.observe(it, {
                googleMap.clear()
                it.map { locationEnitity ->
                    googleMap.addMarker(MarkerOptions().position(LatLng(locationEnitity.latitude,locationEnitity.longitude)))
                }

            })
        }
    }

    private fun getAddress(latLng:LatLng){
        lifecycleScope.launchWhenStarted {
            activity?.let {
                withContext(Dispatchers.Default) {
                    var city =
                        LocationAddress.getCityFromLatLng(latLng.latitude, latLng.longitude, it)
                    city?.let { city ->
                        withContext(Dispatchers.IO) {
                            mainActivityViewModel.saveLocation(
                                latLng.latitude,
                                latLng.longitude,
                                city
                            )
                        }
                    }
                }
            }
        }
    }
}
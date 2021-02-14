package com.ravi.weathertask.ui.fragment.addlocations

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.fragment.app.Fragment

import android.os.Bundle
import android.text.BoringLayout.make
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.make
import com.ravi.weathertask.ui.MainActivityViewModel
import com.ravi.weathertask.R
import com.ravi.weathertask.databinding.FragmentAddLocationBinding
import com.ravi.weathertask.databinding.FragmentCityBinding
import com.ravi.weathertask.repository.local.LocationEntity
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
    private lateinit var fragmentAddLocationBinding: FragmentAddLocationBinding
    private val callback = OnMapReadyCallback { googleMap ->

        this.googleMap = googleMap

        val sydney = LatLng(17.403926158764616, 78.47301498055457)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 7f))
        googleMap.setOnMapClickListener {
            googleMap.addMarker(MarkerOptions().position(it).title("B1"))
            getAddress(it)
        }

        displayMarkersBasedonLocation()
        clearLocations()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentAddLocationBinding = FragmentAddLocationBinding.inflate(inflater, container, false)
        return fragmentAddLocationBinding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        fragmentAddLocationBinding.fab.setOnClickListener {
            Navigation.findNavController(fragmentAddLocationBinding.root).navigate(R.id.action_addLocationFragment_to_locationBookmarkFragment)

        }
        mainActivityViewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        mainActivityViewModel.getLocations()


    }

    private fun clearLocations(){
        lifecycleScope.launchWhenStarted {
            activity?.let {
                mainActivityViewModel.bookmarkEmpty.observe(it,{ flag ->
                    if(flag) {
                        googleMap.clear()
                    }
                })
            }
            }
    }

    private fun displayMarkersBasedonLocation() {
        lifecycleScope.launchWhenStarted {
            activity?.let {
                mainActivityViewModel.cityListMutableLiveData.observe(it, {
                    googleMap.clear()
                    it.map { locationEnitity ->
                        googleMap.addMarker(
                            MarkerOptions().position(
                                LatLng(
                                    locationEnitity.latitude,
                                    locationEnitity.longitude
                                )
                            )
                        )
                    }

                })
            }
        }
    }

    private fun getAddress(latLng: LatLng) {
        lifecycleScope.launchWhenStarted {
            activity?.let {
                withContext(Dispatchers.Default) {
                    var city =
                        LocationAddress.getCityFromLatLng(latLng.latitude, latLng.longitude, it)
                    if (city != null) {
                        withContext(Dispatchers.IO) {
                            mainActivityViewModel.saveLocation(
                                LocationEntity(
                                    latitude = latLng.latitude,
                                    longitude = latLng.longitude,
                                    city = city
                                )
                            )
                            withContext(Dispatchers.Main) {
                                Snackbar.make(
                                    fragmentAddLocationBinding.root,
                                    "Location saved.",
                                    Snackbar.LENGTH_LONG
                                ).show()
                            }
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Snackbar.make(
                                fragmentAddLocationBinding.root,
                                "Unable to fetch your location. Please check internet and try again",
                                Snackbar.LENGTH_LONG
                            ).show()
                        }
                        displayMarkersBasedonLocation()

                    }
                }
            }
        }
    }
}
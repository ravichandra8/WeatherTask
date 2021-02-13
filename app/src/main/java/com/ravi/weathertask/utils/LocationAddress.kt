package com.ravi.weathertask.utils

import android.content.Context
import android.location.Geocoder
import java.util.*

object LocationAddress {

    fun getCityFromLatLng(latitude: Double,longitude: Double,context: Context): String? {
        val geoCoder = Geocoder(
            context,
            Locale.getDefault()
        )
        val addressList = geoCoder.getFromLocation(
            latitude, longitude, 1
        )
        if(addressList != null && addressList.size > 0){
            return if(addressList[0].locality != null){
                addressList[0].locality
            } else{
                addressList[0].subAdminArea
            }
        }
        return null
    }
}
package com.hellomydoc.chat.presentation.userInterface.values

import com.google.android.gms.maps.model.LatLng

fun getMapImage(latLng: LatLng?, zoom: Int = 18): String {
    if(latLng==null){
        return ""
    }
    return "https://maps.google.com/maps/api/staticmap?center=${latLng.latitude},${latLng.longitude}&zoom=$zoom&size=200x200&scale=2&maptype=roadmap&format=jpg&markers=color:red%7c${latLng.latitude},${latLng.longitude}&key=AIzaSyDotqQOvqWH25D5mNkE6vNpE_ktvZr_Ffc"
}
package com.ravimaurya.urjanext.domain.model

import com.google.android.gms.maps.model.LatLng


data class EVStation(
    val name: String?,
    val location: LatLng,
    val address: String? = "",
    val availableChargers: Int
)
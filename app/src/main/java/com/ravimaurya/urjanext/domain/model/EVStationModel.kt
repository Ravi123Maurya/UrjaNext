package com.ravimaurya.urjanext.domain.model

import com.google.android.gms.maps.model.LatLng


data class EVStation(
    val name: String?,
    var location: LatLng,
    val address: String? = "",
    val availableChargers: Int
)



var dummyEVStationList = listOf(
    EVStation(
        name = "Urja Power Station",
        location = LatLng(234.23,23.4),
        address = "",
        availableChargers = (0..10).random()
    ), EVStation(
        name = "VoltVantage",
        location = LatLng(234.23,23.4),
        address = "",
        availableChargers = (0..10).random()
    ), EVStation(
        name = "PowerPulse Station",
        location = LatLng(234.23,23.4),
        address = "",
        availableChargers = (0..10).random()
    ), EVStation(
        name = "GreenAmp Charging",
        location = LatLng(234.23,23.4),
        address = "",
        availableChargers = (0..10).random()
    ), EVStation(
        name = "WattWay Express",
        location = LatLng(234.23,23.4),
        address = "",
        availableChargers = (0..10).random()
    ), EVStation(
        name = "ZapPoint Network",
        location = LatLng(234.23,23.4),
        address = "",
        availableChargers = (0..10).random()
    ), EVStation(
        name = "ChargeStream Plaza",
        location = LatLng(234.23,23.4),
        address = "",
        availableChargers = (0..10).random()
    ),
)

//fun getDummyEVStationList(searchLocation: LatLng) : List<EVStation>{
//
//    val list = dummyEVStationList.map { it.location = searchLocation }
//    return list
//}


// Current Location = 19.1885456, 73.0504882

val dummyLocationsNearThane = listOf(
    LatLng(0.0085113,0.0042871),
    LatLng(0.0095113,0.0002871),
    LatLng(0.0025113,0.0012871),
    LatLng(0.0035113,0.0062871),
    LatLng(0.0065113,0.0042871),
    LatLng(0.0075113,0.0082871),
)


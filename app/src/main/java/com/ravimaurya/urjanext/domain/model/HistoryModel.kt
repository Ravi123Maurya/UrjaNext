package com.ravimaurya.urjanext.domain.model



data class HistoryModel(
    val stationName: String = "",
    val stationLocation: String = "",
    val chargingDate: String = "",
    val amountPaid: Double = 0.0,
    val energyConsumed: Double = 0.0
)




val stationName = listOf(
    "Urja Power Station",
    "VoltVantage",
    "PowerPulse Station",
    "GreenAmp Charging",
    "WattWay Express",
    "EcoVolt Station",
    "CurrentConnect",
    "ZapPoint Network",
    "ChargeStream Plaza",
    "EnergyFlow Terminus"
)
val stationLocations = listOf(
    "Bandra-Worli Sea Link, Mumbai",
    "FC Road, Pune",
    "Hinjewadi IT Park, Pune",
    "Juhu Beach, Mumbai",
    "MG Road, Nashik",
    "Raigad Fort Highway, Raigad",
    "Aurangabad-Ellora Road, Aurangabad",
    "Nagpur-Mumbai Expressway (Samruddhi Mahamarg), Nagpur",
    "Lonavala Hill Station, Lonavala",
    "Shirdi Temple Road, Shirdi"
)
val chargingDates = listOf(
    "7 March",
    "12 March",
    "21 March",
    "4 April",
    "15 April",
    "29 April",
    "10 May",
    "22 May",
    "8 June",
    "17 June"
)
val amountAndEnergy = listOf(
    Pair(485.50, 18.7),
    Pair(320.75, 12.3),
    Pair(156.25, 6.0),
    Pair(624.00, 24.0),
    Pair(390.50, 15.0),
    Pair(208.30, 8.0),
    Pair(572.80, 22.0),
    Pair(416.10, 16.0),
    Pair(260.00, 10.0),
    Pair(755.30, 29.0)
)

val dummyHistoryList = List(stationName.size){index ->
    HistoryModel(
        stationName = stationName[index],
        stationLocation = stationLocations[index],
        chargingDate = chargingDates[index],
        amountPaid = amountAndEnergy[index].first,
        energyConsumed = amountAndEnergy[index].second
    )
}

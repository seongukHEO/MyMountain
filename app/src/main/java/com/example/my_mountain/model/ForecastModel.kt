package com.example.my_mountain.model

data class ForecastModel (
    val fcstDate:String?,
    val fcstTime:String?,

    var temperature:Double = 0.0,
    var sky:String = "",
    var precipitation: Int = 0,
    var precipitationType:String = ""
)
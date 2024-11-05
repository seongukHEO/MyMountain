package com.example.my_mountain.retrofit


import com.example.my_mountain.model.WeatherApiModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitWeatherService {

    @GET("getUltraSrtNcst")
    fun getWeather(
        @Query("serviceKey") serviceKey:String,
        @Query("numOfRows") numOfRows: Int,
        @Query("pageNo") pageNo: Int,
        @Query("base_date") base_date: Int,
        @Query("base_time") base_time:Int,
        @Query("nx") nx:Int,
        @Query("ny") ny:Int
    ) : Call<WeatherApiModel>
}
package com.example.my_mountain.retrofit


import com.example.my_mountain.model.WeatherApiModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitWeatherService {

    @GET("mountListSearch")
    fun getWeather(
        @Query("serviceKey") ServiceKey:String,
        @Query("pageNo") PageNo: Int,
        @Query("numOfRows") numOfRows: Int,
        @Query("_type") _type: String,
        @Query("localArea") localArea:Int,
        @Query("obsid") obsid:Int,
        @Query("tm") tm:String
    ) : Call<WeatherApiModel>
}
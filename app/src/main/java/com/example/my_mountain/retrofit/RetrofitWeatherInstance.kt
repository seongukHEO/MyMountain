package com.example.my_mountain.retrofit

import android.util.Log
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory

object RetrofitWeatherInstance {

    const val LOCAL_KEY = "Qahe3YsG5DEh1NrEibW9IUu4P%2FyYTgk4lBC6o0giu4nI1UjwSA3iTZXm4OcQ4Z%2FQ1ALRTLaKfZ6DsMEg%2BXsoqA%3D%3D"

    private fun httpLoggingInterceptor():Interceptor{
        return HttpLoggingInterceptor{message -> Log.e("MyOkHttpWeather : ", message + "")}
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    val client: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor())
            .build()
    }

    private val getInstance: Retrofit by lazy {
        val parser = TikXml.Builder().exceptionOnUnreadXml(false).build()

        Retrofit.Builder()
            .baseUrl("http://apis.data.go.kr/1400377/mtweather")
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(TikXmlConverterFactory.create(parser))
            .client(client)
            .build()
    }

    val retrofitService: RetrofitWeatherService by lazy {
        getInstance.create(RetrofitWeatherService::class.java)
    }

}
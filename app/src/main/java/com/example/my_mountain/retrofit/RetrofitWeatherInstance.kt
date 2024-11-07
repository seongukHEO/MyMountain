package com.example.my_mountain.retrofit

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.net.URLDecoder
import java.net.URLEncoder

//object RetrofitWeatherInstance {
//
//    val decodedKey = URLDecoder.decode("Qahe3YsG5DEh1NrEibW9IUu4P%2FyYTgk4lBC6o0giu4nI1UjwSA3iTZXm4OcQ4Z%2FQ1ALRTLaKfZ6DsMEg%2BXsoqA%3D%3D", "UTF-8")
//
//
//    private fun httpLoggingInterceptor():Interceptor{
//        return HttpLoggingInterceptor{message -> Log.e("MyOkHttpWeather : ", message + "")}
//            .setLevel(HttpLoggingInterceptor.Level.BODY)
//    }
//
//    val client: OkHttpClient by lazy {
//        OkHttpClient.Builder()
//            .addInterceptor(httpLoggingInterceptor())
//            .build()
//    }
//
//    private val getInstance: Retrofit by lazy {
//        val parser = TikXml.Builder().exceptionOnUnreadXml(true).build()
//
//        Retrofit.Builder()
//            .baseUrl("https://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/")
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .addConverterFactory(TikXmlConverterFactory.create(parser))
//            .client(client)
//            .build()
//    }
//
//    val retrofitService: RetrofitWeatherService by lazy {
//        getInstance.create(RetrofitWeatherService::class.java)
//    }
//
//}
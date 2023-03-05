package com.api.weatherapp.apicall

import com.api.weatherapp.apicall.Constant.API_KEY
import com.api.weatherapp.dataclass.Weather
import retrofit2.http.GET
import retrofit2.http.Headers

interface WeatherApi {
    @Headers("key: $API_KEY")
    @GET("current.json?q=Jeddah&aqi=no")
    suspend fun getWeather(): Weather
}
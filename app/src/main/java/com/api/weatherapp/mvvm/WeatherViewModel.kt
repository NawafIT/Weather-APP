package com.api.weatherapp.mvvm

import android.util.Log
import androidx.lifecycle.*
import com.api.weatherapp.dataclass.Weather
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class WeatherViewModel(private val weatherRep: WeatherRepository = WeatherRepository()) :
    ViewModel() {
    val weather = MutableLiveData<Weather>(null)

    init {

        viewModelScope.launch(Dispatchers.IO) {
            // use loop to make new request every 3.5 min\

            while (true) {
                try {
                    weather.postValue(weatherRep.getWeather())
                    delay(185000)

                } catch (e: Exception) {
                    Log.d("FFFFFFDDDD", e.message.toString())
                    delay(100000)
                }

            }
        }

    }
}

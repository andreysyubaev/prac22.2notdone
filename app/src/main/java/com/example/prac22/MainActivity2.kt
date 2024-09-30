package com.example.prac22

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import org.json.JSONObject

class MainActivity2 : AppCompatActivity() {

    private lateinit var cityEditText: EditText
    private lateinit var weatherInfo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        cityEditText = findViewById(R.id.cityEditText)
        weatherInfo = findViewById(R.id.weatherInfo)
    }

    fun accept(view: View){
        val city = cityEditText.text.toString()
        if (city.isNotEmpty()) {
            val key = "830dc11d9b3d32082179b89ef9a22cd7"
            val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&appid=$key&units=metric&lang=ru"
            val queue = Volley.newRequestQueue(this)
            val stringRequest = StringRequest(
                com.android.volley.Request.Method.GET,
                url,
                { response ->
                    try {
                        val obj = JSONObject(response)
                        val main = obj.getJSONObject("main")
                        val wind = obj.getJSONObject("wind")

                        val cityName = obj.getString("name")
                        val temperature = main.getString("temp")
                        val pressure = main.getString("pressure")
                        val windSpeed = wind.getString("speed")

                        weatherInfo.text = "City: $cityName\nTemperature: $temperature°C\nAir pressure: $pressure hPa\nWind Speed: $windSpeed m/s"
                    } catch (e: Exception) {
                        Log.d("MyLog", "JSON error: $e")
                    }
                },
                { error ->
                    Log.d("MyLog", "Volley error: $error")
                    Snackbar.make(findViewById(android.R.id.content), "Error fetching weather data", Snackbar.LENGTH_LONG)
                        .setActionTextColor(android.graphics.Color.RED)
                        .show()
                }
            )
            queue.add(stringRequest)
        } else {
            Snackbar.make(findViewById(android.R.id.content), "Please enter a city name", Snackbar.LENGTH_LONG)
                .setActionTextColor(android.graphics.Color.RED)
                .show()
        }
    }
}
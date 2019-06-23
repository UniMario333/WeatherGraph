package com.example.project.geoweather.json.mapping

import android.util.Log
import com.example.weathergraph.MainActivity
import java.io.IOException
import java.net.URL

object GeoAPIUrlUtil {

    fun getServerResponse(apiUrl: String): String {

        val response = StringBuffer()

        try {
            Log.i(MainActivity.APPLICATION_TAG, "Connessione verso: " + apiUrl)

            val risposta = URL(apiUrl).readText()

            response.append(risposta)

        } catch (e: IOException) {
            response.append("{\"cod\":\"404\",\"message\":\"city not found\"}")
        }

        Log.i(MainActivity.APPLICATION_TAG, "Risposta del server: " + response.toString())

        return response.toString()
    }
}

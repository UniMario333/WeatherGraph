package com.example.weathergraph

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.project.geoweather.GeoLocation
import com.example.project.geoweather.json.mapping.GeoWeatherUtil
import com.example.project.geoweather.json.mapping.openweather.pojo.OpenWeatherPOJO
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry

class Graph : AppCompatActivity() {

    companion object {
        lateinit var citta: String
        lateinit var nazione: String
        var latitudine: Double? = null
        var longitudine: Double? = null
        var contatoreOpenWeather: Int = 1
        val raggioKm = 20
        val maxComuni = 20
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_graph)

        citta = intent.getStringExtra("citta")
        nazione = intent.getStringExtra("nazione")
        latitudine = intent.getDoubleExtra("latitudine", Double.POSITIVE_INFINITY)
        longitudine = intent.getDoubleExtra("longitudine", Double.POSITIVE_INFINITY)

        Log.i(
            MainActivity.APPLICATION_TAG,
            "City found: "
                    + citta + ", "
                    + nazione + " @("
                    + latitudine.toString() + ", "
                    + longitudine.toString()  + ")")

        Toast.makeText(
            this,
            getString(R.string.loading_data_toast, citta, nazione),
            Toast.LENGTH_LONG).
            show()

        CitiesLoader(
                applicationContext,
                this,
                findViewById(R.id.temperatureAverageChart),
                findViewById(R.id.percentualeUmidita),
                findViewById(R.id.velocitaVento)
            ).
            execute(null)
    }

    private class CitiesLoader(
        private val context: Context,
        private val activity: Activity,
        private val graficoTemperature: BarChart,
        private val testoUmidita: TextView,
        private val testoVelocitaVento: TextView)
        : AsyncTask<String, Void, List<OpenWeatherPOJO>>() {

        private fun generaBarraGrafico(
            posizione: Float, dato: Float, label: String, colore: Int)
                : BarDataSet {
            val barra = BarDataSet(
                listOf(BarEntry(posizione, dato)),
                label)

            barra.color = colore

            return barra
        }

        override fun onPreExecute() {
            super.onPreExecute()

            contatoreOpenWeather += maxComuni

            if(contatoreOpenWeather >= 50) {

                Toast.makeText(
                    context,
                    activity.getString(R.string.openweather_limit_toast),
                    Toast.LENGTH_SHORT).
                    show()

                contatoreOpenWeather = 0

                this.cancel(true)
            }
        }

        override fun doInBackground(vararg params: String?): List<OpenWeatherPOJO> {

            val mutableResult = (try {
                GeoWeatherUtil.
                    getPlacesNear(GeoLocation(longitudine, latitudine), raggioKm).
                    geonames.
                    shuffled().
                    take(maxComuni).
                    map { comune ->
                        try { GeoWeatherUtil.getWeatherData(comune.name!!, comune.countryCode!!) }
                        catch (e: NoSuchElementException) { null } }.
                    filterNotNull()
            } catch (e: NoSuchElementException) {
                Log.e(MainActivity.APPLICATION_TAG, e.message)

                listOf<OpenWeatherPOJO>()
            }).toMutableList()

            mutableResult.add(GeoWeatherUtil.getWeatherData(citta, nazione));

            return mutableResult.toList()
        }

        override fun onPostExecute(result: List<OpenWeatherPOJO>) {
            super.onPostExecute(result)

            if(result.isEmpty()) {
                Log.i(MainActivity.APPLICATION_TAG, activity.getString(R.string.openweather_no_data_found))

                Toast.makeText(
                    context,
                    activity.getString(R.string.openweather_no_data_found_whereabouts),
                    Toast.LENGTH_SHORT).
                    show()
            } else {

                val dati = result.
                    map { city -> DatiGrafico(
                        city.main?.temp as Float,
                        city.main?.temp_max as Float,
                        city.main?.temp_min as Float,
                        city.main?.humidity?.toFloat() as Float,
                        city.wind?.speed as Float) }.
                    reduce { acc, datiNext ->
                        DatiGrafico(
                            acc.temperatura + datiNext.temperatura,
                            acc.temperaturaMassima + datiNext.temperaturaMassima,
                            acc.temperaturaMinima + datiNext.temperaturaMinima,
                            acc.umidita + datiNext.umidita,
                            acc.velocitaVento + datiNext.velocitaVento) }

                    dati.temperatura /= result.size
                    dati.temperaturaMassima /= result.size
                    dati.temperaturaMinima /= result.size
                    dati.umidita /= result.size
                    dati.velocitaVento /= result.size

                val descrizioneGrafico = Description()
                descrizioneGrafico.text =
                    activity.getString(R.string.average_values_descritption_graph, maxComuni, raggioKm)

                graficoTemperature.description = descrizioneGrafico
                graficoTemperature.data = BarData(
                    generaBarraGrafico(
                        1f, dati.temperaturaMassima,
                        activity.getString(R.string.max_temp_label), Color.RED),
                    generaBarraGrafico(
                        2f, dati.temperatura,
                        activity.getString(R.string.avg_temp_label), Color.YELLOW),
                    generaBarraGrafico(
                        3f, dati.temperaturaMinima,
                        activity.getString(R.string.min_temp_label), Color.CYAN))

                graficoTemperature.invalidate()

                testoUmidita.text = activity.getString(R.string.humidity_label, dati.umidita)
                testoVelocitaVento.text = activity.getString(R.string.wind_speed_label, dati.velocitaVento)
            }
        }
    }
}

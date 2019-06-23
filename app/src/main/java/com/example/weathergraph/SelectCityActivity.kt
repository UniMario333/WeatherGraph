package com.example.weathergraph

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import com.example.project.geoweather.json.mapping.GeoWeatherUtil
import com.example.project.geoweather.json.mapping.geonames.pojo.GeoNames
import com.example.project.geoweather.json.mapping.geonames.pojo.Place
import java.util.*

class SelectCityActivity : AppCompatActivity(), AdapterView.OnItemClickListener {

    private lateinit var listaCittaView: ListView
    private lateinit var cittaDaTrovare: String

    companion object {
        var listaCittaTrovate: GeoNames? = null
    }

    private class CitiesLoader(private val context: Context, private val activity: SelectCityActivity, private val listaCittaView: ListView)
        : AsyncTask<String, Void, GeoNames?>() {

        override fun doInBackground(vararg params: String?): GeoNames? {

            var risultati: GeoNames? = null

            try {
                risultati = GeoWeatherUtil.autocompleteCityName(params[0]!!)
            } catch (e: NoSuchElementException) {
                Log.e(MainActivity.APPLICATION_TAG, e.message)
            }

            return risultati
        }

        override fun onPostExecute(result: GeoNames?) {
            super.onPostExecute(result)

            if(result != null) {
                Log.i(
                    MainActivity.APPLICATION_TAG,
                    result.geonames.map { p -> p.name + ", " + p.countryName }.reduce { acc, s -> acc + '\n' + s })

                listaCittaTrovate = result
            } else {
                Log.i(MainActivity.APPLICATION_TAG, activity.getString(R.string.no_cities_found))
            }

            listaCittaView.adapter = GeoNamesListAdapter(
                context,
                if(listaCittaTrovate == null)
                    Collections.emptyList()
                else
                    listaCittaTrovate!!.geonames)

            listaCittaView.setOnItemClickListener(activity::onItemClick)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_city)

        this.listaCittaView = findViewById(R.id.listaCitta)

        cittaDaTrovare = intent.getStringExtra("cittaDaCercare")

        CitiesLoader(applicationContext, this, findViewById(R.id.listaCitta)).
            execute(cittaDaTrovare)
    }

    override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.i(MainActivity.APPLICATION_TAG, "Clicked on: " + (view as TextView).text)

        val i = Intent(applicationContext, Graph::class.java)

        val place = listaCittaView.getItemAtPosition(position) as Place

        i.putExtra("citta",  place.name)
        i.putExtra("nazione", place.countryCode)
        i.putExtra("latitudine", place.lat)
        i.putExtra("longitudine", place.lng)

        startActivity(i)
    }
}

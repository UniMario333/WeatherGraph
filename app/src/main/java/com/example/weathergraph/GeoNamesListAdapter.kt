package com.example.weathergraph

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.project.geoweather.json.mapping.geonames.pojo.Place

class GeoNamesListAdapter(
    private val context: Context, objects: List<Place>)
        : BaseAdapter() {

    private val data: List<Place> = objects

    override fun getItem(position: Int): Any {
        return data[position]
    }

    override fun getItemId(position: Int): Long {
        return data[position].hashCode().toLong()
    }

    override fun getCount(): Int {
        return data.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        val testoPosto = TextView(context)
        testoPosto.text = data[position].toponymName + ", " + data[position].countryName
        testoPosto.setTextColor(Color.BLACK)
        testoPosto.height = 80

        return testoPosto
    }
}
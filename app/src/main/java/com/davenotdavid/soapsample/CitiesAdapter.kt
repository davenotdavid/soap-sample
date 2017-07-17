package com.davenotdavid.soapsample

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Adapter subclass of [RecyclerView] that renders a list of data (cities).
 */
class CitiesAdapter(private val mCitiesData: List<String>):
        RecyclerView.Adapter<CitiesAdapter.CitiesViewHolder>() {

    /**
     * Inflates a layout for the list items.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CitiesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CitiesViewHolder(inflater.inflate(R.layout.list_item, parent, false))
    }

    /**
     * Binds each view from the ViewHolder onto the list item layout, and ultimately the
     * RecyclerView.
     */
    override fun onBindViewHolder(holder: CitiesViewHolder, position: Int) {
        holder.mCityNameTV.text = mCitiesData[position]
    }

    override fun getItemCount(): Int {
        return mCitiesData.size
    }

    /**
     * RecyclerView's ViewHolder class.
     */
    inner class CitiesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val mCityNameTV: TextView = itemView.findViewById<TextView>(R.id.cities_textview)
    }
}
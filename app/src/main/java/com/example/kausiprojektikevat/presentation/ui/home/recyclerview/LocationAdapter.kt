package com.example.kausiprojektikevat.presentation.ui.home.recyclerview

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kausiprojektikevat.R
import com.example.kausiprojektikevat.TAG
import com.example.kausiprojektikevat.data.network.model.location.Location
import com.example.kausiprojektikevat.domain.model.distance.Distance
import kotlinx.android.synthetic.main.list_item_locations.view.*
import java.util.*

class LocationAdapter(
    val onItemClicked: (Location, Int) -> Unit,
    val onLongClick: (Location) -> Unit,
    val onReleaseLongClick: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    private var locations: List<Location> = listOf()
    private var locationsAll: List<Location> = listOf()
    private var distances: List<Distance> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val viewHolder = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_locations,
            parent,
            false
        )
        return LocationViewHolder(viewHolder)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LocationViewHolder -> holder.bind(locations[position])
        }
    }

    override fun getItemCount(): Int = locations.size

    fun submitItems(locations: List<Location>) {
        this.locations = locations
        locationsAll = locations
    }

    fun submitDistances(distances: List<Distance>) {
        this.distances = (distances.toSet().toList())
        if (this.distances.size == locations.size) {
            notifyDataSetChanged()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    inner class LocationViewHolder(
        itemView: View,
    ) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.apply {
                setOnClickListener {
                    onItemClicked.invoke(locations[adapterPosition], adapterPosition)
                }
                setOnLongClickListener {
                    onLongClick(locations[adapterPosition])
                    it.setOnTouchListener { v, event ->
                        when (event.action) {
                            MotionEvent.ACTION_UP -> onReleaseLongClick.invoke()
                        }
                        false
                    }
                    true
                }
            }
        }

        fun bind(location: Location) {
            itemView.apply {
                Glide.with(this)
                    .load(location.imgRef)
                    .centerCrop()
                    .into(locationImageView)
                locationTextView.text = location.name.capitalize(Locale.ROOT)
                for (distance in distances) {
                    if (distance.address == location.address) {
                        distanceTextView.apply {
                            text = distance.distance
                            isVisible = true
                        }
                    }
                }
            }
        }
    }

    private val filteredItems = object : Filter() {

        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList: MutableList<Location> = mutableListOf()

            for (loc in locationsAll) {
                if (loc.name.toLowerCase(Locale.ROOT).contains(
                        constraint.toString().toLowerCase(
                            Locale.ROOT
                        )
                    )
                ) {
                    filteredList.add(loc)
                }
            }

            return FilterResults().apply {
                values = filteredList
            }
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            locations = (results?.values as List<Location>)
            notifyDataSetChanged()
        }

    }

    override fun getFilter(): Filter = filteredItems
}
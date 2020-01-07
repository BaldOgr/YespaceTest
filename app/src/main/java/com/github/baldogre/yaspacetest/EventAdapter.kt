package com.github.baldogre.yaspacetest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.baldogre.yaspacetest.model.Event
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class EventAdapter(val eventList: MutableList<Event> = mutableListOf()) :
    RecyclerView.Adapter<EventAdapter.EventHolder>(), Filterable {
    private var eventListFiltered: MutableList<Event>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventHolder(view)
    }

    override fun onBindViewHolder(holder: EventHolder, position: Int) {
        val event = eventListFiltered?.get(position) ?: eventList[position]
        holder.title.text = event.title
        holder.date.text = event.date
        holder.iAmGoing.visibility = if (event.iAmGoing) View.VISIBLE else View.GONE
        holder.numberOfParticipant.text = holder.numberOfParticipant.context.getString(
            R.string.number_of_participant,
            event.numberOfParticipant,
            event.maxParticipant
        )
        holder.hashTags.removeAllViews()
        for (hashTag in event.hashTags) {
            val chip = LayoutInflater.from(holder.date.context).inflate(
                R.layout.chip_hash_tag,
                null
            ) as Chip
            chip.text = "#$hashTag"
            holder.hashTags.addView(chip)
        }
    }

    override fun getItemCount(): Int {
        return eventListFiltered?.size ?: eventList.size
    }

    inner class EventHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image = itemView.findViewById<ImageView>(R.id.image)
        val title = itemView.findViewById<TextView>(R.id.title)
        val date = itemView.findViewById<TextView>(R.id.date)
        val hashTags = itemView.findViewById<ChipGroup>(R.id.chip_group)
        val iAmGoing = itemView.findViewById<ImageView>(R.id.i_am_going)
        val numberOfParticipant = itemView.findViewById<TextView>(R.id.number_of_participant)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val searchDate = constraint.toString()
                if (searchDate.isEmpty()) {
                    eventListFiltered = eventList
                } else {
                    val dates = searchDate.split(";")

                    val filteredList = mutableListOf<Event>()
                    for (date in dates) {
                        for (item in eventList) {
                            if (item.date == date && !filteredList.contains(item)) {
                                filteredList.add(item)
                            }
                        }
                    }
                    eventListFiltered = filteredList
                }

                val filterResult = FilterResults()
                filterResult.values = eventListFiltered
                return filterResult
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                eventListFiltered = results?.values as MutableList<Event>?
                notifyDataSetChanged()
            }
        }
    }
}
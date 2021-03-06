package org.wit.hillforts.activities

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.card_hillfort.view.*
import org.wit.hillfort.helpers.readImageFromPath
import org.wit.hillforts.models.HillfortModel
import org.wit.hillfortsurvey.R

interface HillfortListener {
    fun onHillfortClick(hillfort: HillfortModel)
    fun onHillfortLongClick(hillfort: HillfortModel)
    fun onMarkerClick(marker: Marker): Boolean
}

class HillfortAdapter constructor(private var hillforts: List<HillfortModel>,
                                  private val listener: HillfortListener) : RecyclerView.Adapter<HillfortAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): MainHolder {
        return MainHolder(LayoutInflater.from(parent?.context).inflate(R.layout.card_hillfort, parent, false))
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val hillfort = hillforts[holder.adapterPosition]
        holder.bind(hillfort, listener)
    }

    override fun getItemCount(): Int = hillforts.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(hillfort: HillfortModel, listener: HillfortListener) {
            itemView.HTownland.text = hillfort.townland
            itemView.HCounty.text = hillfort.county
            itemView.imageIcon.setImageBitmap(readImageFromPath(itemView.context, hillfort.image))
            itemView.HDate.text = hillfort.date
            itemView.rBar.rating = hillfort.rating
            itemView.setOnClickListener { listener.onHillfortClick(hillfort) }
            itemView.setOnLongClickListener { listener.onHillfortLongClick(hillfort); true }
        }
    }
}

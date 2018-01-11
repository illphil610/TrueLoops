package com.newwesterndev.trueloops.utils.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.newwesterndev.trueloops.R
import com.newwesterndev.trueloops.model.Model
import kotlinx.android.synthetic.main.track_list_row.view.*

class TrackListAdapter(private val context: Context, private val tracks: ArrayList<Model.Track?>):
        RecyclerView.Adapter<TrackListAdapter.ViewHolder>(){

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentTrack = tracks[position]

        holder.trackName.text = "Track " + position.toString()

        holder.itemLayout.setOnClickListener({_ ->

        })

        holder.deleteButton.setOnClickListener({_ ->

        })
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.track_list_row, parent, false)
        return ViewHolder(v)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val trackName = itemView.track_name
        val trackImage = itemView.track_image
        val deleteButton = itemView.delete_track_button
        val itemLayout = itemView.track_item_layout
    }

}
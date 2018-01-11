package com.newwesterndev.trueloops.utils.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.newwesterndev.trueloops.R
import com.newwesterndev.trueloops.model.SQLModel
import kotlinx.android.synthetic.main.song_list_row.view.*

class SongListAdapter(private val context: Context, private val songs: ArrayList<SQLModel.Song?>):
        RecyclerView.Adapter<SongListAdapter.ViewHolder>(){

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentSong = songs[position]

        holder.songName.text = currentSong?.name

        holder.itemLayout.setOnClickListener({_ ->

        })

        holder.deleteButton.setOnClickListener({_ ->

        })
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.song_list_row, parent, false)
        return ViewHolder(v)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val songName = itemView.song_name
        val songImage = itemView.song_image
        val deleteButton = itemView.delete_loop_button
        val itemLayout = itemView.song_item_layout
    }

}
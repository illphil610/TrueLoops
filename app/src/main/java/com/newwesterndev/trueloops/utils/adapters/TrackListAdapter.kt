package com.newwesterndev.trueloops.utils.adapters

import android.content.Context
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.newwesterndev.trueloops.R
import com.newwesterndev.trueloops.db.DbManager
import com.newwesterndev.trueloops.model.Model
import com.newwesterndev.trueloops.model.SQLModel
import kotlinx.android.synthetic.main.track_list_row.view.*

class TrackListAdapter(private val context: Context, private val tracks: ArrayList<Model.Track>, private val dbManager: DbManager):
        RecyclerView.Adapter<TrackListAdapter.ViewHolder>(){

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentTrack = tracks[position]
        holder.trackName.text = currentTrack.trackName

        holder.itemLayout.setOnClickListener({_ ->
        })

        holder.deleteButton.setOnClickListener({_ ->
            val popUpMenu = PopupMenu(context, holder.deleteButton)
            popUpMenu.menuInflater.inflate(R.menu.track_pop_up_menu, popUpMenu.menu)
            popUpMenu.setOnMenuItemClickListener({item: MenuItem? ->

                when(item?.itemId){
                    R.id.tpu_delete_item -> consume {
                        Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show()
                    }
                    R.id.tpu_rename_item -> consume {
                        Toast.makeText(context, "Rename", Toast.LENGTH_SHORT).show()
                    }
                    else -> consume {
                        Toast.makeText(context, "Else", Toast.LENGTH_SHORT).show()
                    }
                }
            })
            popUpMenu.show()
        })
    }

    inline fun consume(f: () -> Unit): Boolean {
        f()
        return true
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
        val deleteButton = itemView.delete_track_button
        val itemLayout = itemView.track_item_layout
    }
}
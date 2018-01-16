package com.newwesterndev.trueloops.utils.adapters

import android.content.Context
import android.support.v7.widget.PopupMenu
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.newwesterndev.trueloops.R
import com.newwesterndev.trueloops.db.DbManager
import com.newwesterndev.trueloops.model.Model
import kotlinx.android.synthetic.main.track_list_row.view.*



class TrackListAdapter(private val context: Context, private val tracks: ArrayList<Model.Track>, private val dbManager: DbManager):
        RecyclerView.Adapter<TrackListAdapter.ViewHolder>(){

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentTrack = tracks[position]
        holder.trackName.text = currentTrack.trackName
        currentTrackList = tracks

        when(currentTrack.armed){
            0 -> holder.armedBox.isChecked = false
            1 -> holder.armedBox.isChecked = true
            else -> holder.armedBox.isChecked = false
        }
        when(currentTrack.willplay){
            0 -> holder.willPlayBox.isChecked = false
            1 -> holder.willPlayBox.isChecked = true
            else -> holder.willPlayBox.isChecked = false
        }

        holder.armedBox.setOnClickListener {
            when(holder.armedBox.isChecked){
                true -> { currentTrack.armed = 1
                    currentTrackList[position] = currentTrack
                }
                false -> { currentTrack.armed = 0
                    currentTrackList[position] = currentTrack
                }
            }
        }

        holder.willPlayBox.setOnClickListener {
            when(holder.willPlayBox.isChecked){
                true -> { currentTrack.willplay = 1
                    currentTrackList[position] = currentTrack
                }
                false -> { currentTrack.willplay = 0
                    currentTrackList[position] = currentTrack
                }
            }
        }

        holder.renameCancelButton.setOnClickListener {
            holder.trackName.visibility = View.VISIBLE
            holder.trackGrid.visibility = View.VISIBLE
            holder.trackNameEdit.visibility = View.GONE
            holder.renameSaveButton.visibility = View.GONE
            holder.renameCancelButton.visibility = View.GONE

            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(holder.trackNameEdit.windowToken, 0)
        }

        holder.renameSaveButton.setOnClickListener {
            val newTrackName = holder.trackNameEdit.text.toString()
            if(newTrackName.length in 1..13){
                currentTrack.trackName = newTrackName
                currentTrackList[position] = currentTrack

                holder.trackName.visibility = View.VISIBLE
                holder.trackGrid.visibility = View.VISIBLE
                holder.trackNameEdit.visibility = View.GONE
                holder.renameSaveButton.visibility = View.GONE
                holder.renameCancelButton.visibility = View.GONE
                holder.trackName.text = newTrackName

                val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(holder.trackNameEdit.windowToken, 0)
            }else{
                Toast.makeText(context, "Bad name", Toast.LENGTH_SHORT).show()
            }
        }

        holder.menuButton.setOnClickListener({_ ->
            val popUpMenu = PopupMenu(context, holder.menuButton)
            popUpMenu.menuInflater.inflate(R.menu.track_pop_up_menu, popUpMenu.menu)
            popUpMenu.setOnMenuItemClickListener({item: MenuItem? ->

                when(item?.itemId){
                    R.id.tpu_delete_item -> consume {
                        Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show()
                    }
                    R.id.tpu_rename_item -> consume {
                        holder.trackName.visibility = View.GONE
                        holder.trackNameEdit.visibility = View.VISIBLE
                        holder.trackGrid.visibility = View.GONE
                        holder.renameSaveButton.visibility = View.VISIBLE
                        holder.renameCancelButton.visibility = View.VISIBLE
                        holder.trackNameEdit.requestFocus()
                        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
                    }
                    else -> consume {
                        Toast.makeText(context, "Else", Toast.LENGTH_SHORT).show()
                    }
                }
            })
            popUpMenu.show()
        })
    }

    inline private fun consume(f: () -> Unit): Boolean {
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
        val trackNameEdit = itemView.track_name_edit
        val menuButton = itemView.delete_track_button
        val armedBox = itemView.track_rec_check
        val willPlayBox = itemView.track_play_check
        val trackGrid = itemView.track_grid
        val renameSaveButton = itemView.track_done_renaming
        val renameCancelButton = itemView.track_cancel_renaming
    }

    companion object {
        var currentTrackList: ArrayList<Model.Track> = ArrayList()
    }
}
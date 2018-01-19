package com.newwesterndev.trueloops.utils.adapters

import android.content.Context
import android.content.Intent
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat.startActivity
import android.support.v4.util.Pair
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.newwesterndev.trueloops.MainActivity
import com.newwesterndev.trueloops.R
import com.newwesterndev.trueloops.RecordActivity
import com.newwesterndev.trueloops.db.DbManager
import com.newwesterndev.trueloops.model.Model
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.song_list_row.view.*

class SongListAdapter(private val context: Context, private val songs: ArrayList<Model.Song>, private val dbManager: DbManager):
        RecyclerView.Adapter<SongListAdapter.ViewHolder>(){

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentSong = songs[position]

        holder.songName.text = currentSong.name

        val dateString = context.getString(R.string.created) + " " + currentSong.dateCreated
        holder.dateText.text = dateString

        holder.itemLayout.setOnClickListener({_ ->
            val intent = Intent(context, RecordActivity::class.java)
            val i = songs[position].name.toString()
            intent.putExtra("song_name", i)

            // What is this options nonsense? haha
            startActivity(context, intent, null)
        })

        holder.deleteButton.setOnClickListener({_ ->
            dbManager.deleteSong(currentSong.name)
            val i = Intent(context, MainActivity::class.java)
            context.startActivity(i)
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
        val deleteButton = itemView.delete_loop_button
        val itemLayout = itemView.song_item_layout
        val dateText = itemView.song_date_text
    }
}
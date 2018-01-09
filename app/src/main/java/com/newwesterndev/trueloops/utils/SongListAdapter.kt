package com.newwesterndev.trueloops.utils

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.newwesterndev.trueloops.R
import com.newwesterndev.trueloops.R.color.colorPrimaryLight
import com.newwesterndev.trueloops.model.Model

/**
 * Created by Phil on 1/8/2018.
 */
class SongListAdapter(private var activity: Activity, private var songs: ArrayList<Model.Song>?) : BaseAdapter() {

    private class ViewHolder(row: View?) {
        var mSongName: TextView? = null
        var mSongImage: ImageView? = null

        init {
            mSongName = row?.findViewById(R.id.trackName)
            mSongImage = row?.findViewById(R.id.trackImage)
        }
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View?
        val viewHolder: ViewHolder

        if (convertView == null) {
            val inflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.track_list_row, null)
            viewHolder = ViewHolder(view)
            view?.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        var song = songs?.get(position)
        viewHolder.mSongName?.text = song?.songName
        //viewHolder.mSongName?.setTextColor(Color.parseColor("black"))
        //viewHolder.mSongName?.textSize = 30F

        return view as View
    }

    override fun getItem(position: Int): Model.Song? {
        return songs?.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return songs?.size!!
    }
}
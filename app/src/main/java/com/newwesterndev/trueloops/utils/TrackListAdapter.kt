package com.newwesterndev.trueloops.utils

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.newwesterndev.trueloops.R
import com.newwesterndev.trueloops.model.Model

/**
 * Created by Phil on 1/4/2018.
 */
class TrackListAdapter(private var activity: Activity, private var tracks: ArrayList<Model.Track>?) : BaseAdapter() {

    private class ViewHolder(row: View?) {
        var mTrackName: TextView? = null
        var mTrackImage: ImageView? = null

        init {
            mTrackName = row?.findViewById(R.id.trackName)
            mTrackImage = row?.findViewById(R.id.trackImage)
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

        var track = tracks?.get(position)
        viewHolder.mTrackName?.text = track?.trackName

        return view as View
    }

    override fun getItem(position: Int): Model.Track? {
        return tracks?.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return tracks?.size!!
    }
}
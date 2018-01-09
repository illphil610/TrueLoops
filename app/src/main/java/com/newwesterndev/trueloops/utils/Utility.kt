package com.newwesterndev.trueloops.utils

import android.os.Environment
import com.newwesterndev.trueloops.model.Model
import java.io.File

/**
 * Created by Phil on 1/6/2018.
 */
class Utility {

    fun createUniqueTrackFile(): File {
        val path = File (Environment.getExternalStorageDirectory().absolutePath)
        val mFile = File.createTempFile ("temporary", ".3gp", path)
        return mFile
    }

    fun saveCurrentTrackListAsSong(tracks: ArrayList<Model.Track>): Model.Song {
        return Model.Song("Song Name", tracks)
    }

    fun updateNewTrackToTrackList(newTrack : Model.Track, trackList : ArrayList<Model.Track>?) : ArrayList<Model.Track>? {
        trackList?.add(newTrack)
        return trackList
    }
}
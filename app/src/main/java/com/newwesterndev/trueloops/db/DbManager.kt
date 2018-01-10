package com.newwesterndev.trueloops.db

import android.content.Context
import com.newwesterndev.trueloops.model.Model
import org.jetbrains.anko.db.insert

class DbManager(private val c: Context){

    private val songDb = SongDbHelper.getInstance(c)
    private val trackDb = TrackDbHelper.getInstance(c)

    fun songToDb(song: Model.Song, tracks: ArrayList<Model.Track>){
        songDb.use {
            insert(SongSQLiteContract.TABLE_NAME,
                    SongSQLiteContract.COLUMN_NAME to song.name,
                    SongSQLiteContract.COLUMN_BARS to song.bars,
                    SongSQLiteContract.COLUMN_MEASURES to song.measures,
                    SongSQLiteContract.COLUMN_BPM to song.bpm,
                    SongSQLiteContract.COLUMN_TIME_SIG_ONE to song.timeSigOne,
                    SongSQLiteContract.COLUMN_TIME_SIG_TWO to song.timeSigTwo,
                    SongSQLiteContract.COLUMN_PLAY_DURING_REC to song.playDuringRecording)
        }

        trackDb.use {
            for(i in 0..tracks.size)
            insert(TrackSQLiteContract.TABLE_NAME,
                    TrackSQLiteContract.COLUMN_FROM_SONG_NAME to song.name,
                    TrackSQLiteContract.COLUMN_FILE_PATH to tracks[i].filePath)
        }
    }

}
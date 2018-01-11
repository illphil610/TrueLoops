package com.newwesterndev.trueloops.db

import android.content.Context
import android.util.Log
import com.newwesterndev.trueloops.model.Model
import com.newwesterndev.trueloops.model.SQLModel
import org.jetbrains.anko.db.*
import java.io.IOException

class DbManager(private val c: Context){

    private val songDb = SongDbHelper.getInstance(c)
    private val trackDb = TrackDbHelper.getInstance(c)

    fun songToDb(song: Model.Song, tracks: ArrayList<Model.Track>){
        songDb.use {
            createTable(SongSQLiteContract.TABLE_NAME, true,
                    SongSQLiteContract.COLUMN_ID to INTEGER + PRIMARY_KEY,
                    SongSQLiteContract.COLUMN_NAME to TEXT,
                    SongSQLiteContract.COLUMN_BARS to INTEGER,
                    SongSQLiteContract.COLUMN_MEASURES to INTEGER,
                    SongSQLiteContract.COLUMN_BPM to INTEGER,
                    SongSQLiteContract.COLUMN_TIME_SIG_ONE to INTEGER,
                    SongSQLiteContract.COLUMN_TIME_SIG_TWO to INTEGER,
                    SongSQLiteContract.COLUMN_PLAY_DURING_REC to TEXT)
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
            createTable(TrackSQLiteContract.TABLE_NAME, true,
                    TrackSQLiteContract.COLUMN_ID to INTEGER + PRIMARY_KEY,
                    TrackSQLiteContract.COLUMN_FROM_SONG_NAME to TEXT,
                    TrackSQLiteContract.COLUMN_FILE_PATH to TEXT)

            for(track: Model.Track in tracks) {
                insert(TrackSQLiteContract.TABLE_NAME,
                        TrackSQLiteContract.COLUMN_FROM_SONG_NAME to song.name,
                        TrackSQLiteContract.COLUMN_FILE_PATH to track.filePath)
            }
        }
    }

    fun getSongs(): ArrayList<SQLModel.Song?> {
        val rowParser = classParser<SQLModel.Song>()
        val songModel: ArrayList<SQLModel.Song?> = ArrayList()
        var stillGettingSongs = true
        var i = 1

        songDb.use {
            while (stillGettingSongs) {
                        val currentSong = select(SongSQLiteContract.TABLE_NAME)
                                .whereSimple("(_id = ?)", i.toString())
                                .parseOpt(rowParser)

                        if(currentSong == null){
                            Log.e("False ", "False")
                            stillGettingSongs = false
                        }else{
                            songModel.add(currentSong)
                            Log.e("Current Song ", currentSong.name)
                        }
                i++
            }
        }

        return songModel
    }

    fun getTracks(songName: String): ArrayList<SQLModel.Track?>{
        val rowParser = classParser<SQLModel.Track>()
        val trackModel: ArrayList<SQLModel.Track?> = ArrayList()
        var stillGettingTracks = false
        var i = 1

        trackDb.use {
            while(stillGettingTracks) {
                val currentTrack = select(TrackSQLiteContract.TABLE_NAME)
                        .whereSimple("(_id = ?)", i.toString())
                        .parseOpt(rowParser)
                if(currentTrack == null){
                    stillGettingTracks = false
                }else if(currentTrack.fromSongName == songName){
                    trackModel.add(currentTrack)
                }

                i++
            }
        }

        return trackModel
    }

}
package com.newwesterndev.trueloops.db

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.newwesterndev.trueloops.model.Model
import com.newwesterndev.trueloops.model.SQLModel
import org.jetbrains.anko.db.*

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
                    TrackSQLiteContract.COLUMN_TRACK_NAME to TEXT,
                    TrackSQLiteContract.COLUMN_FROM_SONG_NAME to TEXT,
                    TrackSQLiteContract.COLUMN_FILE_PATH to TEXT)

            for(track: Model.Track? in tracks) {
                insert(TrackSQLiteContract.TABLE_NAME,
                        TrackSQLiteContract.COLUMN_FROM_SONG_NAME to song.name,
                        TrackSQLiteContract.COLUMN_TRACK_NAME to track?.trackName,
                        TrackSQLiteContract.COLUMN_FILE_PATH to track?.filePath)
            }
        }
    }

    fun getSongs(): ArrayList<Model.Song> {
        val rowParser = classParser<SQLModel.Song>()
        var songList: List<SQLModel.Song> = ArrayList()
        val songModel: ArrayList<Model.Song> = ArrayList()

        songDb.use {
            songList = select(SongSQLiteContract.TABLE_NAME)
                    .parseList(rowParser)
        }

        for(i in 0 until(songList.size)){
            songModel.add(Model.Song(songList[i].name,
                    songList[i].bars,
                    songList[i].measures,
                    songList[i].bpm,
                    songList[i].timeSigOne,
                    songList[i].timeSigTwo,
                    songList[i].playDuringRecording))
        }

        return songModel
    }

    fun getTracks(songName: String): ArrayList<Model.Track> {
        val rowParser = classParser<SQLModel.Track>()
        var trackList: List<SQLModel.Track> = ArrayList()
        val trackModel: ArrayList<Model.Track> = ArrayList()

        trackDb.use {
            trackList = select(TrackSQLiteContract.TABLE_NAME)
                    .whereSimple("(fromsong = ?)", songName)
                    .parseList(rowParser)
        }

        for(i in 0 until(trackList.size)){
            val currentTrack = trackList[i]
            trackModel.add(Model.Track(currentTrack.trackName, currentTrack.fromSongName, currentTrack.filePath))
        }

        return trackModel
    }

    fun doesLoopNameExist(name: String): Boolean{
        val rowParser = classParser<SQLModel.Song>()
        var doesExist = false
        var songList: List<SQLModel.Song> = ArrayList()

        songDb.use {
            songList = select(SongSQLiteContract.TABLE_NAME)
                    .parseList(rowParser)
        }

        for(i in 0 until(songList.size)){
            if(songList[i].name == name){
                doesExist = true
            }
        }

        return doesExist
    }

    fun deleteSong(name: String){
        songDb.use {
            delete(SongSQLiteContract.TABLE_NAME,
                    "${SongSQLiteContract.COLUMN_NAME} = {name}",
                    "name" to name)
        }

        trackDb.use {
            delete(TrackSQLiteContract.TABLE_NAME,
                    "${TrackSQLiteContract.COLUMN_FROM_SONG_NAME} = {fromname}",
                    "fromname" to name)
        }
    }

    fun updateSong(song: Model.Song?, tracks: ArrayList<Model.Track>){
        trackDb.use {
            delete(TrackSQLiteContract.TABLE_NAME,
                    "${TrackSQLiteContract.COLUMN_FROM_SONG_NAME} = {fromname}",
                    "fromname" to song!!.name)
            for(track: Model.Track? in tracks) {
                insert(TrackSQLiteContract.TABLE_NAME,
                        TrackSQLiteContract.COLUMN_FROM_SONG_NAME to song.name,
                        TrackSQLiteContract.COLUMN_FILE_PATH to track?.filePath)
            }
        }
    }

    fun getSingleSongFromDB(songName: String?) : Model.Song {

        val rowParser = classParser<SQLModel.Song>()
        var song = Model.Song("",0,0,0,0,0,"")
        val name: String = songName.toString()
        songDb.use {
            val currentSong = select(SongSQLiteContract.TABLE_NAME)
                    .whereSimple("(${SongSQLiteContract.COLUMN_NAME} = ?)", name)
                    .parseOpt(rowParser)
            song = Model.Song(currentSong!!.name,
                    currentSong.bars,
                    currentSong.measures,
                    currentSong.bpm,
                    currentSong.timeSigOne,
                    currentSong.timeSigTwo,
                    currentSong.playDuringRecording)
        }
        return song
    }
}
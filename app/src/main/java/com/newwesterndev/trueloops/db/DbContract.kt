package com.newwesterndev.trueloops.db

object SongSQLiteContract{
    val TABLE_NAME = "Songs"
    val COLUMN_ID = "_id"
    val COLUMN_NAME = "name"
    val COLUMN_BARS = "bars"
    val COLUMN_MEASURES = "measures"
    val COLUMN_BPM = "bpm"
    val COLUMN_TIME_SIG_ONE = "timeone"
    val COLUMN_TIME_SIG_TWO = "timetwo"
    val COLUMN_PLAY_DURING_REC = "playduring"
}

object TrackSQLiteContract{
    val TABLE_NAME = "Tracks"
    val COLUMN_ID = "_id"
    val COLUMN_FROM_SONG_NAME = "fromsong"
    val COLUMN_FILE_PATH = "filepath"
}
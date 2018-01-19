package com.newwesterndev.trueloops.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class SongDbHelper(context: Context): ManagedSQLiteOpenHelper(context, "currentsongdb"){

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(SongSQLiteContract.TABLE_NAME, true,
                SongSQLiteContract.COLUMN_ID to INTEGER + PRIMARY_KEY,
                SongSQLiteContract.COLUMN_NAME to TEXT,
                SongSQLiteContract.COLUMN_DATE_CREATED to TEXT,
                SongSQLiteContract.COLUMN_BARS to INTEGER,
                SongSQLiteContract.COLUMN_MEASURES to INTEGER,
                SongSQLiteContract.COLUMN_BPM to INTEGER,
                SongSQLiteContract.COLUMN_TIME_SIG_ONE to INTEGER,
                SongSQLiteContract.COLUMN_TIME_SIG_TWO to INTEGER,
                SongSQLiteContract.COLUMN_PLAY_DURING_REC to INTEGER,
                SongSQLiteContract.COLUMN_PLAY_METRONOME to INTEGER,
                SongSQLiteContract.COLUMN_COUNT_IN_BARS to INTEGER)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.dropTable(SongSQLiteContract.TABLE_NAME, true)
    }

    companion object {
        private var instance: SongDbHelper? = null

        @Synchronized
        fun getInstance(context: Context): SongDbHelper{
            if(instance == null){
                instance = SongDbHelper(context.applicationContext)
            }
            return instance!!
        }
    }
}

val Context.songdatabase: SongDbHelper
    get() = SongDbHelper.getInstance(applicationContext)
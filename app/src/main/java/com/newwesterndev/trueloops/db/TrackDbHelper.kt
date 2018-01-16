package com.newwesterndev.trueloops.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import org.jetbrains.anko.db.*

class TrackDbHelper(context: Context): ManagedSQLiteOpenHelper(context, "currenttrackdb"){

    override fun onCreate(db: SQLiteDatabase) {
        db.createTable(TrackSQLiteContract.TABLE_NAME, true,
                TrackSQLiteContract.COLUMN_ID to INTEGER + PRIMARY_KEY,
                TrackSQLiteContract.COLUMN_TRACK_NAME to TEXT,
                TrackSQLiteContract.COLUMN_FROM_SONG_NAME to TEXT,
                TrackSQLiteContract.COLUMN_FILE_PATH to TEXT,
                TrackSQLiteContract.COLUMN_ARMED to INTEGER,
                TrackSQLiteContract.COLUMN_WILL_PLAY to INTEGER)
    }

    override fun onUpgrade(db: SQLiteDatabase, p1: Int, p2: Int) {
        db.dropTable(TrackSQLiteContract.TABLE_NAME, true)
    }

    companion object {
        private var instance: TrackDbHelper? = null

        @Synchronized
        fun getInstance(context: Context): TrackDbHelper{
            if(instance == null){
                instance = TrackDbHelper(context.applicationContext)
            }
            return instance!!
        }
    }
}

val Context.trackdatabase: TrackDbHelper
    get() = TrackDbHelper.getInstance(applicationContext)
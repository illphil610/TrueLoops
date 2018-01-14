package com.newwesterndev.trueloops

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v4.util.Pair
import android.support.v7.widget.LinearLayoutManager
import android.widget.LinearLayout
import com.newwesterndev.trueloops.db.DbManager
import com.newwesterndev.trueloops.model.SQLModel

class MainActivity : AppCompatActivity() {

    private var mSongArrayList: ArrayList<SQLModel.Song> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECORD_AUDIO,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 10)

        val mDbManager = DbManager(applicationContext)
        mSongArrayList = mDbManager.getSongs()
        songList.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        songList.adapter = com.newwesterndev.trueloops.utils.adapters.SongListAdapter(this, mSongArrayList, mDbManager)

        main_record_button.setOnClickListener{_ ->
            startRecordActivity()
        }
    }

    private fun startRecordActivity(){
        val i = Intent(this, RecordActivity::class.java)
        val pairOne: Pair<View, String> = Pair(main_record_button, "record_button")
        val pairTwo: Pair<View, String> = Pair(main_bottom_view, "bottom_view")
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pairOne, pairTwo)
        startActivity(i, options.toBundle())
    }
}

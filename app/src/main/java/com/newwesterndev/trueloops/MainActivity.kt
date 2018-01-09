package com.newwesterndev.trueloops

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v4.util.Pair
import android.util.Log
import android.view.Display
import android.widget.ListView
import com.newwesterndev.trueloops.model.Model
import com.newwesterndev.trueloops.utils.SongListAdapter
import kotlinx.android.synthetic.main.activity_record.*

class MainActivity : AppCompatActivity() {

    private var mSongListView: ListView? = null
    private var mSongListAdapter: SongListAdapter? = null
    private var mSongArrayList: ArrayList<Model.Song>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECORD_AUDIO,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 10)

        mSongArrayList = ArrayList()
        mSongListView = findViewById(R.id.songList)
        mSongListAdapter = SongListAdapter(this, mSongArrayList)
        songList.adapter = mSongListAdapter

        val song = intent.extras?.get("Song")
        Log.v("Song", song.toString())

        when {
            song != null -> {
                mSongArrayList?.add(song as Model.Song)
                mSongListAdapter?.notifyDataSetChanged()
            }
        }

        mSongArrayList?.add(Model.Song("test", ArrayList()))
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

package com.newwesterndev.trueloops

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*
import android.support.v4.util.Pair

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_record_button.setOnClickListener{_ ->
            startRecordActivity()
        }
    }

    fun startRecordActivity(){
        val i = Intent(this, RecordActivity::class.java)

        val pairOne: Pair<View, String> = Pair(main_record_button, "record_button")
        val pairTwo: Pair<View, String> = Pair(main_bottom_view, "bottom_view")

        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                pairOne, pairTwo)

        startActivity(i, options.toBundle())
    }
}

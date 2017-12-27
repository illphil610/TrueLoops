package com.newwesterndev.trueloops

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.util.Log
import com.tyorikan.voicerecordingvisualizer.RecordingSampler
import kotlinx.android.synthetic.main.activity_record.*

class RecordActivity : RecordingSampler.CalculateVolumeListener, AppCompatActivity() {

    private val mRecordingSampler = RecordingSampler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)
        ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.RECORD_AUDIO), 10)

        mRecordingSampler.setVolumeListener(this)
        mRecordingSampler.setSamplingInterval(105)
        mRecordingSampler.link(visualizer)
        mRecordingSampler.startRecording()
    }

    override fun onResume() {
        super.onResume()
        mRecordingSampler.startRecording()
    }

    override fun onStop() {
        mRecordingSampler.stopRecording()
        super.onStop()
    }

    override fun onDestroy() {
        mRecordingSampler.release()
        super.onDestroy()
    }

    override fun onCalculateVolume(volume: Int) {
        Log.d("currentVolume",volume.toString())
    }
}

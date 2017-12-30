package com.newwesterndev.trueloops

import android.media.MediaPlayer
import android.media.MediaRecorder
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import com.tyorikan.voicerecordingvisualizer.RecordingSampler
import kotlinx.android.synthetic.main.activity_record.*
import java.io.File
import java.io.IOException

class RecordActivity : RecordingSampler.CalculateVolumeListener, AppCompatActivity() {

    private var mRecorder: MediaRecorder? = null
    private var mPlayer: MediaPlayer? = null
    private var mRecordingSampler: RecordingSampler? = null
    private lateinit var mFile: File
    private var mIsRecording = false
    private var mIsPlaying = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)
        startRecordingSampler()

        val path = File (Environment.getExternalStorageDirectory().absolutePath)
        mFile = File.createTempFile ("temporary", ".3gp", path)

        detail_record_button.setOnClickListener({_ ->
            if(!mIsRecording){
                stopRecordingSampler()
                startRecording()
            }else{
                stopRecording()
                startRecordingSampler()
            }
        })

        detail_play_button.setOnClickListener({_ ->
            if(!mIsPlaying && !mIsRecording){
                stopRecordingSampler()
                startPlaying()
            }else{
                stopPlaying()
                startRecordingSampler()
            }
        })
    }

    @Throws(IOException::class)
    private fun startRecordingSampler(){
        mRecordingSampler = RecordingSampler()
        mRecordingSampler?.setVolumeListener(this)
        mRecordingSampler?.setSamplingInterval(105)
        mRecordingSampler?.link(visualizer)
        mRecordingSampler?.startRecording()
    }

    private fun stopRecordingSampler(){
        mRecordingSampler?.stopRecording()
        mRecordingSampler?.release()
    }

    @Throws(IOException::class, IllegalStateException::class)
    private fun startRecording(){
        detail_record_button.setImageResource(R.drawable.ic_stop_white_24dp)
        mIsRecording = true

        mRecorder = MediaRecorder()
        mRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mRecorder?.setOutputFile(mFile.absolutePath)
        mRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

        mRecorder?.prepare()
        mRecorder?.start()
    }

    private fun stopRecording(){
        detail_record_button.setImageResource(R.drawable.ic_fiber_manual_record_white_24dp)
        mIsRecording = false

        mRecorder?.stop()
        mRecorder?.release()
    }

    @Throws(IOException::class)
    private fun startPlaying(){
        detail_play_button.setImageResource(R.drawable.ic_stop_white_24dp)
        mIsPlaying = true

        mPlayer = MediaPlayer()
        mPlayer?.setDataSource(mFile.absolutePath)
        mPlayer?.prepare()
        mPlayer?.start()

        mPlayer?.setOnCompletionListener { _ ->
            detail_play_button.setImageResource(R.drawable.ic_play_arrow_white_24dp)
            mIsPlaying = false
            startRecordingSampler()
        }
    }

    private fun stopPlaying(){
        mPlayer?.release()
        mPlayer = null
    }

    override fun onStop() {
        stopRecordingSampler()

        if(mIsRecording){
            mRecorder?.stop()
            mRecorder?.release()
        }

        if(mIsPlaying){
            mPlayer?.stop()
            mPlayer?.release()
        }

        super.onStop()
    }

    override fun onCalculateVolume(volume: Int) {
        Log.d("currentVolume ",volume.toString())
    }
}

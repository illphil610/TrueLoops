package com.newwesterndev.trueloops

import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.ListView
import com.newwesterndev.trueloops.db.DbManager
import com.newwesterndev.trueloops.model.Model
import com.newwesterndev.trueloops.model.Model.Track
import com.newwesterndev.trueloops.modules.PlaybackModule
import com.newwesterndev.trueloops.utils.DaggerPlaybackComponent
import com.newwesterndev.trueloops.utils.TrackListAdapter
import com.newwesterndev.trueloops.utils.Utility
import com.tyorikan.voicerecordingvisualizer.RecordingSampler
import kotlinx.android.synthetic.main.activity_record.*
import java.io.File
import java.io.IOException

class RecordActivity : AppCompatActivity() { //RecordingSampler.CalculateVolumeListener

    private var mRecorder: MediaRecorder? = null
    private var mPlayer: MediaPlayer? = null
    private var mRecordingSampler: RecordingSampler? = null
    private var mPlayback: Model.PlaybackRecording
    private var mFile: File? = null
    private var mIsRecording = false
    private var mIsPlaying = false
    private var mSongTrackListView: ListView? = null
    private var mTrackArrayList: ArrayList<Track>? = null
    private var mTrackListAdapter: TrackListAdapter? = null
    private var mUtility: Utility? = null

    init {
        mUtility = Utility()
        mTrackArrayList = ArrayList()
        val daggerPRComponent = DaggerPlaybackComponent.builder()
                .playbackModule(PlaybackModule())
                .build()
        mPlayback = daggerPRComponent.getDefaultPlaybackSettings()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)
        //startRecordingSampler()

        slideUp(detail_settings_button)
        slideUp(detail_play_button)
        mSongTrackListView = findViewById(R.id.detail_track_list)
        mTrackListAdapter = TrackListAdapter(this, mTrackArrayList)
        mSongTrackListView?.adapter = mTrackListAdapter
        mTrackListAdapter?.notifyDataSetChanged()

        detail_record_button.setOnClickListener({_ ->
            if (!mIsRecording){
                //stopRecordingSampler()
                startRecording()
            } else {
                stopRecording()
                //startRecordingSampler()
            }
        })

        detail_play_button.setOnClickListener({_ ->
            if (!mIsPlaying && !mIsRecording) {
                //stopRecordingSampler()
                startPlaying()
            } else {
                stopPlaying()
                //startRecordingSampler()
            }
        })

        detail_settings_button.setOnClickListener({_ ->
           PlaybackSettingsDialog(this, mPlayback).show()
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.record_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.save_song_button -> {

            val dbManager = DbManager(this)
            dbManager.songToDb(Model.Song("New Song",
                    mPlayback.bars, mPlayback.measures,
                    mPlayback.metronome.bpm, mPlayback.metronome.timeSigOne, mPlayback.metronome.timeSigTwo,
                    mPlayback.metronome.playDuringRecording.toString()), mTrackArrayList!!)

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }


    /*
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
    */

    @Throws(IOException::class, IllegalStateException::class)
    private fun startRecording(){
        detail_record_button.setImageResource(R.drawable.ic_stop_white_24dp)
        mIsRecording = true
        mFile = mUtility?.createUniqueTrackFile()
        Log.v("testing unique file", mFile.toString())

        // Abstract this as a function in Utility as well
        mRecorder = MediaRecorder()
        mRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)
        mRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mRecorder?.setOutputFile(mFile?.absolutePath)
        mRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
        mRecorder?.prepare()
        mRecorder?.start()
    }

    private fun stopRecording(){
        detail_record_button.setImageResource(R.drawable.ic_fiber_manual_record_white_24dp)
        mIsRecording = false
        mRecorder?.stop()
        mRecorder?.reset()
        mRecorder?.release()

        val currentTrack = Track("New Track", mFile?.absolutePath.toString())
        mTrackArrayList = mUtility?.updateNewTrackToTrackList(currentTrack, mTrackArrayList)
        mTrackListAdapter?.notifyDataSetChanged()
        // Pop up dialog to get track name
    }

    @Throws(IOException::class)
    private fun startPlaying(){
        detail_play_button.setImageResource(R.drawable.ic_stop_white_24dp)
        mIsPlaying = true
        mPlayer = MediaPlayer()
        mPlayer?.setDataSource(mFile?.absolutePath.toString())
        mPlayer?.prepare()
        mPlayer?.start()

        mPlayer?.setOnCompletionListener { _ ->
            detail_play_button.setImageResource(R.drawable.ic_play_arrow_white_24dp)
            mIsPlaying = false
            //startRecordingSampler()
        }
    }

    private fun stopPlaying(){
        detail_play_button.setImageResource(R.drawable.ic_play_arrow_white_24dp)
        mIsPlaying = false
        mPlayer?.release()
        mPlayer = null
    }

    override fun onStop() {
        //stopRecordingSampler()
        if (mIsRecording) {
            mRecorder?.stop()
            mRecorder?.release()
        }

        if (mIsPlaying) {
            mPlayer?.stop()
            mPlayer?.release()
        }
        super.onStop()
    }

    /*
    override fun onCalculateVolume(volume: Int) {
        Log.d("currentVolume ",volume.toString())
    }
    */

    private fun slideUp(v: View) {
        val metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)

        v.translationY = metrics.heightPixels.toFloat()
        try {
            v.animate().setInterpolator(AccelerateInterpolator())
                    .setDuration(600)
                    .translationYBy((-metrics.heightPixels).toFloat())
                    .start()
        } catch (ex: Exception) {

        }

    }
}

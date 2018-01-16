package com.newwesterndev.trueloops

import android.app.AlertDialog
import android.app.DialogFragment
import android.content.DialogInterface
import android.content.Intent
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.LinearLayout
import android.widget.Toast
import com.newwesterndev.trueloops.db.DbManager
import com.newwesterndev.trueloops.model.Model
import com.newwesterndev.trueloops.model.SQLModel
import com.newwesterndev.trueloops.modules.PlaybackModule
import com.newwesterndev.trueloops.utils.DaggerPlaybackComponent
import com.newwesterndev.trueloops.utils.LoopNameDialog
import com.newwesterndev.trueloops.utils.Utility
import com.newwesterndev.trueloops.utils.adapters.TrackListAdapter
import kotlinx.android.synthetic.main.activity_record.*
import java.io.File
import java.io.IOException

class RecordActivity : AppCompatActivity(), LoopNameDialog.LoopNameDialogListener{ //RecordingSampler.CalculateVolumeListener

    private var mRecorder: MediaRecorder? = null
    private var mPlayer: MediaPlayer? = null
    private var mPlayback: Model.PlaybackRecording
    private var mFile: File? = null
    private var mTrackArrayList: ArrayList<Model.Track> = ArrayList()
    private var mUtility: Utility = Utility()
    private var mLoopNameDialog = LoopNameDialog()
    private var mIsRecording = false
    private var mIsPlaying = false
    private var songName: String? = null
    private var selectedSong: Model.Song? = null

    init {
        val daggerPRComponent = DaggerPlaybackComponent.builder()
                .playbackModule(PlaybackModule())
                .build()
        mPlayback = daggerPRComponent.getDefaultPlaybackSettings()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        val mDBManger = DbManager(applicationContext)
        songName = intent?.getStringExtra("song_name")
        selectedSong = mDBManger.getSingleSongFromDB(songName)

        if (selectedSong != null)
            mTrackArrayList = mDBManger.getTracks(selectedSong!!.name)

        detail_track_list.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        detail_track_list.adapter = com.newwesterndev.trueloops.utils.adapters.TrackListAdapter(this, mTrackArrayList, mDBManger)
        slideUp(detail_settings_button)
        slideUp(detail_play_button)

        detail_record_button.setOnClickListener({_ ->
            if (!mIsRecording){
                startRecording()
            } else {
                stopRecording()
            }
        })

        detail_play_button.setOnClickListener({_ ->
            if (!mIsPlaying && !mIsRecording) {
                startPlaying()
            } else {
                stopPlaying()
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
            mLoopNameDialog.show(fragmentManager, "LoopNameDialog")
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    @Throws(IOException::class, IllegalStateException::class)
    private fun startRecording(){
        detail_record_button.setImageResource(R.drawable.ic_stop_white_24dp)
        mIsRecording = true
        mFile = mUtility.createUniqueTrackFile()
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

        // JOEEEEEE!!!!
        // I dont think this is the best way to create the tracks... I see the tracks are created nicely in the DbManager
        // with the specified song details etc.  I feel like creating this is like a space holder until the actual data is
        // written which will overwrite all of this nonesense.
        val testTrack = Model.Track("New Track", selectedSong?.name.toString(), mFile?.absolutePath.toString())
        mTrackArrayList.add(testTrack)
        //mUtility.showToast(this, mTrackArrayList.toString())
        detail_track_list.adapter.notifyDataSetChanged()
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
        }
    }

    private fun stopPlaying(){
        detail_play_button.setImageResource(R.drawable.ic_play_arrow_white_24dp)
        mIsPlaying = false
        mPlayer?.release()
        mPlayer = null
    }

    override fun onStop() {

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

    override fun onDialogPositiveClick(dialogFragment: DialogFragment, loopName: String) {
        val dbManager = DbManager(this)

        if(loopName.isNotEmpty() && !dbManager.doesLoopNameExist(loopName)) {
            dbManager.songToDb(Model.Song(loopName,
                    mPlayback.bars, mPlayback.measures,
                    mPlayback.metronome.bpm, mPlayback.metronome.timeSigOne, mPlayback.metronome.timeSigTwo,
                    mPlayback.metronome.playDuringRecording.toString()), mTrackArrayList)
            val intent = Intent(this, MainActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }else{
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Do you want to overwrite the song named " + loopName + "?")
                    .setTitle("Song name already exists!")
                    .setPositiveButton(R.string.save, DialogInterface.OnClickListener({dialogInterface, i ->
                        dbManager.updateSong(selectedSong, mTrackArrayList)
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                    }))
                    .setNegativeButton(R.string.cancel, DialogInterface.OnClickListener({dialogInterface, i ->

                    }))

            builder.show()
        }
    }

    override fun onDialogNegativeClick(dialogFragment: DialogFragment) {
    }

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
            ex.printStackTrace()
        }
    }
}

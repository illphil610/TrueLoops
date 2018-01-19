package com.newwesterndev.trueloops

import android.app.AlertDialog
import android.app.DialogFragment
import android.content.*
import android.support.v4.app.FragmentTransaction
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.LinearLayoutManager
import android.util.DisplayMetrics
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.LinearLayout
import android.widget.Toast
import com.google.gson.Gson
import com.newwesterndev.trueloops.db.DbManager
import com.newwesterndev.trueloops.model.Model
import com.newwesterndev.trueloops.utils.DaggerNewSongComponent
import com.newwesterndev.trueloops.utils.dialogs.LoopNameDialog
import com.newwesterndev.trueloops.utils.Utility
import com.newwesterndev.trueloops.utils.adapters.TrackListAdapter
import kotlinx.android.synthetic.main.activity_record.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

const val BROADCAST_TRACKS_UPDATED = "updatetracks"

class RecordActivity : AppCompatActivity(), LoopNameDialog.LoopNameDialogListener,
        MasterTrackFragment.OnFragmentInteractionListener{

    private var mRecorder: MediaRecorder? = null
    private var mPlayer: MediaPlayer? = null
    private var mFile: File? = null
    private var mTrackArrayList: ArrayList<Model.Track> = ArrayList()
    private var mUtility: Utility = Utility()
    private var mLoopNameDialog = LoopNameDialog()
    private var mIsRecording = false
    private var mIsPlaying = false
    private var mIsFragShowing = false
    private var songName: String? = null
    private var mCurrentSong: Model.Song? = null
    private lateinit var mMasterTrackFrag: MasterTrackFragment

    init {
        val daggerNSComponent = DaggerNewSongComponent.builder()
                .build()
        mCurrentSong = daggerNSComponent.getNewSong()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record)

        val mDBManger = DbManager(applicationContext)
        songName = intent?.getStringExtra("song_name")


        if(songName != null)
            mCurrentSong = mDBManger.getSingleSongFromDB(songName)
        if (mCurrentSong != null)
            mTrackArrayList = mDBManger.getTracks(mCurrentSong!!.name)

        detail_track_list.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        detail_track_list.adapter = com.newwesterndev.trueloops.utils.adapters.TrackListAdapter(this, mTrackArrayList, mDBManger)
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

        m_menu_track_button.setOnClickListener{

            if(!mIsFragShowing) {
                mMasterTrackFrag = MasterTrackFragment.newInstance(Gson().toJson(mCurrentSong))

                supportFragmentManager.inTransaction {
                    setCustomAnimations(R.anim.slide_in_from_top,
                            R.anim.slide_out_from_bottom,
                            R.anim.slide_out_from_bottom,
                            R.anim.slide_out_from_bottom)
                    add(R.id.master_track_frag_view,
                            mMasterTrackFrag)
                }
                mIsFragShowing = true
            }else{
                supportFragmentManager.inTransaction {
                    remove(mMasterTrackFrag)
                }
                mIsFragShowing = false
            }
        }
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
        val testTrack = Model.Track("New Track", mCurrentSong?.name.toString(), mFile?.absolutePath.toString(), 0, 0)
        mTrackArrayList.add(testTrack)
        detail_track_list.adapter.notifyDataSetChanged()
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

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(trackListReceiver, IntentFilter(BROADCAST_TRACKS_UPDATED))
    }

    override fun onStop() {

        LocalBroadcastManager.getInstance(this)
                .unregisterReceiver(trackListReceiver)

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

    private val trackListReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {

            mTrackArrayList =
                    Utility.deleteTrackFromTrackList(intent?.getStringExtra("file"), mTrackArrayList)
            detail_track_list.adapter.notifyDataSetChanged()
        }
    }

    override fun onDialogPositiveClick(dialogFragment: DialogFragment, loopName: String) {
        val dbManager = DbManager(this)
        val timeStamp = SimpleDateFormat("MM/dd/yyyy").format(Date())

        if(loopName.isNotEmpty() && !dbManager.doesLoopNameExist(loopName)) {
            dbManager.songToDb(Model.Song(loopName,
                    timeStamp.toString(),
                    mCurrentSong!!.bars,
                    mCurrentSong!!.measures,
                    mCurrentSong!!.bpm,
                    mCurrentSong!!.timeSigOne,
                    mCurrentSong!!.timeSigTwo,
                    mCurrentSong!!.playDuringRecording,
                    mCurrentSong!!.playMetronome,
                    mCurrentSong!!.countInBars),
                    TrackListAdapter.currentTrackList)

            val intent = Intent(this, MainActivity::class.java)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }else{
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Do you want to overwrite the song named " + loopName + "?")
                    .setTitle("Song name already exists!")
                    .setPositiveButton(R.string.save, DialogInterface.OnClickListener({dialogInterface, i ->
                        dbManager.updateSong(mCurrentSong, TrackListAdapter.currentTrackList)
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.record_menu, menu)
        return true
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

    private inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> Unit) {
        val fragmentTransaction = beginTransaction()
        fragmentTransaction.func()
        fragmentTransaction.commit()
    }

    override fun onMTFragmentInteraction(songJson: String) {
        mCurrentSong = Gson().fromJson(songJson, Model.Song::class.java)
        supportFragmentManager.inTransaction {
            remove(mMasterTrackFrag)
        }
        mIsFragShowing = false
    }
}

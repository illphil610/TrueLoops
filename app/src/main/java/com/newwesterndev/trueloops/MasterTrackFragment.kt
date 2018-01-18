package com.newwesterndev.trueloops

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.newwesterndev.trueloops.model.Model
import kotlinx.android.synthetic.main.fragment_master_track.*

private const val ARG_PARAM1 = "param1"

class MasterTrackFragment : Fragment() {
    private lateinit var mSong: Model.Song
    private var listener: OnFragmentInteractionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mSong = Gson().fromJson(it.getString(ARG_PARAM1), Model.Song::class.java)
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mt_bars_edit.setText(mSong.bars.toString())
        mt_measures_edit.setText(mSong.measures.toString())
        mt_bpm_edit.setText(mSong.bpm.toString())
        mt_time_sig_one_edit.setText(mSong.timeSigOne.toString())
        mt_time_sig_two_edit.setText(mSong.timeSigTwo.toString())
        mt_count_in_edit.setText(mSong.countInBars.toString())
        if(mSong.playDuringRecording == 1) mt_play_audio_check.isChecked = true
        if(mSong.playMetronome == 1) mt_play_metro_check.isChecked = true

        mt_save_button.setOnClickListener{

            var playAudio = 0
            var playMetronome = 0

            if(mt_play_audio_check.isChecked) playAudio = 1
            if(mt_play_metro_check.isChecked) playMetronome = 1

            val songToReturn = Model.Song(mSong.name,
                    mt_bars_edit.text.toString().toInt(),
                    mt_measures_edit.text.toString().toInt(),
                    mt_bpm_edit.text.toString().toInt(),
                    mt_time_sig_one_edit.text.toString().toInt(),
                    mt_time_sig_two_edit.text.toString().toInt(),
                    mt_count_in_edit.text.toString().toInt(),
                    playAudio, playMetronome)
            listener?.onMTFragmentInteraction(Gson().toJson(songToReturn))
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_master_track, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onMTFragmentInteraction(songJson: String)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
                MasterTrackFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                    }
                }
    }
}

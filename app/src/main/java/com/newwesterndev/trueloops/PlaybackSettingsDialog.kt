package com.newwesterndev.trueloops

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.newwesterndev.trueloops.model.PlaybackSetup

class PlaybackSettingsDialog(context: Context, playbackRecording: PlaybackSetup.PlaybackRecording): Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_playback_settings)
    }

}
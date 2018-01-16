package com.newwesterndev.trueloops.utils.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import com.newwesterndev.trueloops.R
import com.newwesterndev.trueloops.model.Model

class PlaybackSettingsDialog(context: Context, playbackRecording: Model.PlaybackRecording): Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_playback_settings)
    }

}
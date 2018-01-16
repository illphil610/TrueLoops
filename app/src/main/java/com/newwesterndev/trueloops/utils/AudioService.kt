package com.newwesterndev.trueloops.utils

import android.app.Service
import android.content.Intent
import android.media.MediaRecorder
import android.os.IBinder

class AudioService : Service() {

    private var mRecorder: MediaRecorder? = null

    override fun onBind(intent: Intent?): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
    }
}
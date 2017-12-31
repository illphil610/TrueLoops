package com.newwesterndev.trueloops.utils

import com.newwesterndev.trueloops.model.Model
import com.newwesterndev.trueloops.modules.PlaybackModule
import dagger.Component

@Component(modules = arrayOf(PlaybackModule::class))
interface PlaybackComponent{
    fun getDefaultPlaybackSettings(): Model.PlaybackRecording
}
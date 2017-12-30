package com.newwesterndev.trueloops.modules

import com.newwesterndev.trueloops.model.PlaybackSetup
import dagger.Module
import dagger.Provides

@Module
class PlaybackModule{

    @Provides
    fun playbackRecording(metronome: PlaybackSetup.Metronome): PlaybackSetup.PlaybackRecording{
        return PlaybackSetup.PlaybackRecording(4,
                1,
                metronome)
    }

    @Provides
    fun metronome(): PlaybackSetup.Metronome{
        return PlaybackSetup.Metronome(120,
                4,
                4,
                true)
    }

}
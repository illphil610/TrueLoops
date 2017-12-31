package com.newwesterndev.trueloops.modules

import com.newwesterndev.trueloops.model.Model
import dagger.Module
import dagger.Provides

@Module
class PlaybackModule{

    @Provides
    fun playbackRecording(metronome: Model.Metronome): Model.PlaybackRecording{
        return Model.PlaybackRecording(4,
                1,
                metronome)
    }

    @Provides
    fun metronome(): Model.Metronome{
        return Model.Metronome(120,
                4,
                4,
                true)
    }

}
package com.newwesterndev.trueloops.model

object PlaybackSetup {

    data class PlaybackRecording(var bars: Int,
                                 var measures: Int,
                                 var metronome: Metronome)

    data class Metronome(var bpm: Int,
                         var timeSigOne: Int,
                         var timeSigTwo: Int,
                         var playDuringRecording: Boolean)

}
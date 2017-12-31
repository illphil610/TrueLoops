package com.newwesterndev.trueloops.model

object Model {

    data class PlaybackRecording(var bars: Int,
                                 var measures: Int,
                                 var metronome: Metronome)

    //If time signature is 4/4 I was thinking we can do timeSigOne/timeSigTwo

    data class Metronome(var bpm: Int,
                         var timeSigOne: Int,
                         var timeSigTwo: Int,
                         var playDuringRecording: Boolean)

}
package com.newwesterndev.trueloops.model

import java.io.File

object Model {

    data class PlaybackRecording(var bars: Int,
                                 var measures: Int,
                                 var metronome: Metronome)

    //If time signature is 4/4 I was thinking we can do timeSigOne/timeSigTwo
    // I like that idea you sexy beast
    data class Metronome(var bpm: Int,
                         var timeSigOne: Int,
                         var timeSigTwo: Int,
                         var playDuringRecording: Boolean)

    // Creates a track which will contain a path to the required audio file
    data class Track(var trackName : String, var filePath: String)

    // Creates a Song object which will contain an ArrayList of file paths (Tracks) to the specific
    // audio tracks that make up the Song.
    data class Song(var tracks: ArrayList<Track>)
}
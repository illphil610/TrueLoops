package com.newwesterndev.trueloops.model

object Model {

    data class Song(var name: String,
                    var dateCreated: String,
                    var bars: Int,
                    var measures: Int,
                    var bpm: Int,
                    var timeSigOne: Int,
                    var timeSigTwo: Int,
                    var playDuringRecording: Int,
                    var playMetronome: Int,
                    var countInBars: Int)

    data class Track(var trackName: String,
                     var fromSongName: String,
                     var filePath: String,
                     var armed: Int,
                     var willplay: Int)
}
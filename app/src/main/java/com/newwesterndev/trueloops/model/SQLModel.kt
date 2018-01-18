package com.newwesterndev.trueloops.model

object SQLModel {

    data class Song(var id: Int,
                    var name: String,
                    var bars: Int,
                    var measures: Int,
                    var bpm: Int,
                    var timeSigOne: Int,
                    var timeSigTwo: Int,
                    var playDuringRecording: Int,
                    var playMetronome: Int,
                    var countInBars: Int)

    data class Track(var id: Int,
                     var trackName: String,
                     var fromSongName: String,
                     var filePath: String,
                     var armed: Int,
                     var willplay: Int)
}
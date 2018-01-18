package com.newwesterndev.trueloops.utils

import com.newwesterndev.trueloops.model.Model
import com.newwesterndev.trueloops.modules.NewSongModule
import dagger.Component

@Component(modules = arrayOf(NewSongModule::class))
interface NewSongComponent{
    fun getNewSong(): Model.Song
}
package com.newwesterndev.trueloops.modules

import com.newwesterndev.trueloops.model.Model
import dagger.Module
import dagger.Provides

@Module
class NewSongModule{

    @Provides
    fun song(): Model.Song{
        return Model.Song("New Song", 4, 1, 120, 4, 4,
                0, 0, 0)
    }
}
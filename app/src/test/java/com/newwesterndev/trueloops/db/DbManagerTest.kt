package com.newwesterndev.trueloops.db

import android.content.Context
import android.os.Build.VERSION_CODES.LOLLIPOP
import com.newwesterndev.trueloops.BuildConfig
import com.newwesterndev.trueloops.MainActivity
import junit.framework.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.robolectric.RobolectricGradleTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(RobolectricGradleTestRunner::class)
@Config(constants = BuildConfig::class, sdk = intArrayOf(LOLLIPOP))
class DbManagerTest{

    @Mock lateinit var dbManager: DbManager

    @Before
    fun setUp(){
        dbManager = DbManager(RuntimeEnvironment.application)
    }

}
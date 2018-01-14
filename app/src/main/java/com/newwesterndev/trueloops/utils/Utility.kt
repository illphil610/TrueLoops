package com.newwesterndev.trueloops.utils

import android.content.Context
import android.os.Environment
import android.widget.Toast
import com.newwesterndev.trueloops.model.Model
import com.newwesterndev.trueloops.model.SQLModel
import java.io.File

/**
 * Created by Phil on 1/6/2018.
 */
class Utility {

    fun createUniqueTrackFile(): File {
        val path = File (Environment.getExternalStorageDirectory().absolutePath)
        val mFile = File.createTempFile ("temporary", ".3gp", path)
        return mFile
    }

    fun showToast(context : Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
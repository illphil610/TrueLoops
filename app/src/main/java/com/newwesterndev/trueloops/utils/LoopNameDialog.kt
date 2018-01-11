package com.newwesterndev.trueloops.utils

import android.app.AlertDialog
import android.app.Dialog
import android.app.DialogFragment
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.widget.EditText
import com.newwesterndev.trueloops.R
import kotlinx.android.synthetic.main.give_song_name_dialog.*
import kotlinx.android.synthetic.main.give_song_name_dialog.view.*

class LoopNameDialog: DialogFragment(){

    private var mListener: LoopNameDialogListener? = null

    interface LoopNameDialogListener{
        fun onDialogPositiveClick(dialogFragment: DialogFragment, loopName: String)
        fun onDialogNegativeClick(dialogFragment: DialogFragment)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        val inflater = activity.layoutInflater
        val layout = inflater.inflate(R.layout.give_song_name_dialog, null)

        builder.setView(layout)
                .setPositiveButton(R.string.save, DialogInterface.OnClickListener({_, _ ->
                    mListener?.onDialogPositiveClick(this, layout.loop_name_edit.text.toString())
                }))
                .setNegativeButton(R.string.cancel, DialogInterface.OnClickListener({_, _ ->
                    mListener?.onDialogNegativeClick(this)
                }))

        return builder.create()
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mListener = context as (LoopNameDialogListener)
    }

}
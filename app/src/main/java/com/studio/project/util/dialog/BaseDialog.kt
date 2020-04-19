package com.studio.project.util.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable

open class BaseDialog {
    fun getDialog(context: Context, layout: Int, isCancelable: Boolean): Dialog {
        val dialog = Dialog(context)
        dialog.setContentView(layout)
        dialog.setCancelable(isCancelable)
        if (dialog.window != null) {
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        return dialog
    }
}
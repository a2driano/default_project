package com.studio.project.util.dialog

import android.app.Dialog
import android.content.Context
import com.studio.project.R

class ProgressDialog : BaseDialog() {
    fun build(activity: Context): Dialog {
        val dialog = getDialog(
            activity,
            R.layout.dialog_progress_infinity,
            false
        )
//        val decorView = dialog.window?.decorView

        dialog.setCanceledOnTouchOutside(false)
        dialog.setCancelable(false)
        return dialog
    }
}
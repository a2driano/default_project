package com.studio.project.util.dialog

import android.app.Dialog
import android.content.Context
import com.studio.project.R
import com.studio.project.util.extension.gone
import com.studio.project.util.extension.visible
import kotlinx.android.synthetic.main.dialog_base.*

class DialogCustom : BaseDialog() {

    private var title: String? = null
    private var body: String? = null
    private var rightButtonText: String? = null
    private var leftButtonText: String? = null
    private var rightCallback: (() -> Unit)? = {}
    private var leftCallback: (() -> Unit)? = {}
    private var isCancelable: Boolean = false
    private var dismissAfterClick: Boolean = true

    fun title(t: String?) = apply { title = t }
    fun body(b: String?) = apply { body = b }
    fun rightButtonText(txt: String?) = apply { rightButtonText = txt }
    fun leftButtonText(txt: String?) = apply { leftButtonText = txt }
    fun rightButtonCallback(call: (() -> Unit)) = apply { rightCallback = call }
    fun leftButtonCallback(call: (() -> Unit)) = apply { leftCallback = call }
    fun onlyRightButton() = apply { leftCallback = null }
    fun isCancelable(c: Boolean) = apply { isCancelable = c }
    fun doNotDismissWhenClick() = apply { dismissAfterClick = false }

    fun build(activity: Context): Dialog {

        if (rightButtonText == null || rightButtonText?.length == 0) {
            rightButtonText = activity.getString(R.string.str_ok)
        }
        if (leftButtonText == null || leftButtonText?.length == 0) {
            leftButtonText = activity.getString(R.string.str_cancel)
        }
        return buildDialog(activity)
    }

    private fun buildDialog(activityContext: Context): Dialog {

        val dialog = getDialog(activityContext, R.layout.dialog_base, isCancelable)

        if ((title == null || title!!.isEmpty()) && (body == null || body!!.isEmpty())) {
            dialog.tv_title.gone()
            dialog.tv_body.text = activityContext.getString(R.string.error_something_went_wrong)
            dialog.tv_body.visible()

        } else if (title == null || title!!.isEmpty()) {
            dialog.tv_title.gone()

        } else if (body == null || body!!.isEmpty()) {
            dialog.tv_body.gone()
        }

        title?.let { t ->
            dialog.tv_title.text = t
        }

        body?.let { b ->
            dialog.tv_body.text = b
        }

        if (leftCallback == null) {
            dialog.rel_btn_second.gone()

        } else {
            dialog.rel_btn_second.setOnClickListener {
                leftCallback!!()

                if (dismissAfterClick) {
                    dialog.dismiss()
                }
            }
        }

        dialog.rel_btn_first.setOnClickListener {
            if (rightCallback != null) {
                rightCallback!!()
            }

            if (dismissAfterClick) {
                dialog.dismiss()
            }
        }

        rightButtonText?.let { t ->
            dialog.tv_btn_first_text.text = t
        }

        leftButtonText?.let { t ->
            dialog.tv_btn_second_text.text = t
        }

        return dialog
    }
}
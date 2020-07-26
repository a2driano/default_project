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
    private var buttonPositiveText: String? = null
    private var buttonNegativeText: String? = null
    private var buttonNeutralText: String? = null
    private var buttonPositiveCallback: (() -> Unit)? = {}
    private var buttonNegativeCallback: (() -> Unit)? = {}
    private var buttonNeutralCallback: (() -> Unit)? = {}
    private var isCancelable: Boolean = false
    private var dismissAfterClick: Boolean = true

    fun title(t: String?) = apply { title = t }
    fun body(b: String?) = apply { body = b }
    fun buttonPositiveText(txt: String?) = apply { buttonPositiveText = txt }
    fun buttonNegativeText(txt: String?) = apply { buttonNegativeText = txt }
    fun buttonNeutralText(txt: String?) = apply { buttonNeutralText = txt }
    fun buttonPositiveCallback(call: (() -> Unit)) = apply { buttonPositiveCallback = call }
    fun buttonNegativeCallback(call: (() -> Unit)) = apply { buttonNegativeCallback = call }
    fun buttonNeutralCallback(call: (() -> Unit)) = apply { buttonNeutralCallback = call }
    fun oneButton() = apply {
        buttonNegativeCallback = null
        buttonNeutralCallback = null
    }
    fun twoButton() = apply {
        buttonNeutralCallback = null
    }
    fun isCancelable(c: Boolean) = apply { isCancelable = c }
    fun doNotDismissWhenClick() = apply { dismissAfterClick = false }

    fun build(activity: Context): Dialog {

        if (buttonPositiveText == null || buttonPositiveText?.length == 0) {
            buttonPositiveText = activity.getString(R.string.str_ok)
        }
        if (buttonNegativeText == null || buttonNegativeText?.length == 0) {
            buttonNegativeText = activity.getString(R.string.str_cancel)
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

        if (buttonNegativeCallback == null) {
            dialog.btn_negative.gone()
        } else {
            dialog.btn_negative.setOnClickListener {
                buttonNegativeCallback!!()
                if (dismissAfterClick) {
                    dialog.dismiss()
                }
            }
        }

        if (buttonNeutralCallback == null) {
            dialog.btn_neutral.gone()
        } else {
            dialog.btn_neutral.setOnClickListener {
                buttonNeutralCallback!!()
                if (dismissAfterClick) {
                    dialog.dismiss()
                }
            }
        }

        dialog.btn_positive.setOnClickListener {
            if (buttonPositiveCallback != null) {
                buttonPositiveCallback!!()
            }
            if (dismissAfterClick) {
                dialog.dismiss()
            }
        }

        buttonPositiveText?.let { t ->
            dialog.text_btn_positive.text = t
        }

        buttonNeutralText?.let { t ->
            dialog.text_btn_neutral.text = t
        }

        buttonNegativeText?.let { t ->
            dialog.text_btn_negative.text = t
        }

        return dialog
    }
}
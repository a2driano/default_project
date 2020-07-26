package com.studio.project.presentation.common.toolbar

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.github.torindev.lgi_android.Lgi
import com.studio.project.R
import com.studio.project.util.extension.hide
import com.studio.project.util.extension.show
import kotlinx.android.synthetic.main.layout_toolbar_white.view.*

/**
 * Created by Andrew on 25.04.2020
 */
class CustomToolbar : RelativeLayout {
    init {
        LayoutInflater.from(context).inflate(R.layout.layout_toolbar_white, this, true)
    }

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
            : super(context, attrs, defStyleAttr) {

        attrs?.let { a ->
            initAttrs(a)
        }

        init()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int)
            : super(context, attrs, defStyleAttr, defStyleRes) {

        attrs?.let { a ->
            initAttrs(a)
        }

        init()
    }

    fun showBackButton() {
        rel_btn_back_toolbar.show()
    }

    fun hideBackButton() {
        rel_btn_back_toolbar.hide()
    }

    fun setAlphaBackground(a: Float) {
        val a1 = 255.toFloat() * a
        progress_layout.background.mutate().alpha = a1.toInt()
    }

    fun setTitleAlpha(a: Float) {
        tv_title.alpha = a
    }

    fun onClickBack(onCall: () -> Unit) {
        rel_btn_back_toolbar.setOnClickListener {
            if (isBackButtonEnable) {
                onCall()
            }
        }
    }

    fun onClickNotification(onCall: () -> Unit) {
        icon_layout.setOnClickListener {
            onCall()
        }
    }

    fun setTitle(text: String) {
        this.title = text
        tv_title.text = text
    }

    fun showBadge(count: Int) {
        if (count < 1) {
            hideBadge()
            return
        }
        badge_layout.show()
        tv_badge.text = count.toString()
    }

    fun hideBadge() {
        badge_layout.hide()
    }

    private var title: String = ""

    private fun init() {
        tv_title.text = title

        if (isBackButtonEnable) {
            showBackButton()
        } else {
            hideBackButton()
        }
    }

    private var isBackButtonEnable = false
    private var isNotifyActive = false

    private fun initAttrs(attrs: AttributeSet) {
        val array = context.obtainStyledAttributes(attrs, R.styleable.CustomToolbar)
        try {
            title = array.getString(R.styleable.CustomToolbar_title) ?: ""
            isBackButtonEnable =
                array.getBoolean(R.styleable.CustomToolbar_isBackAvailable, isBackButtonEnable)
            isNotifyActive =
                array.getBoolean(R.styleable.CustomToolbar_isNotifyActive, isNotifyActive)
        } catch (ex: Throwable) {
            Lgi.err(ex)
        } finally {
            array.recycle()
        }

        val curContext = context

        icon_layout.setOnClickListener {

        }
    }
}
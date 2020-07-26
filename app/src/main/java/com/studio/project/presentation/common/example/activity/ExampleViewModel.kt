package com.studio.project.presentation.common.example.activity

import android.content.Context
import com.studio.project.data.session.AppSession
import com.studio.project.presentation.common.BaseViewModel
import timber.log.Timber

/**
 * Created by Andrew on 20.04.2020
 */
class ExampleViewModel(private val context: Context, private val session: AppSession) :
    BaseViewModel(context, session) {
    init {
        Timber.e("--- MainViewModel init")
    }

    override fun viewIsReady() {
        super.viewIsReady()
        Timber.i("--- MainViewModel viewIsReady")
    }
}
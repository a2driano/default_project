package com.studio.project

import androidx.multidex.MultiDexApplication
import timber.log.Timber

/**
 * Created by Andrew on 19.04.2020
 */
class App : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()

        //timber
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
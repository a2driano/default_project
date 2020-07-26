package com.studio.project

import android.content.Context
import androidx.multidex.MultiDexApplication
import com.github.torindev.lgi_android.Lgi
import com.studio.project.data.constants.AppConstants.Server.BASE_URL_STAGING
import com.studio.project.data.prefs.Preferences
import com.studio.project.di.ComponentsHolder
import timber.log.Timber

/**
 * Created by Andrew on 19.04.2020
 */
class App : MultiDexApplication() {

    val componentsHolder: ComponentsHolder by lazy {
        ComponentsHolder(this)
    }


    override fun onCreate() {
        super.onCreate()

        //timber
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        Preferences.setServerAPI(this, BASE_URL_STAGING)

        Lgi.sLog = BuildConfig.DEBUG
        componentsHolder.init()
    }
}

fun getApp(context: Context) = context.applicationContext as App

package com.studio.project.di

import android.content.Context
import com.studio.project.data.session.AppSession
import com.studio.project.data.session.AppSessionImpl
import dagger.Module
import dagger.Provides

@Module
class SessionModule {
    @AppScope
    @Provides
    internal fun provideSession(context: Context): AppSession {
        return AppSessionImpl(context)
    }
}



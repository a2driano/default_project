package com.studio.project.presentation.common.example.activity.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.studio.project.data.session.AppSession
import com.studio.project.di.ViewComponent
import com.studio.project.di.ViewComponentBuilder
import com.studio.project.di.ViewModule
import com.studio.project.presentation.common.example.activity.ExampleActivity
import com.studio.project.presentation.common.example.activity.ExampleViewModel
import com.studio.project.util.extension.viewModelFactory
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class MainActivityScope

@Module
class MainActivityModule : ViewModule {

    @MainActivityScope
    @Provides
    internal fun provideFactory(context: Context, session: AppSession): ViewModelProvider.Factory {
        return viewModelFactory {
            ExampleViewModel(
                context,
                session
            )
        }
    }
}

@MainActivityScope
@Subcomponent(modules = [MainActivityModule::class])
interface MainActivityComponent : ViewComponent<ExampleActivity> {

    @Subcomponent.Builder
    interface Builder :
        ViewComponentBuilder<MainActivityComponent, MainActivityModule>

}
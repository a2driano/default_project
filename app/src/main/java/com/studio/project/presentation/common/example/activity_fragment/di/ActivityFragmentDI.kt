package com.studio.project.presentation.common.example.activity_fragment.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.studio.project.data.session.AppSession
import com.studio.project.di.ViewComponent
import com.studio.project.di.ViewComponentBuilder
import com.studio.project.di.ViewModule
import com.studio.project.presentation.common.example.activity_fragment.ExampleActivityFragmentViewModel
import com.studio.project.presentation.common.example.activity_fragment.ExampleActivityFragment
import com.studio.project.util.extension.viewModelFactory
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ExampleActivityFragmentScope

@Module
class ExampleActivityFragmentModule : ViewModule {

    @ExampleActivityFragmentScope
    @Provides
    internal fun provideFactory(context: Context, session: AppSession): ViewModelProvider.Factory {
        return viewModelFactory {
            ExampleActivityFragmentViewModel(
                context,
                session
            )
        }
    }
}

@ExampleActivityFragmentScope
@Subcomponent(modules = [ExampleActivityFragmentModule::class])
interface ExampleActivityFragmentComponent : ViewComponent<ExampleActivityFragment> {

    @Subcomponent.Builder
    interface Builder :
        ViewComponentBuilder<ExampleActivityFragmentComponent, ExampleActivityFragmentModule>

}
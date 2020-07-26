package com.studio.project.presentation.common.example.fragment.di

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import com.studio.project.data.session.AppSession
import com.studio.project.di.ViewComponent
import com.studio.project.di.ViewComponentBuilder
import com.studio.project.di.ViewModule
import com.studio.project.presentation.common.example.fragment.ExampleFragment
import com.studio.project.presentation.common.example.fragment.ExampleFragmentViewModel
import com.studio.project.util.extension.viewModelFactory
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ExampleFragmentScope

@Module
class ExampleFragmentModule : ViewModule {

    @ExampleFragmentScope
    @Provides
    internal fun provideFactory(context: Context, session: AppSession): ViewModelProvider.Factory {
        return viewModelFactory {
            ExampleFragmentViewModel(
                context,
                session
            )
        }
    }
}

@ExampleFragmentScope
@Subcomponent(modules = [ExampleFragmentModule::class])
interface ExampleFragmentComponent : ViewComponent<ExampleFragment> {

    @Subcomponent.Builder
    interface Builder :
        ViewComponentBuilder<ExampleFragmentComponent, ExampleFragmentModule>

}
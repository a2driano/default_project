package com.studio.project.di

import android.content.Context
import com.studio.project.presentation.common.example.activity.ExampleActivity
import com.studio.project.presentation.common.example.activity.di.MainActivityComponent
import com.studio.project.presentation.common.example.activity_fragment.ExampleActivityFragment
import com.studio.project.presentation.common.example.activity_fragment.di.ExampleActivityFragmentComponent
import com.studio.project.presentation.common.example.fragment.ExampleFragment
import com.studio.project.presentation.common.example.fragment.di.ExampleFragmentComponent
import dagger.Component
import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap
import javax.inject.Scope

interface ViewModule

interface ViewComponent<in V> {
    fun inject(view: V)
}

interface ViewComponentBuilder<out C : ViewComponent<*>, in M : ViewModule> {
    fun build(): C
    fun module(module: M): ViewComponentBuilder<C, M>
}

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class AppScope

@AppScope
@Component(
    modules = [
        AppModule::class,
        SessionModule::class,
        NetworkModule::class
    ]
)
interface AppComponent {
    fun injectComponentsHolder(componentsHolder: ComponentsHolder)
}

@Module(
    subcomponents = [
        MainActivityComponent::class,
        ExampleActivityFragmentComponent::class,
        ExampleFragmentComponent::class
    ]
)
class AppModule(private val context: Context) {

    @AppScope
    @Provides
    fun provideContext(): Context = context

    @Provides
    @IntoMap
    @ClassKey(ExampleActivity::class)
    fun provideMainActivityBuilder(builder: MainActivityComponent.Builder): ViewComponentBuilder<*, *> {
        return builder
    }

    @Provides
    @IntoMap
    @ClassKey(ExampleActivityFragment::class)
    fun provideExampleActivityFragmentBuilder(builder: ExampleActivityFragmentComponent.Builder): ViewComponentBuilder<*, *> {
        return builder
    }

    @Provides
    @IntoMap
    @ClassKey(ExampleFragment::class)
    fun provideExampleFragmentBuilder(builder: ExampleFragmentComponent.Builder): ViewComponentBuilder<*, *> {
        return builder
    }

}


package com.studio.project.di

import android.content.Context
import com.github.torindev.lgi_android.Lgi
import javax.inject.Inject
import javax.inject.Provider


class ComponentsHolder(private val context: Context) {

    @Inject
    lateinit var builders: MutableMap<Class<*>, Provider<ViewComponentBuilder<*, *>>>

    private var components: MutableMap<Class<*>, ViewComponent<*>?> = HashMap()

    var appComponent: AppComponent? = null
    var networkComponent: AppComponent? = null

    fun init() {
        appComponent = DaggerAppComponent.builder().appModule(
            AppModule(
                context
            )
        ).build()

        appComponent?.injectComponentsHolder(this)
    }

    fun getViewComponent(cls: Class<*>): ViewComponent<*> {
        var component: ViewComponent<*>? = components[cls]
        if (component == null) {
            val builder: ViewComponentBuilder<*, *>? = builders[cls]?.get()
            component = builder?.build()
            components[cls] = component
        }
        return component!!
    }

    fun releaseViewComponent(cls: Class<*>) {
        components[cls] = null
        printComponents()
    }

    private fun printComponents() {
//        for ((cl, component) in components) {
//            Lgi.p("class ${cl} = ${component}")
//        }
    }
}
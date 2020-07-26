package com.studio.project.di

import android.content.Context
import com.google.gson.GsonBuilder
import com.studio.project.BuildConfig
import com.studio.project.data.network.api.AppApi
import com.studio.project.data.network.interceptor.RefreshTokenInterceptor
import com.studio.project.data.prefs.Preferences
import com.studio.project.util.network.HiddenAnnotationExclusionStrategy
import dagger.Module
import dagger.Provides
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Andrew on 27.04.2020
 */
@Module
class NetworkModule {

    companion object {
        fun getNewRetrofit(context: Context, client: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl(Preferences.getServerAPI(context))
                .client(client)
                .addConverterFactory(
                    GsonConverterFactory.create(
                        GsonBuilder()
                            .serializeNulls()
                            .setExclusionStrategies(HiddenAnnotationExclusionStrategy())
                            .setPrettyPrinting()
                            .setLenient()
                            .create()
                    )
                )
                .build()
        }
    }

    @AppScope
    @Provides
    fun provideClient(
        interceptor: HttpLoggingInterceptor,
        refreshInterceptor: RefreshTokenInterceptor
    ): OkHttpClient {
        val builder = if (BuildConfig.DEBUG) {
            OkHttpClient.Builder()
                .addInterceptor(interceptor)
        } else {
            OkHttpClient.Builder()
        }
        return builder
            .dispatcher(Dispatcher().apply { maxRequests = 1 })
            .addInterceptor(refreshInterceptor)
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .build()
    }

    @AppScope
    @Provides
    fun provideInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @AppScope
    @Provides
    fun provideRefreshInterceptor(context: Context): RefreshTokenInterceptor {
        return RefreshTokenInterceptor(
            context
        )
    }

    @AppScope
    @Provides
    fun provideRetrofit(client: OkHttpClient, context: Context): Retrofit {
        return getNewRetrofit(context, client)
    }

    @AppScope
    @Provides
    fun provideServerAPI(retrofit: Retrofit): AppApi {
        return retrofit.create<AppApi>(
            AppApi::class.java
        )
    }
}
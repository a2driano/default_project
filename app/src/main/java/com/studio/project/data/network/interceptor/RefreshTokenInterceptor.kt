package com.studio.project.data.network.interceptor

import android.content.Context
import com.studio.project.data.constants.AppConstants.Server.addressExceptions
import com.studio.project.data.model.request.RefreshTokenRequest
import com.studio.project.data.network.api.RefreshApi
import com.studio.project.data.prefs.Preferences
import com.studio.project.di.NetworkModule
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException

/**
 * Created by Andrew on 27.04.2020
 */
class RefreshTokenInterceptor(
    private val context: Context
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        synchronized(this) {
            val originalRequest = chain.request()
            //check if url from another domain
            if (isAddressInException(originalRequest)) {
                return chain.proceed(originalRequest)
            }
            val prefixToken = ""
            val authenticationRequest = originalRequest.newBuilder()
                .addHeader("Authorization", prefixToken)
                .build()
            val initialResponse = chain.proceed(authenticationRequest)

            when {
                initialResponse.code == 403 || initialResponse.code == 401 -> {
                    val responseNewTokenLoginModel = runBlocking {
                        val client = OkHttpClient.Builder()
                            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                            .build()

                        NetworkModule
                            .getNewRetrofit(context, client)
                            .create(RefreshApi::class.java)
                            .refresh(RefreshTokenRequest().apply {
                                token = Preferences.getRefreshToken(context)
                            })
                    }

                    return when {
                        responseNewTokenLoginModel.code() != 200 -> {
                            Preferences.logout(context)
                            initialResponse.close()
                            chain.proceed(authenticationRequest)
                        }
                        else -> {
                            responseNewTokenLoginModel.body()?.let { authModel ->
//                                Prefs.refreshTokens(context, authModel)
                            }
//                            val prefixToken2 = getPrefixToken(context)
                            val prefixToken2 = "getPrefixToken(context)"
                            val newAuthenticationRequest = originalRequest.newBuilder().addHeader(
                                "Authorization", "$prefixToken2"
                            ).build()
                            initialResponse.close()
                            chain.proceed(newAuthenticationRequest)
                        }
                    }
                }
                else -> return initialResponse
            }
        }
    }

    private fun isAddressInException(request: Request): Boolean {
        val url = request.url.toUrl().toString()
        addressExceptions.forEach {
            if (url.contains(it, ignoreCase = true)) {
                return true
            }
        }
        return false
    }
}
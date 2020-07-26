package com.studio.project.data.prefs

import android.content.Context
import com.github.torindev.lgi_android.Lgi
import com.studio.project.data.model.AuthModel
import com.studio.project.data.model.User
import com.studio.project.util.extension.fromJson
import timber.log.Timber

/**
 * Created by Andrew on 27.04.2020
 */
object Preferences : BasePreferences() {
    private const val ARG_SERVER_API = "ARG_SERVER_API"
    private const val ARG_TOKEN_ACCESS = "ARG_TOKEN_ACCESS"
    private const val ARG_TOKEN_REFRESH = "ARG_TOKEN_REFRESH"
    private const val ARG_EXPIRES_ACCESS = "ARG_EXPIRES_ACCESS"
    private const val ARG_EXPIRES_REFRESH = "ARG_EXPIRES_REFRESH"
    private const val ARG_IS_FACEBOOK_LOGIN = "ARG_IS_FACEBOOK_LOGIN"
    private const val ARG_IS_USER_LOGIN_SAVED = "ARG_IS_USER_LOGIN_SAVED"

    private const val ARG_USER = "ARG_USER"

    fun setAppUser(context: Context, user: User?) {
        if (user == null) {
            removeRecord(context, ARG_USER)
            return
        }
        putString(context, ARG_USER, user.toJson())
    }

    fun getAppUser(context: Context): User? {
        return try {
            getString(context, ARG_USER).fromJson(User::class.java)
        } catch (ex: Throwable) {
            Lgi.err(ex)
            null
        }
    }

    fun logout(context: Context) {
        setAccessToken(context, null)
        setRefreshToken(context, null)
        setAccessExpires(context, null)
        setRefreshExpires(context, null)
        setLoginRemember(context, false)
        setIsFacebookLogin(context, false)

        setAppUser(context, null)
    }

    fun login(context: Context, authModel: AuthModel) {
        authModel.authData?.let { authData ->
            setAccessToken(context, authData.accessToken)
            setRefreshToken(context, authData.refreshToken)
            setAccessExpires(context, authData.expiresInAccess)
            setRefreshExpires(context, authData.expiresInRefresh)
        }
    }

    fun signUp(context: Context, authModel: AuthModel) {
        authModel.authData?.let { authData ->
            setAccessToken(context, authData.accessToken)
            setRefreshToken(context, authData.refreshToken)
            setAccessExpires(context, authData.expiresInAccess)
            setRefreshExpires(context, authData.expiresInRefresh)
        }
    }

    fun isAlreadyLogin(context: Context): Boolean {
        val isNeedToLogin = isLoginRememberChose(context)
        val isAccessTokenAvailable = getAccessToken(context).isNotEmpty()
        val isRefreshTokenAvailable = getRefreshToken(context).isNotEmpty()
        val isFbLogin = isFacebookLogin(context)
        return (isNeedToLogin && isAccessTokenAvailable && isRefreshTokenAvailable) || (isNeedToLogin && isFbLogin && isAccessTokenAvailable)
    }

    fun setLoginRemember(context: Context, isLogged: Boolean) {
        putBoolean(context, ARG_IS_USER_LOGIN_SAVED, isLogged)
    }

    fun isLoginRememberChose(context: Context): Boolean {
        return getBoolean(context, ARG_IS_USER_LOGIN_SAVED, false)
    }

    fun setIsFacebookLogin(context: Context, isFb: Boolean) {
        putBoolean(context, ARG_IS_FACEBOOK_LOGIN, isFb)
    }

    fun isFacebookLogin(context: Context): Boolean {
        return getBoolean(context, ARG_IS_FACEBOOK_LOGIN, false)
    }

    fun setRefreshExpires(context: Context, expiresDate: String?) {
        putString(context, ARG_EXPIRES_REFRESH, expiresDate)
    }

    fun getRefreshExpires(context: Context): String {
        return getString(context, ARG_EXPIRES_REFRESH)
    }

    fun setAccessExpires(context: Context, expiresDate: String?) {
        putString(context, ARG_EXPIRES_ACCESS, expiresDate)
    }

    fun getAccessExpires(context: Context): String {
        return getString(context, ARG_EXPIRES_ACCESS)
    }

    fun setRefreshToken(context: Context, token: String?) {
        putString(context, ARG_TOKEN_REFRESH, token)
    }

    fun getRefreshToken(context: Context): String {
        return getString(context, ARG_TOKEN_REFRESH)
    }

    fun setAccessToken(context: Context, token: String?) {
        putString(context, ARG_TOKEN_ACCESS, token)
    }

    fun getAccessToken(context: Context): String {
        Timber.e("--- http token: ${getString(context, ARG_TOKEN_ACCESS)}")
        return getString(context, ARG_TOKEN_ACCESS)
    }

    fun setServerAPI(context: Context, string: String?) {
        putString(context, ARG_SERVER_API, string)
    }

    fun getServerAPI(context: Context): String {
        return getString(context, ARG_SERVER_API)
    }

}
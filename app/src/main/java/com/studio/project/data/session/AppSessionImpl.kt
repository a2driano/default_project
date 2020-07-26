package com.studio.project.data.session

import android.content.Context
import com.studio.project.data.prefs.Preferences
import com.studio.project.data.model.AuthModel
import com.studio.project.data.model.User

/**
 * Created by Dmitry Torin on 2020-01-05.
 * mova.io
 * Slack: @dt
 */
class AppSessionImpl(
    private val context: Context
) : AppSession {

    private var user: User? = null

    init {

    }

    override fun setUser(muleUser: User) {
        this.user = muleUser
        Preferences.setAppUser(context, muleUser)
    }

    override fun getUser(): User? {
        if (user == null) {
            user == Preferences.getAppUser(context)
        }
        return user
    }

//    override fun loginByFb(loginResult: LoginResult) {
//        MulePrefs.setIsFacebookLogin(context, true)
//        MulePrefs.setAccessToken(context, loginResult.accessToken.token)
//    }

    override fun login(
        authModel: AuthModel,
        fromFacebook: Boolean
    ) {
        Preferences.setIsFacebookLogin(context, fromFacebook)
        login(authModel)
    }

    override fun login(authModel: AuthModel) {
        if (!Preferences.isFacebookLogin(context))
            Preferences.login(context, authModel)
        authModel.user?.let { user ->
            val muleUser = User.getBy(user)
            setUser(muleUser)
        }
    }

    override fun signUp(authModel: AuthModel) {
        Preferences.signUp(context, authModel)
        authModel.user?.let { user ->
            val muleUser = User.getBy(user)
            setUser(muleUser)
        }
    }

    override fun logout() {
        Preferences.logout(context)
        user = null
    }

    override fun isAlreadyLogIn(): Boolean {
        if (Preferences.isAlreadyLogin(context)) {
            return true
        }

        return false
    }
}
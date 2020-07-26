package com.studio.project.data.session

import com.studio.project.data.model.AuthModel
import com.studio.project.data.model.User

/**
 * Created by Dmitry Torin on 2020-01-05.
 * mova.io
 * Slack: @dt
 */
interface AppSession {
//    fun loginByFb(loginResult: LoginResult)
    fun login(
        authModel: AuthModel,
        fromFacebook: Boolean
    )
    fun login(authModel: AuthModel)
    fun signUp(authModel: AuthModel)
    fun setUser(muleUser: User)
    fun getUser(): User?
    fun isAlreadyLogIn(): Boolean
    fun logout()
}
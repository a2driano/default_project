package com.studio.project.data.model

import com.google.gson.Gson

/**
 * Created by Dmitry Torin on 2020-01-28.
 * mova.io
 * Slack: @dt
 */
class User {

    companion object {
        fun getBy(muleUserResponse: UserResponse) = User().apply {
            id = muleUserResponse.id
            name = muleUserResponse.name
            email = muleUserResponse.email
            phone = muleUserResponse.phoneNumber
        }
    }

    var id: String? = null
    var name: String? = null
    var email: String? = null
    var phone: String? = null

    fun toJson() = Gson().toJson(this)
}
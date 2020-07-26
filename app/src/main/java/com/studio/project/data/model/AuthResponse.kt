package com.studio.project.data.model

import com.google.gson.annotations.SerializedName

/**
 * Created by Dmitry Torin on 03.03.2020.
 * mova.io
 * Slack: @dt
 */
class AuthModel {
    @SerializedName("authData")
    var authData: AuthData? = null
    @SerializedName("user")
    var user: UserResponse? = null
}

class UserResponse {
    @SerializedName("id")
    var id: String? = null
    @SerializedName("email")
    var email: String? = null
    @SerializedName("name")
    var name: String? = null
    @SerializedName("phoneNumber")
    var phoneNumber: String? = null
    @SerializedName("isEmailApproved")
    var isEmailApproved: Boolean? = null
}

class AuthData {
    @SerializedName("accessToken")
    var accessToken: String? = null
    @SerializedName("refreshToken")
    var refreshToken: String? = null
    @SerializedName("expiresInAccess")
    var expiresInAccess: String? = null
    @SerializedName("expiresInRefresh")
    var expiresInRefresh: String? = null
}



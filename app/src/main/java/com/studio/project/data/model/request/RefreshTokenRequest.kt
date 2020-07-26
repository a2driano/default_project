package com.studio.project.data.model.request

import com.google.gson.annotations.SerializedName

/**
 * Created by Andrew on 05.02.2020
 */
class RefreshTokenRequest {
    @SerializedName("token")
    var token: String? = null
}
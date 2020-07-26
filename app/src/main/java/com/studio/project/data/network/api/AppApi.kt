package com.studio.project.data.network.api

import com.studio.project.data.model.AuthModel
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

/**
 * Created by Dmitry Torin on 2020-01-20.
 * mova.io
 * Slack: @dt
 */
interface AppApi {

    @POST("/v1/sign/login")
    suspend fun login(): Response<AuthModel>

}
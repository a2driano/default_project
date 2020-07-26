package com.studio.project.data.network.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by Andrew on 27.04.2020
 */
interface RefreshApi {
    //add url and params after
    @POST("/v1/sign/refresh")
    suspend fun refresh(@Body authRequest: Any): Response<Unit>
}
package com.studio.project.data.metadata

/**
 * Created by Dmitry Torin on 2020-01-20.
 * mova.io
 * Slack: @dt
 */
data class AppError(
    val title: String?,
    val body: String?,
    val exception: Throwable?,
    val code: Int
)
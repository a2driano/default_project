package com.studio.project.data.constants

/**
 * Created by Andrew on 27.04.2020
 */
object AppConstants {
    object Server {
        const val BASE_URL_STAGING = ""
        const val SERVER_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"

        // interceptor checks, if request contains any of this address,
        // then interceptor don't use authorization token
        val addressExceptions = mutableListOf("https://api.getAddress.io/find/")
        const val PROHIBITED_AND_RESTRICTED_URL = "http://www.mule.app/prohibiteditems"
    }

    object Analytics {
        const val ANALYTIC_TIME_PATTERN = "yyyy-MM-dd"
        const val ANALYTIC_DAY_LIMIT_LIST_SIZE = 31
    }

}
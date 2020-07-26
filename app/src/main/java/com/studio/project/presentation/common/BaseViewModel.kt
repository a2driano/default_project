package com.studio.project.presentation.common

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.studio.project.data.metadata.AppError
import com.studio.project.data.metadata.ProgressMetaData
import com.studio.project.data.session.AppSession
import com.studio.project.util.viewmodel_single_event.Event
import com.studio.project.util.viewmodel_single_event.LiveEvent
import com.studio.project.util.viewmodel_single_event.StringLiveEvent
import com.github.torindev.lgi_android.Lgi
import com.github.torindev.lgi_android.LgiUtils
import com.studio.project.util.extension.somethingWentWrong
import retrofit2.Response
import timber.log.Timber

/**
 * Created by Andrew on 25.04.2020
 */
open class BaseViewModel(
    private val context: Context,
    private val session: AppSession
) : ViewModel() {

    val showProgress =
        MutableLiveData(ProgressMetaData(title = null, isLoading = false))
    val showNoInternetConnection = LiveEvent()
    private val showToastShort = StringLiveEvent()
    private val showToastLong = StringLiveEvent()

    val showError = MutableLiveData<Event<AppError>>()
    val openClearLogin = LiveEvent()

    open fun initArgs(args: Bundle?) {

    }

    open fun setIntent(intent: Intent) {

    }

    open fun viewIsReady() {

    }

    open fun viewIsReady(intent: Intent) {

    }

    open fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

    }

    fun showToastShort(text: String) {
        showToastShort.produce(text)
    }

    fun getShowToastShortField() = showToastShort

    fun showToastLong(text: String) {
        showToastLong.produce(text)
    }

    fun getShowToastLongField() = showToastLong

    fun <T : Any> handleError(errorCode: String, response: Response<T>) {
        val errorBody = response.errorBody()
        when (response.code()) {
            401, 403 -> {
                openClearLogin.produce()
            }
            else -> {
                var httpError = context.somethingWentWrong()
                try {
//                    val errorContent = errorBody?.string() ?: ""
//                    val httpError1 = errorContent.fromJson(HttpError1::class.java)
//                    if (httpError1 == null) {
//                        val httpError2 = errorContent.fromJson(HttpError2::class.java)
//                        if (httpError2 != null) {
//                            httpError = httpError2.getAllMessages(context)
//                        }
//                    } else {
//                        httpError = httpError1.error ?: context.somethingWentWrong()
//                    }
                } catch (ex: Throwable) {
                    Lgi.err(ex)
                }
                showError(title = errorCode, body = httpError)
            }
        }

    }

    fun showError(
        title: String? = null,
        body: String? = null,
        exception: Throwable? = null,
        code: Int = -1
    ) {
        val err = AppError(title = title, body = body, exception = exception, code = code)
        showError.value = Event(err)
//        _isShowError.postValue(Event(err))
    }

    fun showConnectionError() {
        showNoInternetConnection.produce()
    }

    fun showProgress(text: String? = null) {
        Timber.i("--- progress showProgress")
        showProgress.value = ProgressMetaData(isLoading = true, title = text)
    }

    fun hideProgress() {
        Timber.e("--- progress hideProgress")
        showProgress.value = ProgressMetaData(isLoading = false)
    }

    fun postDelayed(duration: Long = 100, callback: () -> Unit) {
        Handler().postDelayed(callback, duration)
    }

    fun isOnline(): Boolean {
        var isOnline = false
        try {
            isOnline = LgiUtils.isOnline(context)
        } catch (ex: Throwable) {
            Lgi.err(ex)
            return isOnline
        }
        return isOnline
    }

    fun getString(stringId: Int): String {
        return try {
            context.getString(stringId)
        } catch (ex: Throwable) {
            Lgi.err(ex)
            ""
        }
    }
}
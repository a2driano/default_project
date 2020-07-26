package com.studio.project.presentation.common

import android.app.Activity
import android.app.Dialog
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.torindev.lgi_android.Lgi
import com.github.torindev.lgi_android.LgiUtils
import com.studio.project.R
import com.studio.project.di.ViewComponent
import com.studio.project.getApp
import com.studio.project.util.dialog.DialogCustom
import com.studio.project.util.dialog.ProgressDialog
import com.studio.project.util.extension.*
import com.studio.project.util.viewmodel_single_event.EventObserver
import kotlinx.android.synthetic.main.dialog_progress_infinity.*
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject
import kotlin.reflect.KClass


/**
 * Created by Andrew on 25.04.2020
 */
abstract class BaseActivity<T : BaseViewModel>(
    private val clazz: Class<T>
) : AppCompatActivity(), BaseContract.ActivityView {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    val viewModel: T by lazy {
        ViewModelProvider(this, factory).get(clazz)
    }


//    fun <T: Any> retrieveSomething(ofClass: KClass<T>): T {
//        return ofClass.javaObjectType
//    }
//
//    inline fun <reified T: BaseViewModel> retrieveSomething(): T {
//        return retrieveSomething(T::class)
//    }

//    val viewModel: T by lazy {
//        ViewModelProvider(this, factory).get(clazz)
//    }

    protected lateinit var binding: ViewDataBinding

    abstract fun getLayoutId(): Int

    abstract fun initViewComponents()

    abstract fun initDataBinding()

    abstract fun initUI(bundle: Bundle? = null)

    open fun getStatusBarColor() = R.color.colorPrimary

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            var color = getStatusBarColor()
            if (color == R.color.colorWhite) {
                if (Build.VERSION.SDK_INT == Build.VERSION_CODES.LOLLIPOP) {
                    color = R.color.colorGreyStatusBar
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    window.decorView.systemUiVisibility = (SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
                }
            }
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = ContextCompat.getColor(this, color)
        }

        initViewComponents()
        initDataBinding()
        initUI(savedInstanceState)
        initBaseViewModel()
    }

    protected fun <T : ViewDataBinding> bind(onBindingDone: (T) -> Unit) {
        val b: T = DataBindingUtil.setContentView(this, getLayoutId())
        b.lifecycleOwner = this
        b.executePendingBindings()
        onBindingDone(b)
    }

    open fun getBaseVm(): BaseViewModel? {
        return null
    }

    private fun initBaseViewModel() {
        viewModel.viewIsReady()
        initProgressObserver()
        initConnectionErrorObserver()
        initShowErrorObserver()
        initErrorHandling()

        viewModel.getShowToastShortField().observe(this, EventObserver {
            Lgi.toastShort(this, it)
        })

        viewModel.getShowToastLongField().observe(this, EventObserver {
            Lgi.toastLong(this, it)
        })
    }

    open fun initErrorHandling() {
        viewModel.openClearLogin.observe(this, EventObserver {
//            val intent = SignInActivity.newIntent(this)
//            intent.flags =
//                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(intent)
//            finish()
        })
    }

    open fun initShowErrorObserver() {
        viewModel.showError.observe(this, EventObserver { throwable ->
            showError(throwable.title, throwable.body, throwable.exception, null)
        })
    }

    open fun initConnectionErrorObserver() {
        viewModel.showNoInternetConnection.observe(this, EventObserver {
            showConnectionError(null)
        })
    }

    open fun initProgressObserver() {
        viewModel.showProgress.observe(this, Observer { pMd ->
            if (pMd.isLoading) {
                showProgress(pMd.title)
            } else {
                hideProgress()
            }
        })
    }

    fun postDelayed(dur: Long = 100, onPost: () -> Unit) {
        Handler().postDelayed(onPost, dur)
    }

    override fun showConnectionError(onOk: (() -> Unit)?) {
        DialogCustom()
            .title(getString(R.string.str_no_internet_connection_title))
            .body(getString(R.string.str_no_internet_connection_body))
            .oneButton()
            .buttonPositiveText(getString(R.string.str_ok))
            .buttonPositiveCallback {

            }
            .isCancelable(true)
            .build(this)
            .show()
    }

    private var dialogProgress: Dialog? = null

    override fun showProgress(text: String?) {
        val result = if (text == null || text.isEmpty()) getString(R.string.str_loading) else text
        showProgressResult(result)
    }

    override fun showProgress(strId: Int) {
        var result = getString(R.string.str_loading)
        try {
            result = if (strId > 0) getString(strId) else result
        } catch (err: Throwable) {
            Lgi.err(err)
        }
        showProgressResult(result)
    }

    private fun showProgressResult(title: String) {
        if (dialogProgress != null) {
            dialogProgress?.dismiss()
            dialogProgress = null
        }

        dialogProgress = ProgressDialog().build(this)
        dialogProgress?.tv_title?.text = title
        dialogProgress?.show()
        dialogProgress?.lin_progress?.show()
    }

    override fun hideProgress() {
        dialogProgress?.lin_progress?.invisible()
        dialogProgress?.dismiss()
        dialogProgress = null
    }

    override fun showToast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }

    override fun showError(
        title: String?,
        body: String?,
        exception: Throwable?,
        onOk: (() -> Unit)?
    ) {
        var titleResult = ""
        if (title != null && title.isNotEmpty()) {
            titleResult = title

        } else {
            titleResult = unexpectedError()
        }

        var message = ""
        if (body != null && body.isNotEmpty()) {
            message = body

            if (exception?.message != null && exception.message?.length ?: 0 > 0) {
                message += " (${exception.message})"
            }
        } else if (exception?.message != null && exception.message?.length ?: 0 > 0) {
            message = exception.message ?: ""
        }

        if (exception != null) {
            Timber.e(exception)
        }

        if (exception != null && exception is HttpException) {
            DialogCustom()
                .title(unexpectedError())
                .body("Need to implement handler of HttpException")
                .oneButton()
                .buttonPositiveText(getString(R.string.str_ok))
                .buttonPositiveCallback {

                }
                .isCancelable(true)
                .build(this)
                .show()

            when ((exception).code()) {
                400 -> {

                }
                401 -> {

                }
                403 -> {

                }
                404 -> {

                }
                502 -> {
                    val servErrorMess = "${getString(R.string.str_server_error)} Bad Gateway"
                }
                503 -> {
                    val servErrorMess =
                        "${getString(R.string.str_server_error)} Service Unavailable"
                }
                504 -> {
                    val servErrorMess = "${getString(R.string.str_server_error)} Gateway Timeout"
                }
                else -> {
                    val servErrorMess = "${getString(R.string.str_server_error)} -"
                }
            }
        } else {
            DialogCustom()
                .title(titleResult)
                .body(message)
                .oneButton()
                .buttonPositiveText(getString(R.string.str_ok))
                .buttonPositiveCallback {
                    if (onOk != null) {
                        onOk()
                    }
                }
                .isCancelable(false)
                .build(this)
                .show()
        }
    }

    fun <DI : ViewComponent<*>> getComponent(): DI? {
        return getApp(this)
            .componentsHolder
            .getViewComponent(this::class.java) as DI?
    }

    override fun onDestroy() {
        Timber.i("onDestroyView !!! -> ${this::class.java}}")
        val cls = this::class.java
        getApp(this)
            .componentsHolder.releaseViewComponent(cls)
        super.onDestroy()
        Timber.i("activity :: onDestroy() :: ${this::class.java.simpleName.toString()}")
        dialogProgress?.progress?.stopAnimations()
    }

    override fun hideKeyboard() {
        try {
            LgiUtils.hideKeyboard(this)
        } catch (ex: Throwable) {
            Timber.e(ex)
        }
    }

    override fun getActivity(): Activity {
        return this
    }
}
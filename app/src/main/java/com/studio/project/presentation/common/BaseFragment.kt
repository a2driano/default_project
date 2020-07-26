package com.studio.project.presentation.common


import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.github.torindev.lgi_android.Lgi
import com.studio.project.di.ViewComponent
import com.studio.project.getApp
import com.studio.project.util.viewmodel_single_event.EventObserver
import javax.inject.Inject

/**
 * Created by Andrew on 25.04.2020
 */
abstract class BaseFragment<T : BaseViewModel>(
    private val clazz: Class<T>, private val isActivityLevel: Boolean = false
) : Fragment(), BaseContract.FragmentView {

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    val viewModel: T by lazy {
        //detect Viewmodel is Activity or Fragment level lifecycle
        val owner: ViewModelStoreOwner =
            if (isActivityLevel) activity as AppCompatActivity else this
        ViewModelProvider(owner, factory).get(clazz)
    }

    protected var binding: ViewDataBinding? = null

    abstract fun getLayoutId(): Int

    abstract fun viewCreated(savedInstanceState: Bundle?)

    abstract fun initViewComponents()

    abstract fun initDataBinding(): View?

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewComponents()
    }

    open fun init(view: View, bundle: Bundle?) {

    }

    private var inflater: LayoutInflater? = null
    private var container: ViewGroup? = null
    private var savedInstanceState: Bundle? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.inflater = inflater
        this.container = container
        this.savedInstanceState = savedInstanceState
        return initDataBinding()
    }

    protected fun <T : ViewDataBinding> bind(onBindingDone: (T) -> Unit) {
        val b: T = DataBindingUtil.inflate(inflater!!, getLayoutId(), container, false)
        binding = b
        b.lifecycleOwner = this
        b.executePendingBindings()
        init(b.root, savedInstanceState)
        onBindingDone(b)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.initArgs(savedInstanceState)
        hideKeyboard()
        viewCreated(arguments)

        initBaseViewModel()
    }

    private fun initBaseViewModel() {
        initProgressObserver()
        initConnectionErrorObserver()
        initShowErrorObserver()
        initErrorHandling()

        viewModel.getShowToastShortField().observe(viewLifecycleOwner, EventObserver {
            activity?.let { a ->
                Lgi.toastShort(a, it)
            }
        })

        viewModel.getShowToastLongField().observe(viewLifecycleOwner, EventObserver {
            activity?.let { a ->
                Lgi.toastLong(a, it)
            }
        })
    }

    open fun initErrorHandling() {
        viewModel.openClearLogin.observe(viewLifecycleOwner, EventObserver {
            activity?.let { a ->
//                val intent = SignInActivity.newIntent(a)
//                intent.flags =
//                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                startActivity(intent)
//                finish()
            }
        })
    }

    open fun initShowErrorObserver() {
        viewModel.showError.observe(viewLifecycleOwner, EventObserver { muleError ->
            val a = activity
            if (a != null && a is BaseActivity<*>) {
                a.showError(muleError.title, muleError.body, muleError.exception, null)
            }
        })
    }

    open fun initConnectionErrorObserver() {
        viewModel.showNoInternetConnection.observe(viewLifecycleOwner, EventObserver {
            val a = activity
            if (a != null && a is BaseActivity<*>) {
                a.showConnectionError(null)
            }
        })
    }

    open fun initProgressObserver() {
        viewModel.showProgress.observe(viewLifecycleOwner, Observer { pMd ->
            if (pMd.isLoading) {
                val a = activity
                if (a != null && a is BaseActivity<*>) {
                    a.showProgress(pMd.title)
                }

            } else {
                val a = activity
                if (a != null && a is BaseActivity<*>) {
                    a.hideProgress()
                }
            }
        })
    }

    fun <T : ViewComponent<*>> getComponent(): T? {
        return getApp(activity!!)
            .componentsHolder
            .getViewComponent(this::class.java) as T?
    }

    override fun onDestroyView() {
        Lgi.p("onDestroyView !!! -> ${this::class.java}}")
        val cls = this::class.java
        activity?.run {
            getApp(this)
                .componentsHolder.releaseViewComponent(cls)
        }
        super.onDestroyView()
    }

    override fun showToast(text: String) {
        val a = activity
        if (a != null && a is BaseActivity<*>) {
            a.showToast(text)
        }
    }

    override fun showError(
        title: String?,
        body: String?,
        exception: Throwable?,
        onOk: (() -> Unit)?
    ) {
        val a = activity
        if (a != null && a is BaseActivity<*>) {
            a.showError(title, body, exception, onOk)
        }
    }

    override fun showConnectionError(onOk: (() -> Unit)?) {
        val a = activity
        if (a != null && a is BaseActivity<*>) {
            a.showConnectionError(onOk)
        }
    }

    override fun hideKeyboard() {
        val a = activity
        if (a != null && a is BaseActivity<*>) {
            a.hideKeyboard()
        }
    }

    override fun finish() {
        activity?.finish()
    }

    override fun addFragment(f: Fragment) {
        val a = activity
        if (a != null && a is BaseFragmentActivity<*>) {
            a.addFragment(f)
        }
    }

    override fun addFragmentWithBackStack(f: Fragment) {
        val a = activity
        if (a != null && a is BaseFragmentActivity<*>) {
            a.addFragmentWithBackStack(f)
        }

    }

    override fun addFragmentWithAnim(f: Fragment) {
        val a = activity
        if (a != null && a is BaseFragmentActivity<*>) {
            a.addFragmentWithAnim(f)
        }
    }

    override fun addFragmentWithAnimAndBackStack(f: Fragment) {
        val a = activity
        if (a != null && a is BaseFragmentActivity<*>) {
            a.addFragmentWithAnimAndBackStack(f)
        }
    }

    override fun addFragmentFromBottom(f: Fragment) {
        val a = activity
        if (a != null && a is BaseFragmentActivity<*>) {
            a.addFragmentFromBottom(f)
        }
    }

    override fun replaceFragment(fragment: Fragment) {
        val a = activity
        if (a != null && a is BaseFragmentActivity<*>) {
            a.replaceFragment(fragment)
        }
    }

    override fun replaceFragmentWithAnim(fragment: Fragment) {
        val a = activity
        if (a != null && a is BaseFragmentActivity<*>) {
            a.replaceFragmentWithAnim(fragment)
        }
    }

    override fun replaceFragmentWithAnimClose(fragment: Fragment) {
        val a = activity
        if (a != null && a is BaseFragmentActivity<*>) {
            a.replaceFragmentWithAnimClose(fragment)
        }
    }

    override fun addFragmentFromBottomWithBackStack(f: Fragment) {
        val a = activity
        if (a != null && a is BaseFragmentActivity<*>) {
            a.addFragmentFromBottomWithBackStack(f)
        }
    }

    override fun replaceFragmentWithBackStack(fragment: Fragment) {
        val a = activity
        if (a != null && a is BaseFragmentActivity<*>) {
            a.replaceFragmentWithBackStack(fragment)
        }
    }

    override fun replaceFragmentWithOpenSlide(fragment: Fragment) {
        val a = activity
        if (a != null && a is BaseFragmentActivity<*>) {
            a.replaceFragmentWithOpenSlide(fragment)
        }
    }

    override fun replaceFragmentWithCloseSlide(fragment: Fragment) {
        val a = activity
        if (a != null && a is BaseFragmentActivity<*>) {
            a.replaceFragmentWithCloseSlide(fragment)
        }
    }

    override fun removeFromBackStack(tag: String) {
        val a = activity
        if (a != null && a is BaseFragmentActivity<*>) {
            a.removeFromBackStack(tag)
        }
    }

    override fun popBackStack() {
        val a = activity
        if (a != null && a is BaseFragmentActivity<*>) {
            a.popBackStack()
        }
    }

    override fun isViewAttached() = isAdded && (activity != null)

    protected fun getTextFromHtml(id: Int): Spanned {
        return Html.fromHtml(getString(id))
    }

    protected fun backClick() {
        activity?.onBackPressed()
    }

    fun postDelayed(duration: Long = 100, callback: () -> Unit) {
        Handler().postDelayed(callback, duration)
    }

    private var mOnGlobalLayoutListener: ViewTreeObserver.OnGlobalLayoutListener? = null

    fun addKeyboardStateListener(
        v: View,
        onHide: (() -> Unit)? = null,
        onShow: (() -> Unit)? = null
    ) {
        mOnGlobalLayoutListener = object : ViewTreeObserver.OnGlobalLayoutListener {

            private var mRectInitial: Rect? = null
            private var mIsKeyboardOpened = false

            override fun onGlobalLayout() {
                if (mRectInitial == null) {
                    mRectInitial = Rect()
                    v.getWindowVisibleDisplayFrame(mRectInitial)
                }
                val rect = Rect()
                v.getWindowVisibleDisplayFrame(rect)
                if (!mIsKeyboardOpened && rect.height() != mRectInitial!!.height()) {
                    mIsKeyboardOpened = true
                    if (onShow != null) {
                        onShow()
                    }
                } else if (mIsKeyboardOpened && rect.height() == mRectInitial!!.height()) {
                    mIsKeyboardOpened = false
                    if (onHide != null) {
                        onHide()
                    }
                }
            }
        }
        v.viewTreeObserver.addOnGlobalLayoutListener(mOnGlobalLayoutListener)
    }

    fun removeKeyboardStateListener(v: View) {
        try {
            v.viewTreeObserver.removeOnGlobalLayoutListener(mOnGlobalLayoutListener)
        } catch (ex: Exception) {
            Lgi.err(ex)
        }
    }
}

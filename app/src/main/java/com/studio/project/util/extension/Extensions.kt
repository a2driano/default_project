package com.studio.project.util.extension

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.text.Editable
import android.text.InputFilter
import android.text.Selection
import android.text.TextWatcher
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.TranslateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.interpolator.view.animation.FastOutLinearInInterpolator
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.transition.Transition
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.google.gson.Gson
import com.studio.project.R
import com.transitionseverywhere.extra.Scale
import timber.log.Timber
import java.io.File
import java.util.*
import kotlin.math.abs


fun String?.isNullOrBlankOrEmpty(): Boolean {
    return isNullOrBlank() || isNullOrEmpty()
}

fun String.textOrNull(): String? {
    if (this.isEmpty() || this.isBlank()) {
        return null
    }
    return this
}

fun <T : Any> String.fromJson(clazz: Class<T>): T? {
    return try {
        val g = Gson()
        g.fromJson(this, clazz)
    } catch (ex: Throwable) {
        Timber.e(ex)
        null
    }
}

fun Random.chanceByPercents(percent: Int) = nextInt(100) < percent

fun Activity.statusBarHeight(): Int {
    var statusBarHeight = 0
    val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        statusBarHeight = resources.getDimensionPixelSize(resourceId)
    }
    return statusBarHeight
}

fun Int.isResultOk(): Boolean = this == Activity.RESULT_OK

fun getExtraLargeByteArray(mb: Int): Array<Byte?> {
    val bytesAmount = mb * 1024 * 1024
    val rnd = Random()
    val array = arrayOfNulls<Byte>(bytesAmount)
    for (i in 0 until bytesAmount) {
        array[i] = rnd.nextInt(250).toByte()
    }
    return array
}

fun Int.seconds(): Long {
    return (this * 1000).toLong()
}

fun Int.minutes(): Long {
    return 60.seconds() * this
}

fun Int.hours(): Long {
    return 60.minutes() * this
}

fun ImageView.setImageDrawableRes(id: Int) {
    setImageResource(id)
}

inline fun <VM : ViewModel> viewModelFactory(crossinline f: () -> VM) =
    object : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(aClass: Class<T>): T = f() as T
    }

inline fun <T : View> T.afterMeasured(crossinline callback: T.() -> Unit) {
    viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (measuredWidth > 0 && measuredHeight > 0) {
                viewTreeObserver.removeOnGlobalLayoutListener(this)
                callback()
            }
        }
    })
}

inline fun <T> LiveData<T>.reobserve(owner: LifecycleOwner, crossinline func: (T?) -> (Unit)) {
    removeObservers(owner)
    observe(owner, androidx.lifecycle.Observer<T> { t -> func(t) })
}

internal fun Context.getColorCompat(@ColorRes color: Int) = ContextCompat.getColor(this, color)

internal fun TextView.setTextColorRes(@ColorRes color: Int) =
    setTextColor(context.getColorCompat(color))

operator fun <T : Any> Array<T>.times(cnt: Int): MutableList<T> {
    val list = mutableListOf<T>()
    for (i in 0 until cnt) {
        list.addAll(this.toList())
    }
    return list.toMutableList()
}

operator fun <T : Any> MutableList<T>.times(cnt: Int): MutableList<T> {
    val list = mutableListOf<T>()
    for (i in 0 until cnt) {
        list.addAll(this)
    }
    return list
}

/**fragments*/
fun Fragment.showKeyboard(view: View) {
    val inputManager =
        context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

    inputManager?.showSoftInput(view, 0)
}

fun Fragment.hideKeyboard() {
    val inputManager =
        context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

    if (view != null) inputManager?.hideSoftInputFromWindow(view?.windowToken, 0)
}

/**fragment manager*/
fun FragmentManager.replaceWithBackStack(containerId: Int, fragment: Fragment) {
    beginTransaction()
        .replace(containerId, fragment, fragment::class.java.simpleName)
        .addToBackStack(fragment::class.java.simpleName)
        .commit()
}

fun FragmentManager.replaceWithoutBackStack(containerId: Int, fragment: Fragment) {
    beginTransaction()
        .replace(containerId, fragment, fragment::class.java.simpleName)
        .commit()
}

fun FragmentManager.addWithBackStack(containerId: Int, fragment: Fragment) {
    val tag = fragment::class.java.simpleName
    Timber.i("tag = $tag")
    beginTransaction()
        .add(containerId, fragment, fragment::class.java.simpleName)
        .addToBackStack(tag)
        .commit()
}

fun FragmentManager.addWithoutBackStack(containerId: Int, fragment: Fragment) {
    beginTransaction()
        .add(containerId, fragment, fragment::class.java.simpleName)
        .commit()
}

fun File.toUri(): Uri {
    return Uri.fromFile(this)
}

data class ParsedPath(val dir: String, val fullName: String, val fileName: String, val ext: String)

fun String.parsePath(): ParsedPath {
    val dir = this.substringBeforeLast("/")
    val fullName = this.substringAfterLast("/")
    val fileName = fullName.substringBeforeLast(".")
    val ext = fullName.substringAfterLast(".")
    return ParsedPath(dir, fullName, fileName, ext)
}

fun String.toIntSafe(): Int {
    if (this == "") {
        return 0
    }

    return if (this.contains(".")) {
        try {
            Math.round(this.toDouble()).toInt()
        } catch (ex: Throwable) {
            0
        }
    } else {
        try {
            java.lang.Integer.parseInt(this)
        } catch (ex: Throwable) {
            0
        }
    }
}

fun String.toDoubleSafe(): Double {
    if (this == "") {
        return 0.0
    }

    try {
        return java.lang.Double.parseDouble(this)
    } catch (e: Throwable) {
        return 0.0
    }
}

fun View.setBackground(@DrawableRes drawableResId: Int) {
    val sdk = android.os.Build.VERSION.SDK_INT;
    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
        this.setBackgroundDrawable(ContextCompat.getDrawable(context, drawableResId));
    } else {
        this.background = ContextCompat.getDrawable(context, drawableResId);
    }
}

fun View.setShadow(size: Float) {
    val sdk = android.os.Build.VERSION.SDK_INT
    if (sdk >= android.os.Build.VERSION_CODES.LOLLIPOP) {
        //this.elevation = LgiUtils.dpToPx(context, sizeDp).toFloat()
        this.elevation = size
    }
}


fun View.getCoordinatesOnScreen(): Pair<Int, Int>? {
    try {
        val intArray = IntArray(2)
        this.getLocationOnScreen(intArray)
        val result = Pair<Int, Int>(intArray[0], intArray[1])
        return result
    } catch (e: Throwable) {
        Timber.e(e)
    }
    return null
}

fun View.getCoordinatesOnView(): Pair<Int, Int>? {
    try {
        val intArray = IntArray(2)
        this.getLocationInWindow(intArray)
        val result = Pair<Int, Int>(intArray[0], intArray[1])
        return result
    } catch (e: Throwable) {
        Timber.e(e)
    }
    return null
}

fun Context.validationError(): String {
    return this.getString(R.string.error_validation_error)
}

fun Context.somethingWentWrong(): String {
    return this.getString(R.string.error_something_went_wrong)
}

fun Context.unexpectedError(): String {
    return this.getString(R.string.error_unexpected)
}

fun View.gone() {
    visibility = View.GONE
}

fun View.visible() {
    visibility = View.VISIBLE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.setVisibility(visible: Boolean) {
    this.visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.isVisible() = this.visibility == View.VISIBLE

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

fun View.animateClick(innerView: View) {
    val scaleDown = 0.97f
    val scaleUp = 1.03f
    val duration = 80L
    this.setOnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                v.animate().scaleX(scaleDown).scaleY(scaleDown).duration = duration
                innerView.animate().scaleX(scaleDown).scaleY(scaleDown).duration = duration
            }
            MotionEvent.ACTION_UP -> {
                v.animate().scaleX(scaleUp).scaleY(scaleUp).duration = duration
                innerView.animate().scaleX(scaleUp).scaleY(scaleUp).duration = duration
            }
        }
        false
    }
}


fun Fragment.showToast(res: Any?, duration: Int = Toast.LENGTH_SHORT) {
    val text = when (res) {
        is Int -> this.getString(res)
        is String -> res
        else -> ""
    }
    Toast.makeText(this.context, text, duration).show()
}

fun EditText.limitLength(maxLength: Int) {
    filters = arrayOf(InputFilter.LengthFilter(maxLength))
}

fun Calendar.setEndDay() {
    set(Calendar.HOUR_OF_DAY, 23)
    set(Calendar.MINUTE, 59)
    set(Calendar.SECOND, 59)
    set(Calendar.MILLISECOND, 999)
}

fun Calendar.copyDateWithoutMinutes(targetCalendar: Calendar) {
    set(Calendar.YEAR, targetCalendar.get(Calendar.YEAR))
    set(Calendar.MONTH, targetCalendar.get(Calendar.MONTH))
    set(Calendar.DAY_OF_MONTH, targetCalendar.get(Calendar.DAY_OF_MONTH))
    set(Calendar.MILLISECOND, targetCalendar.get(Calendar.MILLISECOND))
    set(Calendar.SECOND, targetCalendar.get(Calendar.SECOND))
}

fun EditText.addListener(listener: () -> Boolean) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            listener.invoke()
        }
    })
}

fun String.getLocalFile(context: Context): File {
    val name = this.substringAfterLast("/")
    val path = this.substringBeforeLast("/")

    val localPath = path.split("/")
    Timber.i("--- getLocalFile path localPath array: ${localPath}")
    val tempPath = StringBuilder()
    for ((i, leftPath) in localPath.withIndex()) {
        if (i == localPath.size - 1)
            break
        tempPath.append(leftPath)
        tempPath.append("/")

        val dirFolder = File(context.externalCacheDir, tempPath.toString())
//            val dirFolder = File(context.cacheDir, tempPath.toString())
        if (!dirFolder.exists()) {
            dirFolder.mkdir()
        }
    }

    val dirFile = File(context.externalCacheDir, path)
//        val dirFile = File(context.cacheDir, path)
    if (!dirFile.exists()) {
        dirFile.mkdir()
    }

    return File(dirFile, name)
}

fun String.getUniqueInt(): Int {
    val uuid = UUID.nameUUIDFromBytes((this).toByteArray())
    return abs(uuid.mostSignificantBits.toInt())
}

/** Return Pence from String */
fun String.getPence(): Int {
    // *100 because need Pence from Pound
    val floatValue = (this.toFloatOrNull() ?: 0.00f) * 100
    return floatValue.toInt()
}

fun <T : ViewModel> T.createFactory(): ViewModelProvider.Factory {
    val viewModel = this
    return object : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModel as T
    }
}

fun Int.getProgress(): Int {
    val stepFinal = 7
    val maxProgress = 100
    val stepCur = this

    return maxProgress * stepCur / stepFinal
}

fun ImageView.glide(url: String) {
    Glide.with(this.context)
        .load(url)
        .centerCrop()
//        .placeholder(R.drawable.ic_camera)
        .into(this)
}

fun ImageView.glideCircle(url: String) {
    Glide.with(this)
        .load(url)
        .diskCacheStrategy(DiskCacheStrategy.ALL)
        .apply(RequestOptions.centerCropTransform())
        .apply(RequestOptions.circleCropTransform())
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)
}


fun EditText.addSuffix(suffix: String) {
    val editText = this
    val formattedSuffix = " $suffix"
    var text = ""
    var isSuffixModified = false

    val setCursorPosition: () -> Unit =
        { Selection.setSelection(editableText, editableText.length - formattedSuffix.length) }

    val setEditText: () -> Unit = {
        editText.setText(text)
        setCursorPosition()
    }

    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            val newText = editable.toString()

            if (isSuffixModified) {
                // user tried to modify suffix
                isSuffixModified = false
                setEditText()
            } else if (text.isNotEmpty() && newText.length < text.length && !newText.contains(
                    formattedSuffix
                )
            ) {
                // user tried to delete suffix
                setEditText()
            } else if (!newText.contains(formattedSuffix)) {
                // new input, add suffix
                text = "$newText$formattedSuffix"
                setEditText()
            } else {
                text = newText
            }
        }

        override fun beforeTextChanged(
            charSequence: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {
            charSequence?.let {
                val textLengthWithoutSuffix = it.length - formattedSuffix.length
                if (it.isNotEmpty() && start > textLengthWithoutSuffix) {
                    isSuffixModified = true
                }
            }
        }

        override fun onTextChanged(
            charSequence: CharSequence?,
            start: Int,
            before: Int,
            count: Int
        ) {
        }
    })
}

//fun EditText.addFocusViewListener(action: (EditText, Int) -> Unit) {
//    val margin = 140f
//    var yPos = 0
//
//    Handler().postDelayed({
//        yPos = (this.getCoordinatesOnView()?.second ?: 0) - dpToPx(context, margin)
//    }, 100)
//
//    setOnFocusChangeListener { view, hasFocus ->
//        if (hasFocus) {
//            action(this, yPos)
//        }
//    }
//}

fun EditText.allowOnlyAlphaNumericCharacters() {
    filters = filters.plus(
        listOf(
            InputFilter { s, _, _, _, _, _ ->
                s.replace(Regex("[^A-Z0-9 ]"), "")
            },
            InputFilter.AllCaps()
        )
    )
}

fun EditText.addFocusListener(action: (View) -> Unit) {
    setOnFocusChangeListener { view, hasFocus ->
        //        if (hasFocus && !isFocused) {
        if (hasFocus) {
            action(view)
        }
    }
}

fun ViewGroup.animateFadeAndZoom(
    scale: Float = 0.8f,
    duration: Long = 300,
    onStart: (() -> Unit)? = null,
    onEnd: (() -> Unit)? = null
) {
    val set = androidx.transition.TransitionSet()
        .addTransition(Scale(scale))
        .addTransition(androidx.transition.Fade())
        .setDuration(duration)
        .setInterpolator(FastOutLinearInInterpolator())
        .addListener(object : Transition.TransitionListener {
            override fun onTransitionEnd(transition: Transition) {
                if (onEnd != null) {
                    onEnd()
                }
            }

            override fun onTransitionResume(transition: Transition) {

            }

            override fun onTransitionPause(transition: Transition) {

            }

            override fun onTransitionCancel(transition: Transition) {

            }

            override fun onTransitionStart(transition: Transition) {
                if (onStart != null) {
                    onStart()
                }
            }
        })

    TransitionManager.beginDelayedTransition(this, set)
}

fun ViewGroup.stopAnimations() {
    TransitionManager.endTransitions(this)
}

fun ViewGroup.slideFromBottom(duration: Long = 300L) {
    this.show()
    val animate = TranslateAnimation(
        0f,
        0f,
        this.height.toFloat(),
        0f
    )
    animate.duration = duration;
    animate.fillAfter = true;
    this.startAnimation(animate);
}

fun View.fadeIn(onStart: (() -> Unit)? = null, onEnd: (() -> Unit)? = null) {

    if (this.visibility != View.VISIBLE) {
        val slide: Animation = AnimationUtils.loadAnimation(context, R.anim.fade_in)

        slide.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                if (onStart != null) {
                    onStart()
                }
            }

            override fun onAnimationEnd(animation: Animation) {
                if (onEnd != null) {
                    onEnd()
                }
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
        this.visibility = View.VISIBLE
        this.startAnimation(slide)
    }
}

fun View.fadeOut(onStart: (() -> Unit)? = null, onEnd: (() -> Unit)? = null) {

    if (this.visibility == View.VISIBLE) {
        val slide: Animation = AnimationUtils.loadAnimation(context, R.anim.fade_out)

        slide.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                if (onStart != null) {
                    onStart()
                }
            }

            override fun onAnimationEnd(animation: Animation) {
                if (onEnd != null) {
                    onEnd()
                }
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
        this.visibility = View.GONE
        this.startAnimation(slide)
    }
}

fun View.translateInFromLeft(
    animDuration: Long = 100,
    onStart: (() -> Unit)? = null,
    onEnd: (() -> Unit)? = null
) {

    if (this.visibility != View.VISIBLE) {
        val slide: Animation = AnimationUtils.loadAnimation(context, R.anim.enter_from_left)
            .apply { duration = animDuration }

        slide.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                if (onStart != null) {
                    onStart()
                }
            }

            override fun onAnimationEnd(animation: Animation) {
                if (onEnd != null) {
                    onEnd()
                }
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
        this.visibility = View.VISIBLE
        this.startAnimation(slide)
    }
}

fun View.translateOutToLeft(
    animDuration: Long = 100,
    onStart: (() -> Unit)? = null,
    onEnd: (() -> Unit)? = null
) {

    if (this.visibility == View.VISIBLE) {
        val slide: Animation = AnimationUtils.loadAnimation(context, R.anim.exit_to_left)
            .apply { duration = animDuration }

        slide.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                if (onStart != null) {
                    onStart()
                }
            }

            override fun onAnimationEnd(animation: Animation) {
                if (onEnd != null) {
                    onEnd()
                }
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
        this.visibility = View.GONE
        this.startAnimation(slide)
    }
}

fun View.translateInFromRight(
    animDuration: Long = 100,
    onStart: (() -> Unit)? = null,
    onEnd: (() -> Unit)? = null
) {

    if (this.visibility != View.VISIBLE) {
        val slide: Animation = AnimationUtils.loadAnimation(context, R.anim.enter_from_right)
            .apply { duration = animDuration }

        slide.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                if (onStart != null) {
                    onStart()
                }
            }

            override fun onAnimationEnd(animation: Animation) {
                if (onEnd != null) {
                    onEnd()
                }
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
        this.visibility = View.VISIBLE
        this.startAnimation(slide)
    }
}

fun View.translateOutToRight(
    animDuration: Long = 100,
    onStart: (() -> Unit)? = null,
    onEnd: (() -> Unit)? = null
) {

    if (this.visibility == View.VISIBLE) {
        val slide: Animation = AnimationUtils.loadAnimation(context, R.anim.exit_to_right)
            .apply { duration = animDuration }

        slide.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                if (onStart != null) {
                    onStart()
                }
            }

            override fun onAnimationEnd(animation: Animation) {
                if (onEnd != null) {
                    onEnd()
                }
            }

            override fun onAnimationRepeat(animation: Animation) {

            }
        })
        this.visibility = View.GONE
        this.startAnimation(slide)
    }
}
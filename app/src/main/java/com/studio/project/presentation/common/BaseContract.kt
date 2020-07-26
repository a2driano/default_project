package com.studio.project.presentation.common

import android.app.Activity
import androidx.fragment.app.Fragment

/**
 * Created by Andrew on 25.04.2020
 */
interface BaseContract {

    interface BaseRepo {

    }

    interface MvpView {
        fun showToast(text: String)
        fun showError(
            title: String? = null,
            body: String? = null,
            exception: Throwable? = null,
            onOk: (() -> Unit)? = null
        )

        fun showConnectionError(onOk: (() -> Unit)? = null)
        fun hideKeyboard()
        fun finish()
    }

    interface ActivityView :
        MvpView {
        fun getActivity(): Activity
        fun showProgress(text: String?)
        fun showProgress(strId: Int)
        fun hideProgress()
    }

    interface FragmentControl {
        fun addFragmentWithBackStack(f: Fragment)
        fun addFragment(f: Fragment)

        fun addFragmentWithAnim(f: Fragment)
        fun addFragmentWithAnimAndBackStack(f: Fragment)

        fun addFragmentFromBottom(f: Fragment)
        fun addFragmentFromBottomWithBackStack(f: Fragment)

        fun replaceFragment(fragment: Fragment)
        fun replaceFragmentWithAnim(fragment: Fragment)
        fun replaceFragmentWithAnimClose(fragment: Fragment)
        fun replaceFragmentWithBackStack(fragment: Fragment)

        fun replaceFragmentWithOpenSlide(fragment: Fragment)
        fun replaceFragmentWithCloseSlide(fragment: Fragment)

        fun removeFromBackStack(tag: String)
        fun popBackStack()
    }

    interface FragmentActivityView :
        ActivityView,
        FragmentControl {
        fun clearBackStackFragments()
        fun getCurrentFragment(): Fragment?
        fun getFragmentByTag(tag: String): Fragment?
    }

    interface FragmentView : MvpView,
        FragmentControl {
        //fun initDependencies()
        fun isViewAttached(): Boolean
    }
}
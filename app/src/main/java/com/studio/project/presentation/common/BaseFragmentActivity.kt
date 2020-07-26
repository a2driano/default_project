package com.studio.project.presentation.common

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.github.torindev.lgi_android.Lgi
import com.studio.project.R

/**
 * Created by Andrew on 25.04.2020
 */
abstract class BaseFragmentActivity<T : BaseViewModel>(clazz: Class<T>) :
    BaseActivity<T>(clazz), BaseContract.FragmentActivityView {

    override fun getLayoutId(): Int = R.layout.activity_fragment_base

    open fun getContainerId(): Int = R.id.fragment_container

    abstract fun getStartFragment(): Fragment?

    open fun onFragmentSetup() {

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val fm = supportFragmentManager
        var f = fm.findFragmentById(getContainerId())
        if (f == null) {
            f = getStartFragment()

            if (f == null) {
                return
            }

            fm.beginTransaction()
                .add(getContainerId(), f)
                .commit()

            onFragmentSetup()
        }
    }

    override fun getCurrentFragment() = supportFragmentManager.findFragmentById(getContainerId())

    override fun addFragment(f: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .add(getContainerId(), f, f::class.java.simpleName)
            .commit()
    }

    override fun addFragmentWithBackStack(f: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .add(getContainerId(), f, f::class.java.simpleName)
            .addToBackStack(f::class.java.simpleName)
            .commit()
    }

    override fun addFragmentWithAnim(f: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .add(getContainerId(), f, f::class.java.simpleName)
            .commit()
    }

    override fun addFragmentWithAnimAndBackStack(f: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .add(getContainerId(), f, f::class.java.simpleName)
            .addToBackStack(f::class.java.simpleName)
            .commit()
    }

    override fun addFragmentFromBottom(f: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_up_fragment,
                R.anim.slide_out_up_fragment,
                R.anim.slide_in_down_fragment,
                R.anim.slide_out_down_fragment
            )
            .add(getContainerId(), f, f::class.java.simpleName)
            .commit()
    }

    override fun addFragmentFromBottomWithBackStack(f: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.slide_in_up_fragment,
                R.anim.slide_out_up_fragment,
                R.anim.slide_in_down_fragment,
                R.anim.slide_out_down_fragment
            )
            .add(getContainerId(), f, f::class.java.simpleName)
            .addToBackStack(f::class.java.simpleName)
            .commit()
    }

    override fun replaceFragmentWithBackStack(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(getContainerId(), fragment, fragment::class.java.simpleName)
            .commit()
    }

    override fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .replace(getContainerId(), fragment, fragment::class.java.simpleName)
            .commit()
    }

    override fun replaceFragmentWithAnim(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .replace(getContainerId(), fragment, fragment::class.java.simpleName)
            .commit()
    }

    override fun replaceFragmentWithAnimClose(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .replace(getContainerId(), fragment, fragment::class.java.simpleName)
            .commit()
    }

    override fun replaceFragmentWithOpenSlide(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            //.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
            .setCustomAnimations(
                R.anim.enter_from_right,
                R.anim.exit_to_left,
                R.anim.enter_from_left,
                R.anim.exit_to_right
            )
            .replace(getContainerId(), fragment, fragment::class.java.simpleName)
            .commit()
    }

    override fun replaceFragmentWithCloseSlide(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            //.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
            .setCustomAnimations(
                R.anim.enter_from_left,
                R.anim.exit_to_right,
                R.anim.enter_from_right,
                R.anim.exit_to_left
            )
            .replace(getContainerId(), fragment, fragment::class.java.simpleName)
            .commit()
    }

    override fun removeFromBackStack(tag: String) {
        val fm = supportFragmentManager
        fm.popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    override fun popBackStack() {
        supportFragmentManager.popBackStack()
    }

    override fun clearBackStackFragments() {
        val fm = supportFragmentManager
        for (i in 0 until fm.backStackEntryCount) {
            fm.popBackStack()
        }
    }

    override fun getFragmentByTag(tag: String): Fragment? {
        try {
            return supportFragmentManager.findFragmentByTag(tag)
        } catch (ex: Throwable) {
            Lgi.err(ex)
            return null
        }
    }
}
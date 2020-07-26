package com.studio.project.presentation.common.example.activity_fragment

import android.os.Bundle
import com.studio.project.databinding.ActivityFragmentBaseBinding
import com.studio.project.presentation.common.BaseFragmentActivity
import com.studio.project.presentation.common.example.activity_fragment.di.ExampleActivityFragmentComponent
import com.studio.project.presentation.common.example.fragment.ExampleFragment

class ExampleActivityFragment :
    BaseFragmentActivity<ExampleActivityFragmentViewModel>(ExampleActivityFragmentViewModel::class.java) {

    override fun getStartFragment() = ExampleFragment.newInstance()

    override fun initViewComponents() {
        getComponent<ExampleActivityFragmentComponent>()?.inject(this)
    }

    override fun initDataBinding() {
        bind<ActivityFragmentBaseBinding> { it.model = viewModel }
    }

    override fun initUI(bundle: Bundle?) {

    }

}

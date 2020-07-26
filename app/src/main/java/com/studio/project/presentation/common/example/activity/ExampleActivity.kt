package com.studio.project.presentation.common.example.activity

import android.os.Bundle
import com.studio.project.R
import com.studio.project.databinding.ActivityExampleBinding
import com.studio.project.presentation.common.BaseActivity
import com.studio.project.presentation.common.example.activity.di.MainActivityComponent

class ExampleActivity : BaseActivity<ExampleViewModel>(
    ExampleViewModel::class.java
) {

    override fun getLayoutId() = R.layout.activity_example

    override fun initViewComponents() {
        getComponent<MainActivityComponent>()?.inject(this)
    }

    override fun initDataBinding() {
        bind<ActivityExampleBinding> { it.model = viewModel }
    }

    override fun initUI(bundle: Bundle?) {

    }
}

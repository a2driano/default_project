package com.studio.project.presentation.common.example.fragment

import android.os.Bundle
import android.view.View
import com.studio.project.R
import com.studio.project.databinding.FragmentExampleBinding
import com.studio.project.presentation.common.BaseFragment
import com.studio.project.presentation.common.example.fragment.di.ExampleFragmentComponent

/**
 * Created by Andrew on 25.04.2020
 */
class ExampleFragment :
    BaseFragment<ExampleFragmentViewModel>(ExampleFragmentViewModel::class.java) {

    override fun getLayoutId() = R.layout.fragment_example

    override fun initViewComponents() {
        getComponent<ExampleFragmentComponent>()?.inject(this)
    }

    override fun initDataBinding(): View? {
        bind<FragmentExampleBinding> { it.model = viewModel }
        return binding?.root
    }

    override fun viewCreated(savedInstanceState: Bundle?) {

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            ExampleFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}

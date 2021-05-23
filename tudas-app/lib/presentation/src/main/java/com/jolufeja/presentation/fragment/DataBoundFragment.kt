package com.jolufeja.presentation.fragment


import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.reflect.KClass

abstract class DataBoundFragment<VM : ViewModel, B : ViewDataBinding>(
    @LayoutRes layoutId: Int,
    viewModelClass: KClass<VM>,
    private val viewModelPropertyId: Int
) : LayoutFragment(layoutId) {

    protected val viewModel: VM by viewModel(clazz = viewModelClass)

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = createBinding(view)
        binding.lifecycleOwner = this
        binding.setVariable(viewModelPropertyId, viewModel)
        binding.setBindingVariables()

        onViewAndBindingCreated(view, binding, savedInstanceState)
    }

    protected abstract fun createBinding(view: View): B

    protected open fun B.setBindingVariables() {}

    protected open fun onViewAndBindingCreated(view: View, binding: B, savedInstanceState: Bundle?) {}

}
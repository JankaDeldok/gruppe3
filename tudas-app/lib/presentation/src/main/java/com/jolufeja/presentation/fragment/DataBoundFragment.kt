package com.jolufeja.presentation.fragment


import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.reflect.KClass

/**
 * Data bound fragment.
 * See also [View binding](https://developer.android.com/topic/libraries/view-binding)
 *
 * @param VM
 * @param B
 * @property viewModelPropertyId
 * @constructor
 *
 * @param layoutId
 * @param viewModelClass
 */
abstract class DataBoundFragment<VM : ViewModel, B : ViewDataBinding>(
    @LayoutRes layoutId: Int,
    viewModelClass: KClass<VM>,
    private val viewModelPropertyId: Int
) : Fragment(layoutId) {

    private val viewModel: VM by viewModel(clazz = viewModelClass)

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
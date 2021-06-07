package com.jolufeja.tudas


import android.view.View
import com.jolufeja.presentation.fragment.DataBoundFragment
import com.jolufeja.tudas.databinding.FragmentRegistrationBinding


class RegistrationFragment : DataBoundFragment<RegistrationViewModel, FragmentRegistrationBinding>(
    R.layout.fragment_registration,
    RegistrationViewModel::class,
    BR.registrationViewModel
) {
    override fun createBinding(view: View) = FragmentRegistrationBinding.bind(view)
}

package com.jolufeja.tudas

import android.view.View
import com.jolufeja.presentation.fragment.DataBoundFragment
import com.jolufeja.tudas.databinding.FragmentLoginBinding

class LoginFragment : DataBoundFragment<LoginViewModel, FragmentLoginBinding>(
    R.layout.fragment_login,
    LoginViewModel::class,
    BR.loginViewModel
) {
    override fun createBinding(view: View) = FragmentLoginBinding.bind(view)
}

package com.jolufeja.tudas

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import arrow.core.computations.either
import com.jolufeja.httpclient.error.CommonErrors
import com.jolufeja.presentation.fragment.DataBoundFragment
import com.jolufeja.tudas.databinding.FragmentLoginBinding
import com.jolufeja.tudas.databinding.FragmentRegistrationBinding
import com.jolufeja.tudas.service.user.UserService
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class LoginFragment(
    private val userService: UserService
) : DataBoundFragment<LoginViewModel, FragmentLoginBinding>(
    R.layout.fragment_login,
    LoginViewModel::class,
    BR.loginViewModel
) {
    override fun createBinding(view: View) = FragmentLoginBinding.bind(view)

    override fun onViewAndBindingCreated(
        view: View,
        binding: FragmentLoginBinding,
        savedInstanceState: Bundle?
    ) {
        lifecycleScope.launch {
            viewModel.hasLoggedIn.receiveAsFlow().collect {
                updatePoints()
            }
        }
    }

    private suspend fun updatePoints() {
        either<CommonErrors, Unit> {
            val currentPoints = userService.getPointsOfCurrentUser().bind()
            (activity as MainActivity).statsChannel.trySend(currentPoints)
        }.fold(
            ifLeft = { Log.d("LoginFragment", "$it")},
            ifRight = {}
        )

    }
}

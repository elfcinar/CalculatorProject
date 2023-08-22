package com.example.calculatorproject.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculatorproject.AppDatabase
import com.example.calculatorproject.data.repository.LoginRepository
import com.example.calculatorproject.data.state.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository):ViewModel() {

    private val _loginState: MutableStateFlow<LoginState> = MutableStateFlow(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    fun login(email: String, password: String, rememberMe:Boolean) {
        viewModelScope.launch {
            if(!email.isNullOrEmpty() && !password.isNullOrEmpty()) {
                runCatching {
                    _loginState.value = LoginState.Loading
                    loginRepository.login(email, password, rememberMe)?.let {
                        _loginState.value = LoginState.Result(it,rememberMe)
                    } ?: kotlin.run {
                        //error
                    }
                }.onFailure {
                    _loginState.value = LoginState.Error(it)
                }
            }else{
                       //error
            }
        }
    }


}
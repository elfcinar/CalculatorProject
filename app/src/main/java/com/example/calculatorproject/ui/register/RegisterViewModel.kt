package com.example.calculatorproject.ui.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculatorproject.data.entity.User
import com.example.calculatorproject.data.repository.RegisterRepository
import com.example.calculatorproject.data.state.RegisterMessageState
import com.example.calculatorproject.data.state.RegisterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerRepository: RegisterRepository
):ViewModel() {

    private val _registerState: MutableStateFlow<RegisterState> = MutableStateFlow(RegisterState.Idle)
    val registerState: StateFlow<RegisterState> = _registerState

    private val _message: MutableSharedFlow<RegisterMessageState> = MutableSharedFlow()
    val message: SharedFlow<RegisterMessageState> = _message

    fun insert(name:String, surname:String, email:String, password:String, confirm:String) {
        viewModelScope.launch(Dispatchers.IO) {
            var result:Long = -1
            if(!email.isNullOrEmpty() && !password.isNullOrEmpty() && !name.isNullOrEmpty() && !surname.isNullOrEmpty()&& !confirm.isNullOrEmpty()  ){
                if(password.equals(confirm)){
                    val user = User(name = name, surname = surname, email = email, password = password)

                    kotlin.runCatching {
                        _registerState.emit(RegisterState.Loading)
                        result = registerRepository.register(user)
                        if(result > -1){
                            _message.emit(RegisterMessageState.Success)
                        }else{
                            _message.emit(RegisterMessageState.UserAlreadyExists)
                        }
                    }.onSuccess {
                        _registerState.value = RegisterState.Success
                    }.onFailure {
                        _registerState.value = RegisterState.Error(it)
                    }

                }else{
                    _message.emit(RegisterMessageState.PasswordsNotEquals)
                }
            }else{
                _message.emit(RegisterMessageState.Empty)
            }
        }
    }
}
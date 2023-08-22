package com.example.calculatorproject.data.state

import com.example.calculatorproject.data.entity.User


sealed class LoginState {

    object Idle:LoginState()
    object Loading:LoginState()
    class Result(val user: User, val rememberMe:Boolean):LoginState()
    class Error(val throwable: Throwable):LoginState()

}
package com.example.calculatorproject.data.state

sealed class LoginMessageState{
    object Idle:LoginMessageState()
    object Empty:LoginMessageState()
    object UserNotFound:LoginMessageState()
    object InformationWrong:LoginMessageState()
}

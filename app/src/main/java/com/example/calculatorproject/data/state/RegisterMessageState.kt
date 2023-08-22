package com.example.calculatorproject.data.state

sealed class RegisterMessageState{
    object Idle:RegisterMessageState()
    object UserAlreadyExists:RegisterMessageState()
    object Success:RegisterMessageState()
    object Empty:RegisterMessageState()
    object PasswordsNotEquals:RegisterMessageState()
}

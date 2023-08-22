package com.example.calculatorproject.data.state

sealed class RandomTransactionState{
    object Idle:RandomTransactionState()
    class Success(val firstNumber:Int, val secondNumber:Int, val operator:String):RandomTransactionState()
    class Error(val throwable: Throwable):RandomTransactionState()
}

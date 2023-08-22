package com.example.calculatorproject.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculatorproject.data.state.RandomTransactionState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel:ViewModel() {
    private var score = 0
    private var resultProcess = 0.0f

    private val _scoreState:MutableSharedFlow<Int> = MutableSharedFlow()
    val scoreState:SharedFlow<Int> = _scoreState

    private val _userAnswer:MutableSharedFlow<Boolean> = MutableSharedFlow()
    val userAnswer:SharedFlow<Boolean> = _userAnswer

    private val _randomTransactionState:MutableStateFlow<RandomTransactionState> = MutableStateFlow(RandomTransactionState.Idle)
    val randomTransactionState:StateFlow<RandomTransactionState> = _randomTransactionState

    fun fetchRandomTransaction(){
        viewModelScope.launch {
            runCatching {
                val firstNumber = generateRandomNumber()
                var secondNumber = generateRandomNumber()
                val operator = generateRandomOperator()
                if(operator == "/" && secondNumber == 0){
                    while (secondNumber == 0){
                        secondNumber = generateRandomNumber()
                    }
                }
                calculateTransaction(firstNumber.toFloat(),secondNumber.toFloat(),operator)
                if(secondNumber > firstNumber) _randomTransactionState.value = RandomTransactionState.Success(secondNumber,firstNumber,operator)
                else _randomTransactionState.value = RandomTransactionState.Success(firstNumber,secondNumber,operator)
            }.onFailure {
                _randomTransactionState.value = RandomTransactionState.Error(it)
            }
        }
    }

    fun generateRandomNumber(): Int{
        return (0..50).random()
    }

    fun generateRandomOperator():String {
        return Operator.values().toList().shuffled().first().operator
    }

    fun calculateTransaction(firstNumber:Float, secondNumber:Float, operator: String){
        when(operator){
            "+" -> {
                resultProcess = firstNumber + secondNumber
            }
            "-" -> {
                if(secondNumber > firstNumber) resultProcess = secondNumber - firstNumber
                else resultProcess = firstNumber - secondNumber
            }
            "*" -> {
                resultProcess = firstNumber * secondNumber
            }
            "/" -> {
                resultProcess = firstNumber / secondNumber
            }
        }
    }

    fun checkUserAnswer(answer:String){
        viewModelScope.launch {
            if(answer.toFloat() == resultProcess){
                score += 5
                _userAnswer.emit(true)
                _scoreState.emit(score)
            } else {
                if(score > 0) score--
                _userAnswer.emit(false)
                _scoreState.emit(score)
            }
        }
    }

    fun gameOver() {
        viewModelScope.launch {
            score = 0
            _scoreState.emit(score)
        }
    }
}

enum class Operator(val operator:String){
    Addition("+"),
    Extraction("-"),
    Division("/"),
    Multiplication("*")
}
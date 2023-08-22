package com.example.calculatorproject.data.repository

import com.example.calculatorproject.data.entity.User


interface LoginRepository {
    suspend fun login(email: String, password: String): User?
}
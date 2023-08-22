package com.example.calculatorproject.data.repository

import com.example.calculatorproject.data.entity.User

interface RegisterRepository {
    suspend fun register(user:User): Long

}
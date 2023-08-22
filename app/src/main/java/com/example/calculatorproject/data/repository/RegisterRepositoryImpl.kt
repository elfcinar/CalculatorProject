package com.example.calculatorproject.data.repository

import com.example.calculatorproject.data.dao.UserDao
import com.example.calculatorproject.data.entity.User
import javax.inject.Inject

class RegisterRepositoryImpl @Inject constructor(private val userDao: UserDao):RegisterRepository{
    override suspend fun register(user: User): Long = userDao.insert(user)?.let {
        return it
    }?: run{
        return -2
    }




}
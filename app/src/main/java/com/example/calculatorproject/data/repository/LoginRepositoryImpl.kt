package com.example.calculatorproject.data.repository

import com.example.calculatorproject.data.dao.UserDao
import com.example.calculatorproject.data.entity.User
import javax.inject.Inject

class LoginRepositoryImpl @Inject constructor(private val userDao: UserDao) : LoginRepository {

    override suspend fun login(email: String, password: String, rememberMe:Boolean): User? {
        userDao.getUser(email, password)?.let {
            return it
        } ?: run {
            return null
        }
    }
}
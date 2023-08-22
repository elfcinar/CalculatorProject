package com.example.calculatorproject

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.calculatorproject.data.dao.UserDao
import com.example.calculatorproject.data.entity.User


@Database(
    entities = [User::class], version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}

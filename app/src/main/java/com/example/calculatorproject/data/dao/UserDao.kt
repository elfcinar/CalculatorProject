package com.example.calculatorproject.data.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.calculatorproject.data.entity.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User):Long

    @Query("SELECT * FROM user WHERE email = :email AND password = :password")
    suspend fun getUser(email: String, password: String): User?


    @Query("SELECT * FROM User WHERE id = :id")
    suspend fun getUserById(id: Int): User?

  @Update
    suspend fun update(user: User)
}
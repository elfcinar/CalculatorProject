package com.example.calculatorproject.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val email: String?,
    val password: String?,
    val name:String? =null,
    val surname:String?=null
)
package com.example.library.domain

import com.example.library.data.User

interface UserRepository {
    suspend fun createUser(email:String, password: String): Result<Unit>
    suspend fun saveUser(user:User):Result<Unit>
}
package com.example.library.domain

import com.example.library.data.User
import com.google.firebase.auth.FirebaseUser

interface UserRepository {
    suspend fun createUser(email:String, password: String): Result<Unit>
    suspend fun signInUser(email:String, password: String): Result<FirebaseUser?>
    suspend fun signOutUser():Result<Unit>
    suspend fun saveUser(user:User):Result<Unit>
}
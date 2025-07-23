package com.example.library.domain

import com.example.library.data.User
import com.google.firebase.auth.FirebaseUser

interface UserRepository {
    suspend fun createUser(email:String, password: String): Result<FirebaseUser?>
    suspend fun deleteUserAccount(user:FirebaseUser?=null):Result<Unit>
    suspend fun getUser(uid:String):Result<User>
    suspend fun updateUser(data: Map<String, Any>):Result<Unit>
    suspend fun deleteUserData(user:FirebaseUser):Result<User?>
    suspend fun saveUser(user:User):Result<Unit>
    suspend fun signInUser(email:String, password: String): Result<FirebaseUser?>
    suspend fun signOutUser():Result<Unit>
    suspend fun reAuthenticateUser(password: String): Result<FirebaseUser>
    suspend fun sendVerificationEmail(user:FirebaseUser?=null):Result<Unit>
    suspend fun isEmailVerified ():Boolean
    suspend fun updatePassword(password:String):Result<Unit>
    suspend fun resetPassword(email: String):Result<Unit>
}
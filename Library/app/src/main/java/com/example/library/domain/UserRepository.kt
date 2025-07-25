package com.example.library.domain

import com.example.library.data.ExternalUser
import com.example.library.data.User

interface UserRepository {
    suspend fun createUser(email:String, password: String): Result<ExternalUser?>
    suspend fun deleteUserAccount(user:ExternalUser?=null):Result<Unit>
    suspend fun getUser(uid:String):Result<User>
    suspend fun updateUser(data: Map<String, Any>):Result<Unit>
    suspend fun deleteUserData(uid:String):Result<User?>
    suspend fun saveUser(user:User):Result<Unit>
    suspend fun signInUser(email:String, password: String): Result<ExternalUser?>
    suspend fun signOutUser():Result<Unit>
    suspend fun reAuthenticateUser(password: String): Result<ExternalUser>
    suspend fun sendVerificationEmail(user:ExternalUser?=null):Result<Unit>
    suspend fun isEmailVerified ():Boolean
    suspend fun updatePassword(password:String):Result<Unit>
    suspend fun resetPassword(email: String):Result<Unit>
}
package com.example.library.domain

import com.example.library.data.entity.User
import kotlinx.coroutines.flow.Flow

interface UserService {
    val userPreferences: Flow<User>
    val logInPreferences:Flow<Boolean>
    suspend fun register(user: User, password:String)
    suspend fun unregister(password: String)
    suspend fun changeUserInfo(data: Map<String, Any>)
    suspend fun signIn(email:String, password: String)
    suspend fun signOut()
    suspend fun sendVerificationEmail(user: ExternalUser?)
    suspend fun isEmailVerified():Boolean
    suspend fun verifyCurrentPassword(password: String)
    suspend fun changePassword(password: String)
    suspend fun findPassword(email: String)
    suspend fun updateLogInState(b:Boolean)
}
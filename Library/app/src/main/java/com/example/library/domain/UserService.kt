package com.example.library.domain

import com.example.library.data.ExternalUser
import com.example.library.data.User

interface UserService {
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
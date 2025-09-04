package com.example.library.domain

import com.example.library.data.entity.User
import kotlinx.coroutines.flow.Flow

interface SessionManager {
    val userPreferences: Flow<User>
    val logInPreferences:Flow<Boolean>
    suspend fun saveUserData(user: User)
    suspend fun removeUserData()
    suspend fun saveLogInState(isLogIn:Boolean)
    suspend fun removeLogInState()
}
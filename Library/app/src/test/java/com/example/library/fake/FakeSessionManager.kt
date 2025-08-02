package com.example.library.fake

import com.example.library.data.SessionManager
import com.example.library.data.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

class FakeSessionManager:SessionManager {
    override val userPreferences: Flow<User> = emptyFlow()
    override val logInPreferences: Flow<Boolean> = emptyFlow()

    override suspend fun saveUserData(user: User) {
    }
    override suspend fun removeUserData() {
    }
    override suspend fun saveLogInState(isLogIn: Boolean) {
    }
    override suspend fun removeLogInState() {
    }
}
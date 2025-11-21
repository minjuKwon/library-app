package com.example.library.fake

import com.example.library.domain.SessionManager
import com.example.library.data.entity.User
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf

class FakeSessionManager: SessionManager {
    override val userPreferences: Flow<User> = flowOf(User())
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
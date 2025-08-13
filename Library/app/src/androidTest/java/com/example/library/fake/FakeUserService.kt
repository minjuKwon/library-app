package com.example.library.fake

import com.example.library.data.ExternalUser
import com.example.library.data.User
import com.example.library.domain.UserService
import kotlinx.coroutines.flow.Flow

class FakeUserService:UserService {

    private val fakeSessionManager = FakeSessionManager()

    override val userPreferences: Flow<User> = fakeSessionManager.userPreferences
    override val logInPreferences: Flow<Boolean> = fakeSessionManager.logInPreferences

    var isThrowException= false

    override suspend fun register(user: User, password: String) {
        when{
            isThrowException ->throw Exception("fail")
            else ->{}
        }
    }

    override suspend fun unregister(password: String) {
        when{
            isThrowException ->throw Exception("fail")
            else ->{}
        }
    }

    override suspend fun changeUserInfo(data: Map<String, Any>) {
        when{
            isThrowException ->throw Exception("fail")
            else ->{}
        }
    }

    override suspend fun signIn(email: String, password: String) {
        when{
            isThrowException  ->throw Exception("fail")
            else ->{}
        }
    }

    override suspend fun signOut() {
        when{
            isThrowException  ->throw Exception("fail")
            else ->{}
        }
    }

    override suspend fun sendVerificationEmail(user: ExternalUser?) {
        when{
            isThrowException ->throw Exception("fail")
            else ->{}
        }
    }

    override suspend fun isEmailVerified(): Boolean {
        return true
    }

    override suspend fun verifyCurrentPassword(password: String) {
        when{
            isThrowException ->throw Exception("fail")
            else ->{}
        }
    }

    override suspend fun changePassword(password: String) {
        when{
            isThrowException ->throw Exception("fail")
            else ->{}
        }
    }

    override suspend fun findPassword(email: String) {
        when{
            isThrowException ->throw Exception("fail")
            else ->{}
        }
    }

    override suspend fun updateLogInState(b: Boolean) {
        fakeSessionManager.saveLogInState(b)
    }

}
package com.example.library.service

import com.example.library.data.User
import com.example.library.domain.UserRepository
import com.google.firebase.auth.FirebaseUser
import javax.inject.Inject

class FirebaseUserService @Inject constructor(
    private val userRepository:UserRepository
) {
    suspend fun register(password:String, user: User){
        val created= userRepository.createUser(user.email, password)
        if(created.isFailure) throw created.exceptionOrNull()?:SignUpFailedException()

        val saved= userRepository.saveUser(user)
        if(saved.isFailure) throw SaveUserInfoException()
    }

    suspend fun signIn(email:String, password: String):FirebaseUser?{
        val success = userRepository.signInUser(email, password)
        if(success.isFailure) throw success.exceptionOrNull()?:SignInFailedException()
        return success.getOrNull()
    }

    suspend fun signOut(){
        val success= userRepository.signOutUser()
        if(success.isFailure) throw SignOutFailedException()
    }
}

class SignUpFailedException:Exception("회원가입 실패")
class SaveUserInfoException:Exception("사용자 정보 저장 실패")
class SignInFailedException:Exception("로그인 실패")
class SignOutFailedException:Exception("로그아웃 실패")
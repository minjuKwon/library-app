package com.example.library.service

import com.example.library.data.User
import com.example.library.domain.UserRepository
import javax.inject.Inject

class FirebaseUserService @Inject constructor(
    private val userRepository:UserRepository
) {
    suspend fun register(password:String, user: User){
        val created= userRepository.createUser(user.email, password)
        val saved= userRepository.saveUser(user)

        if(created.isFailure) throw SignUpFailedException()
        if(saved.isFailure) throw SaveUserInfoException()
    }
}

class SignUpFailedException:Exception("회원가입 실패")
class SaveUserInfoException:Exception("사용자 정보 저장 실패")
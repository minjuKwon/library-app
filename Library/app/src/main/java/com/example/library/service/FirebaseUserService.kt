package com.example.library.service

import com.example.library.data.SessionManager
import com.example.library.data.User
import com.example.library.domain.UserRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FirebaseUserService @Inject constructor(
    private val userRepository:UserRepository,
    private val sessionManager: SessionManager
) {

    val userPreferences: Flow<User> = sessionManager.userPreferences
    val logInPreferences:Flow<Boolean> = sessionManager.logInPreferences

    suspend fun register(password:String, user: User){
        val created= userRepository.createUser(user.email, password)
        if(created.isFailure) throw created.exceptionOrNull()?:SignUpFailedException()

        val saved= userRepository.saveUser(user)
        val result= created.getOrNull()

        if(saved.isFailure) {
            if(result!=null) userRepository.removeUser(result)
            throw SaveUserInfoException()
        }

        result?.let { sendEmail(it) }
    }

    suspend fun signIn(email:String, password: String){
        val success = userRepository.signInUser(email, password)
        if(success.isFailure) throw success.exceptionOrNull()?:SignInFailedException()

        val user= success.getOrNull()
        if(user!=null){
            if(!user.isEmailVerified){
                sendEmail(user)
                throw VerificationFailedException()
            }
            val data=userRepository.getUser(user.uid)
            if(data.isFailure) throw data.exceptionOrNull()?:SaveSessionException()
            data.getOrNull()?.let { sessionManager.saveSession(it) }
        }
    }

    suspend fun signOut(){
        val success= userRepository.signOutUser()
        if(success.isFailure) throw SignOutFailedException()
        sessionManager.removeSession()
        sessionManager.removeLogInState()
    }

    suspend fun unregister(password: String){
        val reAuthenticate= userRepository.reAuthenticateUser(password)
        if(reAuthenticate.isFailure)
            throw reAuthenticate.exceptionOrNull()?:ReAuthenticateFailedException()

        val user= reAuthenticate.getOrNull()
        if(user!=null){
            val delete= userRepository.deleteUser(user)
            if(delete.isFailure)
                throw delete.exceptionOrNull()?:DeleteUserInfoException()

            val unRegister= userRepository.removeUser()
            if(unRegister.isFailure){
                val backupUser = delete.getOrNull()
                if(backupUser!=null) userRepository.saveUser(backupUser)
                throw unRegister.exceptionOrNull()?:UnRegisterFailedException()
            }
        }
    }

    suspend fun updateUserInfo(data: Map<String, Any>){
        val result= userRepository.updateUser(data)
        if(result.isFailure) throw result.exceptionOrNull()?:UpdateUserInfoException()
    }

    suspend fun verifyCurrentPassword(password: String){
        val reAuthenticate= userRepository.reAuthenticateUser(password)
        if(reAuthenticate.isFailure)
            throw reAuthenticate.exceptionOrNull()?:ReAuthenticateFailedException()
    }

    suspend fun updatePassword(password: String){
        val result= userRepository.updatePassword(password)
        if(result.isFailure) throw result.exceptionOrNull()?:UpdatePasswordException()
    }

    suspend fun sendEmail(user:FirebaseUser?){
        val send = userRepository.sendEmail(user)
        if(send.isFailure) throw send.exceptionOrNull()?:VerificationFailedException()
    }

    suspend fun isUserVerified():Boolean = userRepository.isVerified()

    suspend fun findPassword(email: String){
        val result= userRepository.resetPassword(email)
        if(result.isFailure) throw result.exceptionOrNull()?:ResetPasswordFAiledException()
    }

    suspend fun updateLogInState(b:Boolean){
        sessionManager.saveLogInState(b)
    }

}

class SignUpFailedException:Exception("회원가입 실패")
class SaveUserInfoException:Exception("사용자 정보 저장 실패")
class SignInFailedException:Exception("로그인 실패")
class SignOutFailedException:Exception("로그아웃 실패")
class ReAuthenticateFailedException:Exception("사용자 인증 실패")
class UnRegisterFailedException:Exception("회원가입 실패")
class DeleteUserInfoException:Exception("사용자 정보 삭제 실패")
class UpdateUserInfoException:Exception("사용자 정보 수정 실패")
class UpdatePasswordException:Exception("비밀번호 수정 실패")
class SaveSessionException:Exception("세션 저장 실패")
class VerificationFailedException:Exception("사용자 인증 실패")
class ResetPasswordFAiledException:Exception("비밀번호 수정 실패")
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
        val createdResult= userRepository.createUser(user.email, password)
        if(createdResult.isFailure) throw createdResult.exceptionOrNull()?:SignUpFailedException()

        val isSave= userRepository.saveUser(user)
        val createdUser= createdResult.getOrNull()

        if(isSave.isFailure) {
            createdUser?.let { userRepository.deleteUserAccount(it) }
            throw SaveUserInfoException()
        }

        createdUser?.let { sendVerificationEmail(it) }
    }

    suspend fun signIn(email:String, password: String){
        val signedInResult = userRepository.signInUser(email, password)
        if(signedInResult.isFailure) throw signedInResult.exceptionOrNull()?:SignInFailedException()
        signedInResult.getOrNull()?.let{ user ->
            if(!user.isEmailVerified){
                sendVerificationEmail(user)
                throw VerificationFailedException()
            }
            val signedInUserData=userRepository.getUser(user.uid)
            if(signedInUserData.isFailure) throw signedInUserData.exceptionOrNull()?:SaveSessionException()
            signedInUserData.getOrNull()?.let { sessionManager.saveUserData(it) }
        }
    }

    suspend fun signOut(){
        val isSignOut= userRepository.signOutUser()
        if(isSignOut.isFailure) throw SignOutFailedException()
        sessionManager.removeUserData()
        sessionManager.removeLogInState()
    }

    suspend fun unregister(password: String){
        val reAuthenticatedResult= userRepository.reAuthenticateUser(password)
        if(reAuthenticatedResult.isFailure)
            throw reAuthenticatedResult.exceptionOrNull()?:ReAuthenticateFailedException()

        reAuthenticatedResult.getOrNull()?.let{ user ->
            val deletedUser= userRepository.deleteUserData(user)
            if(deletedUser.isFailure)
                throw deletedUser.exceptionOrNull()?:DeleteUserInfoException()

            val isUnRegister= userRepository.deleteUserAccount()
            if(isUnRegister.isFailure){
                deletedUser.getOrNull()?.let { userRepository.saveUser(it) }
                throw isUnRegister.exceptionOrNull()?:UnRegisterFailedException()
            }
        }
    }

    suspend fun changeUserInfo(data: Map<String, Any>){
        val isUpdate= userRepository.updateUser(data)
        if(isUpdate.isFailure) throw isUpdate.exceptionOrNull()?:UpdateUserInfoException()
    }

    suspend fun verifyCurrentPassword(password: String){
        val isReAuthenticate= userRepository.reAuthenticateUser(password)
        if(isReAuthenticate.isFailure)
            throw isReAuthenticate.exceptionOrNull()?:ReAuthenticateFailedException()
    }

    suspend fun changePassword(password: String){
        val isChange= userRepository.updatePassword(password)
        if(isChange.isFailure) throw isChange.exceptionOrNull()?:UpdatePasswordException()
    }

    suspend fun sendVerificationEmail(user:FirebaseUser?){
        val isSend = userRepository.sendVerificationEmail(user)
        if(isSend.isFailure) throw isSend.exceptionOrNull()?:VerificationFailedException()
    }

    suspend fun isEmailVerified():Boolean = userRepository.isEmailVerified()

    suspend fun findPassword(email: String){
        val isReset= userRepository.resetPassword(email)
        if(isReset.isFailure) throw isReset.exceptionOrNull()?:ResetPasswordFAiledException()
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
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

    suspend fun register(user: User, password:String){
        val createdResult= userRepository.createUser(user.email, password)
        if(createdResult.isFailure) throw createdResult.exceptionOrNull()?:SignUpFailedException()

        val isSave= userRepository.saveUser(user)
        val createdUser= createdResult.getOrNull()

        if(isSave.isFailure) {
            createdUser?.let { userRepository.deleteUserAccount(it) }
            throw SaveUserInfoFailedException()
        }

        createdUser?.let { sendVerificationEmail(it) }
    }

    suspend fun unregister(password: String){
        val reAuthenticatedResult= userRepository.reAuthenticateUser(password)
        if(reAuthenticatedResult.isFailure)
            throw reAuthenticatedResult.exceptionOrNull()?:ReAuthenticateFailedException()

        reAuthenticatedResult.getOrNull()?.let{ user ->
            val deletedUser= userRepository.deleteUserData(user)
            if(deletedUser.isFailure)
                throw deletedUser.exceptionOrNull()?:DeleteUserInfoFailedException()

            val isUnRegister= userRepository.deleteUserAccount()
            if(isUnRegister.isFailure){
                deletedUser.getOrNull()?.let { userRepository.saveUser(it) }
                throw isUnRegister.exceptionOrNull()?:UnRegisterFailedException()
            }
        }
    }

    suspend fun changeUserInfo(data: Map<String, Any>){
        val isUpdate= userRepository.updateUser(data)
        if(isUpdate.isFailure) throw isUpdate.exceptionOrNull()?:UpdateUserInfoFailedException()
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
            if(signedInUserData.isFailure)
                throw signedInUserData.exceptionOrNull()?:SaveSessionFailedException()
            signedInUserData.getOrNull()?.let { sessionManager.saveUserData(it) }
        }
    }

    suspend fun signOut(){
        val isSignOut= userRepository.signOutUser()
        if(isSignOut.isFailure) throw SignOutFailedException()
        sessionManager.removeUserData()
        sessionManager.removeLogInState()
    }

    suspend fun sendVerificationEmail(user:FirebaseUser?){
        val isSend = userRepository.sendVerificationEmail(user)
        if(isSend.isFailure) throw isSend.exceptionOrNull()?:VerificationFailedException()
    }

    suspend fun isEmailVerified():Boolean = userRepository.isEmailVerified()

    suspend fun verifyCurrentPassword(password: String){
        val isReAuthenticate= userRepository.reAuthenticateUser(password)
        if(isReAuthenticate.isFailure)
            throw isReAuthenticate.exceptionOrNull()?:ReAuthenticateFailedException()
    }

    suspend fun changePassword(password: String){
        val isChange= userRepository.updatePassword(password)
        if(isChange.isFailure) throw isChange.exceptionOrNull()?:UpdatePasswordFailedException()
    }

    suspend fun findPassword(email: String){
        val isReset= userRepository.resetPassword(email)
        if(isReset.isFailure) throw isReset.exceptionOrNull()?:ResetPasswordFAiledException()
    }

    suspend fun updateLogInState(b:Boolean){
        sessionManager.saveLogInState(b)
    }

}
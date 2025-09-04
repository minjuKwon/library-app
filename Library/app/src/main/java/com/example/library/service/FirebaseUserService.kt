package com.example.library.service

import com.example.library.domain.ExternalUser
import com.example.library.domain.SessionManager
import com.example.library.data.entity.User
import com.example.library.domain.UserRepository
import com.example.library.domain.UserService
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FirebaseUserService @Inject constructor(
    private val userRepository:UserRepository,
    private val defaultSessionManager: SessionManager
): UserService {

    override val userPreferences: Flow<User> = defaultSessionManager.userPreferences
    override val logInPreferences:Flow<Boolean> = defaultSessionManager.logInPreferences

    override suspend fun register(user: User, password:String){
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

    override suspend fun unregister(password: String){
        val reAuthenticatedResult= userRepository.reAuthenticateUser(password)
        if(reAuthenticatedResult.isFailure)
            throw reAuthenticatedResult.exceptionOrNull()?:ReAuthenticateFailedException()

        reAuthenticatedResult.getOrNull()?.let{ user ->
            val deletedUser= userRepository.deleteUserData(user.uid)
            if(deletedUser.isFailure)
                throw deletedUser.exceptionOrNull()?:DeleteUserInfoFailedException()

            val isUnRegister= userRepository.deleteUserAccount()
            if(isUnRegister.isFailure){
                deletedUser.getOrNull()?.let { userRepository.saveUser(it) }
                throw isUnRegister.exceptionOrNull()?:UnRegisterFailedException()
            }
        }
    }

    override suspend fun changeUserInfo(data: Map<String, Any>){
        val isUpdate= userRepository.updateUser(data)
        if(isUpdate.isFailure) throw isUpdate.exceptionOrNull()?:UpdateUserInfoFailedException()
    }

    override suspend fun signIn(email:String, password: String){
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
            signedInUserData.getOrNull()?.let { defaultSessionManager.saveUserData(it) }
        }
    }

    override suspend fun signOut(){
        val isSignOut= userRepository.signOutUser()
        if(isSignOut.isFailure) throw SignOutFailedException()
        defaultSessionManager.removeUserData()
        defaultSessionManager.removeLogInState()
    }

    override suspend fun sendVerificationEmail(user: ExternalUser?){
        val isSend = userRepository.sendVerificationEmail(user)
        if(isSend.isFailure) throw isSend.exceptionOrNull()?:VerificationFailedException()
    }

    override suspend fun isEmailVerified():Boolean = userRepository.isEmailVerified()

    override suspend fun verifyCurrentPassword(password: String){
        val isReAuthenticate= userRepository.reAuthenticateUser(password)
        if(isReAuthenticate.isFailure)
            throw isReAuthenticate.exceptionOrNull()?:ReAuthenticateFailedException()
    }

    override suspend fun changePassword(password: String){
        val isChange= userRepository.updatePassword(password)
        if(isChange.isFailure) throw isChange.exceptionOrNull()?:UpdatePasswordFailedException()
    }

    override suspend fun findPassword(email: String){
        val isReset= userRepository.resetPassword(email)
        if(isReset.isFailure) throw isReset.exceptionOrNull()?:ResetPasswordFAiledException()
    }

    override suspend fun updateLogInState(b:Boolean){
        defaultSessionManager.saveLogInState(b)
    }

}
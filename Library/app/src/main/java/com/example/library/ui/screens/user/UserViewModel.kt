package com.example.library.ui.screens.user

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.library.data.User
import com.example.library.di.ApplicationScope
import com.example.library.service.FirebaseUserService
import com.google.firebase.auth.FirebaseAuthException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val firebaseUserService: FirebaseUserService,
    @ApplicationScope externalScope:CoroutineScope?=null
) : ViewModel(){

    private val scope= externalScope ?: viewModelScope

    private val _userPreferences: StateFlow<User> =
        firebaseUserService.userPreferences.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            User()
    )
    val userPreferences= _userPreferences

    private val _event= MutableSharedFlow<UserUiState>()
    val event= _event.asSharedFlow()

    private val _isLogIn= MutableStateFlow(false)
    val isLogIn= _isLogIn

    private val _isClickEmailLink= MutableStateFlow(false)
    val isClickEmailLink= _isClickEmailLink

    private val _isVerifyPassword= mutableStateOf(false)
    val isVerifyPassword= _isVerifyPassword

    private val _isVerifyUser= mutableStateOf(true)
    val isVerifyUser= _isVerifyUser

    fun register(password:String, user: User){
        scope.launch {
            try{
                firebaseUserService.register(password, user)
                _isVerifyUser.value=false
                _event.emit(UserUiState.Success)
            }catch(e:FirebaseAuthException){
                _event.emit(UserUiState.Failure(e.errorCode))
            }catch(e:Exception){
                _event.emit(UserUiState.Failure(e.message?:"실패"))
            }
        }
    }

    fun signIn(email:String, password: String){
        scope.launch {
            try{
                firebaseUserService.signIn(email, password)
                _event.emit(UserUiState.Success)
            }catch(e:FirebaseAuthException){
                _event.emit(UserUiState.Failure(e.errorCode))
            }catch (e:Exception){
                _event.emit(UserUiState.Failure(e.message?:"실패"))
            }
        }
    }

    fun signOut(){
        scope.launch {
            try{
                firebaseUserService.signOut()
                _event.emit(UserUiState.Success)
            }catch (e:Exception){
                _event.emit(UserUiState.Failure(e.message?:"실패"))
            }
        }
    }

    fun unregister(password: String){
        scope.launch {
            try {
                firebaseUserService.unregister(password)
                _event.emit(UserUiState.Success)
            }catch (e:FirebaseAuthException){
                _event.emit(UserUiState.Failure(e.errorCode))
            }catch (e:Exception){
                _event.emit(UserUiState.Failure(e.message?:"실패"))
            }
        }
    }

    fun updateUserInfo(data: Map<String, Any>){
        scope.launch {
            try {
                firebaseUserService.updateUserInfo(data)
                _event.emit(UserUiState.Success)
            }catch(e:FirebaseAuthException){
                _event.emit(UserUiState.Failure(e.errorCode))
            }catch (e:Exception){
                _event.emit(UserUiState.Failure(e.message?:"실패"))
            }
        }
    }

    fun verifyCurrentPassword(currentPassword: String){
        scope.launch {
            try {
                firebaseUserService.verifyCurrentPassword(currentPassword)
                _isVerifyPassword.value=true
            }catch(e:FirebaseAuthException){
                _event.emit(UserUiState.Failure(e.errorCode))
            }catch (e:Exception){
                _event.emit(UserUiState.Failure(e.message?:"실패"))
            }
        }
    }

    fun updatePassword(newPassword:String){
        scope.launch {
            try {
                if(_isVerifyPassword.value) firebaseUserService.updatePassword(newPassword)
                _event.emit(UserUiState.Success)
            }catch(e:FirebaseAuthException){
                _event.emit(UserUiState.Failure(e.errorCode))
            }catch (e:Exception){
                _event.emit(UserUiState.Failure(e.message?:"실패"))
            }
        }
    }

    fun sendVerificationEmail(){
        scope.launch {
            try {
                firebaseUserService.sendEmail(null)
                _event.emit(UserUiState.Success)
            }catch(e:FirebaseAuthException){
                _event.emit(UserUiState.Failure(e.errorCode))
            }catch (e:Exception){
                _event.emit(UserUiState.Failure(e.message?:"실패"))
            }
        }
    }

    fun checkUserIsVerified(){
        scope.launch {
            _isVerifyUser.value = firebaseUserService.isUserVerified()
        }
    }

    fun updateLogInState(b:Boolean){
        _isLogIn.value= b
    }

    fun updatePasswordCheckState(b:Boolean){
        _isVerifyPassword.value=b
    }

    fun updateEmailLinkState(b:Boolean){
        _isClickEmailLink.value=b
    }

}
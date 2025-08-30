package com.example.library.ui.screens.user

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.library.data.User
import com.example.library.di.ApplicationScope
import com.example.library.domain.UserService
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
    private val firebaseUserService: UserService,
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

    private val _isLogIn: StateFlow<Boolean> =
        firebaseUserService.logInPreferences.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            false
        )
    val isLogIn= _isLogIn

    private val _event= MutableSharedFlow<UserUiState>()
    val event= _event.asSharedFlow()

    private val _isUserVerified= mutableStateOf(true)
    val isUserVerified= _isUserVerified

    private val _isPasswordVerified= mutableStateOf(false)
    val isPasswordVerified= _isPasswordVerified

    private val _isClickEmailLink= MutableStateFlow(false)
    val isClickEmailLink= _isClickEmailLink

    fun register(user: User, password:String){
        scope.launch {
            try{
                firebaseUserService.register(user, password)
                _isUserVerified.value=false
                _event.emit(UserUiState.Success)
            }catch(e:Exception){
                _event.emit(UserUiState.Failure(e.message?:"실패"))
            }
        }
    }

    fun unregister(password: String){
        scope.launch {
            try {
                firebaseUserService.unregister(password)
                _event.emit(UserUiState.Success)
            }catch (e:Exception){
                _event.emit(UserUiState.Failure(e.message?:"실패"))
            }
        }
    }

    fun changeUserInfo(data: Map<String, Any>){
        scope.launch {
            try {
                firebaseUserService.changeUserInfo(data)
                _event.emit(UserUiState.Success)
            }catch (e:Exception){
                _event.emit(UserUiState.Failure(e.message?:"실패"))
            }
        }
    }

    fun signIn(email:String, password: String){
        scope.launch {
            try{
                firebaseUserService.signIn(email, password)
                _event.emit(UserUiState.Success)
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

    fun sendVerificationEmail(){
        scope.launch {
            try {
                firebaseUserService.sendVerificationEmail(null)
                _event.emit(UserUiState.Success)
            }catch (e:Exception){
                _event.emit(UserUiState.Failure(e.message?:"실패"))
            }
        }
    }

    fun verifyCurrentPassword(currentPassword: String){
        scope.launch {
            try {
                firebaseUserService.verifyCurrentPassword(currentPassword)
                _isPasswordVerified.value=true
            }catch (e:Exception){
                _event.emit(UserUiState.Failure(e.message?:"실패"))
            }
        }
    }

    fun changePassword(newPassword:String){
        scope.launch {
            try {
                if(_isPasswordVerified.value) firebaseUserService.changePassword(newPassword)
                _event.emit(UserUiState.Success)
            }catch (e:Exception){
                _event.emit(UserUiState.Failure(e.message?:"실패"))
            }
        }
    }

    fun findPassword(email: String){
        scope.launch {
            try {
                firebaseUserService.findPassword(email)
            }catch (e:Exception){
                _event.emit(UserUiState.Failure(e.message?:"실패"))
            }
        }
    }

    fun updateLogInState(b:Boolean){
        scope.launch {
            firebaseUserService.updateLogInState(b)
        }
    }

    fun checkUserVerified(){
        scope.launch {
            _isUserVerified.value = firebaseUserService.isEmailVerified()
        }
    }

    fun updatePasswordVerifiedState(b:Boolean){
        _isPasswordVerified.value=b
    }

    fun updateEmailVerifiedState(b:Boolean){
        _isClickEmailLink.value=b
    }

}
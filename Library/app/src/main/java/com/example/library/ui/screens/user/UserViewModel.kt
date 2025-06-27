package com.example.library.ui.screens.user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.library.data.User
import com.example.library.di.ApplicationScope
import com.example.library.service.FirebaseUserService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val firebaseUserService: FirebaseUserService,
    @ApplicationScope externalScope:CoroutineScope?=null
) : ViewModel(){

    private val scope= externalScope ?: viewModelScope

    private val _event= MutableSharedFlow<UserUiState>()
    val event= _event.asSharedFlow()

    fun register(password:String, user: User){
        scope.launch {
            try{
                firebaseUserService.register(password, user)
                _event.emit(UserUiState.Success)
            }catch(e:Exception){
                _event.emit(UserUiState.Failure(e.message?:"실패"))
            }
        }
    }
}
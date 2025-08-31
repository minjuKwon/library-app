package com.example.library.ui.screens.user

sealed class UserUiState {
    data class Success(val message:String=""): UserUiState()
    data class Failure(val message:String): UserUiState()
}
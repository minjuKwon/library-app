package com.example.library.ui.screens.user

sealed class UserUiState {
    object Success: UserUiState()
    data class Failure(val message:String): UserUiState()
}
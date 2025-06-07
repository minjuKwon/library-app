package com.example.library.ui.screens.detail

sealed class LibraryDetailsUiState{
    object Success: LibraryDetailsUiState()
    object Loading: LibraryDetailsUiState()
}
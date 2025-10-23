package com.example.library.ui.screens.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.library.data.entity.Library
import com.example.library.ui.screens.search.defaultLibrary
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LibraryDetailsViewModel @Inject constructor(): ViewModel() {

    var uiState: LibraryDetailsUiState by mutableStateOf(LibraryDetailsUiState.Loading)
        private set

    private val _currentLibrary= mutableStateOf(defaultLibrary)
    val currentLibrary= _currentLibrary

    fun updateCurrentItem(library: Library){
        uiState=LibraryDetailsUiState.Loading
        _currentLibrary.value= library
        uiState=LibraryDetailsUiState.Success
    }

}
package com.example.library.ui.screens.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.library.data.entity.Library
import com.example.library.di.ApplicationScope
import com.example.library.domain.LibrarySyncService
import com.example.library.ui.screens.search.defaultLibrary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryDetailsViewModel @Inject constructor(
    private val librarySyncService: LibrarySyncService,
    @ApplicationScope externalScope: CoroutineScope?=null
): ViewModel() {

    private val scope= externalScope?:viewModelScope

    var uiState: LibraryDetailsUiState by mutableStateOf(LibraryDetailsUiState.Loading)
        private set

    private val _textFieldKeyword = mutableStateOf("android")

    private val _currentLibrary= mutableStateOf(defaultLibrary)
    val currentLibrary= _currentLibrary

    private val _isDataReadyForUi = MutableStateFlow(false)
    val isDataReadyForUi: StateFlow<Boolean> = _isDataReadyForUi

    fun getBookById(
        id:String,
        pageNumber:Int
    ): Library {
        if(_isDataReadyForUi.value){
            uiState=LibraryDetailsUiState.Loading
            scope.launch {
                val list= librarySyncService.getSearchBooks(_textFieldKeyword.value, pageNumber)

                _currentLibrary.value= list?.find { it.libraryId == id } ?: _currentLibrary.value
                uiState=LibraryDetailsUiState.Success
            }

        }
        return _currentLibrary.value
    }

    fun updateKeyword(input:String){
        _textFieldKeyword.value=input
    }

    fun updateDataReadyForUi(b:Boolean){
        _isDataReadyForUi.value= b
    }

    fun updateCurrentItem(library: Library){
        uiState=LibraryDetailsUiState.Loading
        _currentLibrary.value= library
        uiState=LibraryDetailsUiState.Success
    }

}
package com.example.library.ui.screens.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.library.data.entity.Book
import com.example.library.di.ApplicationScope
import com.example.library.di.IoDispatcher
import com.example.library.domain.RemoteRepository
import com.example.library.ui.screens.search.defaultBookInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LibraryDetailsViewModel @Inject constructor(
    private val bookRepository: RemoteRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher= Dispatchers.IO,
    @ApplicationScope externalScope: CoroutineScope?=null
): ViewModel() {

    private val scope= externalScope?:viewModelScope

    var uiState: LibraryDetailsUiState by mutableStateOf(LibraryDetailsUiState.Loading)
        private set

    private val _textFieldKeyword = mutableStateOf("android")

    private val _currentBook= mutableStateOf(Book("0", defaultBookInfo))
    val currentBook= _currentBook

    private val _isDataReadyForUi = MutableStateFlow(false)
    val isDataReadyForUi: StateFlow<Boolean> = _isDataReadyForUi

    fun getBookById(
        id:String
    ): Book {
        if(_isDataReadyForUi.value){
            uiState=LibraryDetailsUiState.Loading
            scope.launch {
                val list= withContext(ioDispatcher){
                    bookRepository
                        .searchVolume(_textFieldKeyword.value,10,0).book
                }
                _currentBook.value= list.find { it.id == id } ?: _currentBook.value
                uiState=LibraryDetailsUiState.Success
            }

        }
        return _currentBook.value
    }

    fun updateKeyword(input:String){
        _textFieldKeyword.value=input
    }

    fun updateDataReadyForUi(b:Boolean){
        _isDataReadyForUi.value= b
    }

    fun updateCurrentItem(book: Book){
        uiState=LibraryDetailsUiState.Loading
        _currentBook.value= book
        uiState=LibraryDetailsUiState.Success
    }

}
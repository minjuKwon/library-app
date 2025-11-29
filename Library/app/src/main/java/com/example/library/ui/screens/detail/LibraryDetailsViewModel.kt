package com.example.library.ui.screens.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.library.data.entity.Library
import com.example.library.data.entity.User
import com.example.library.di.ApplicationScope
import com.example.library.domain.SessionManager
import com.example.library.service.FirebaseBookService
import com.example.library.ui.screens.search.defaultLibrary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryDetailsViewModel @Inject constructor(
    private val firebaseBookService: FirebaseBookService,
    defaultSessionManager: SessionManager,
    @ApplicationScope externalScope: CoroutineScope? = null
): ViewModel() {

    private val scope = externalScope ?: viewModelScope

    private val _userPreferences: StateFlow<User?> =
        defaultSessionManager.userPreferences.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            null
        )
    val userPreferences= _userPreferences

    var uiState: LibraryDetailsUiState by mutableStateOf(LibraryDetailsUiState.Loading)
        private set

    private val _currentLibrary= mutableStateOf(defaultLibrary)
    val currentLibrary= _currentLibrary

    fun updateCurrentItem(library: Library){
        uiState=LibraryDetailsUiState.Loading
        _currentLibrary.value= library
        uiState=LibraryDetailsUiState.Success
    }

    fun loanLibrary(
        keyword: String,
        page: String
    ){
        uiState=LibraryDetailsUiState.Loading

        scope.launch {
            val id= awaitUserId()
            uiState=try{
                val isSave= firebaseBookService.saveLoanHistory(
                    userId = id,
                    keyword = keyword,
                    page = page,
                    libraryId = _currentLibrary.value.libraryId,
                    bookId = _currentLibrary.value.book.id
                )
                if(isSave.isSuccess)LibraryDetailsUiState.Success
                else LibraryDetailsUiState.Error
            }catch(e: Exception){
                LibraryDetailsUiState.Error
            }
        }
    }

    fun updateCurrentBookStatus(bookStatus: BookStatus){
        _currentLibrary.value=_currentLibrary.value.copy(bookStatus = bookStatus)
    }
    private suspend fun awaitUserId(): String {
        return userPreferences.filterNotNull().first().uid
    }

}
package com.example.library.ui.screens.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.library.data.entity.BookStatus
import com.example.library.data.entity.BookStatusType
import com.example.library.data.entity.Library
import com.example.library.data.entity.LibraryHistory
import com.example.library.data.entity.User
import com.example.library.data.mapper.toStringType
import com.example.library.di.ApplicationScope
import com.example.library.domain.SessionManager
import com.example.library.service.FirebaseBookService
import com.example.library.ui.screens.search.defaultLibrary
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.Instant
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

    private var bookStatusListener: ListenerRegistration? =null

    fun updateCurrentItem(library: Library){
        uiState=LibraryDetailsUiState.Loading
        _currentLibrary.value= library
        uiState=LibraryDetailsUiState.Success
    }

    fun loanLibrary(
        keyword: String,
        page: String
    ){
        scope.launch {
            uiState=LibraryDetailsUiState.Loading

            val id= awaitUserId()
            uiState=try{
                val isSave= firebaseBookService.updateLibraryHistory(
                    userId = id,
                    libraryId = _currentLibrary.value.libraryId,
                    bookId = _currentLibrary.value.book.id,
                    bookStatus= _currentLibrary.value.bookStatus.toStringType(),
                    bookTitle = _currentLibrary.value.book.bookInfo.title,
                    bookAuthors = _currentLibrary.value.book.bookInfo.authors,
                    keyword = keyword,
                    page = page
                )

                if(isSave.isSuccess) LibraryDetailsUiState.Success
                else LibraryDetailsUiState.Error
            }catch(e: Exception){
                LibraryDetailsUiState.Error
            }
        }
    }

    fun getBookStatus(){
        bookStatusListener?.remove()

        scope.launch {
            val uid= awaitUserId()

            val registration = firebaseBookService.getLibraryStatus(
                bookId = _currentLibrary.value.book.id,
                callback = { updateBookStatus(uid, it) }
            )

            bookStatusListener = registration
        }
    }

    private fun updateBookStatus(userId:String, libraryHistory: LibraryHistory) {
        val bookId= _currentLibrary.value.book.id
        if (bookId == libraryHistory.bookId){
            val bookStatus:BookStatus = when(libraryHistory.status){
                BookStatusType.AVAILABLE.name -> BookStatus.Available
                BookStatusType.UNAVAILABLE.name -> BookStatus.UnAvailable
                BookStatusType.BORROWED.name ->{
                    if(userId == libraryHistory.userId){
                        BookStatus.Borrowed(
                            userId = libraryHistory.userId,
                            borrowedAt = Instant.ofEpochMilli(libraryHistory.loanDate),
                            dueDate = Instant.ofEpochMilli(libraryHistory.dueDate)
                        )
                    } else {
                        BookStatus.UnAvailable
                    }
                }
                BookStatusType.RESERVED.name ->{
                    BookStatus.Reserved(
                        userId = libraryHistory.userId,
                        reservedAt = Instant.ofEpochMilli(libraryHistory.loanDate),
                    )
                }
                else -> BookStatus.UnAvailable
            }
            _currentLibrary.value= _currentLibrary.value.copy(bookStatus= bookStatus)
        }
        else _currentLibrary.value
    }

    private suspend fun awaitUserId(): String {
        return userPreferences.filterNotNull().first().uid
    }

}
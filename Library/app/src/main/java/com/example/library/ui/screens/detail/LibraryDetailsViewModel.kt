package com.example.library.ui.screens.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.library.core.DateTimeConverter.calculateOverDueDate
import com.example.library.core.DateTimeConverter.isSuspensionOver
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
import com.example.library.ui.screens.user.getSuspensionEndDateToLong
import com.google.firebase.firestore.ListenerRegistration
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
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

    private val _uiState= MutableStateFlow<LibraryDetailsUiState>(LibraryDetailsUiState.Loading)
    val uiState=_uiState

    private val _currentLibrary= MutableStateFlow(defaultLibrary)
    val currentLibrary= _currentLibrary

    private val _reservationStatus= MutableStateFlow("")
    val reservationStatus= _reservationStatus

    private val _isShowOverdueDialog= MutableStateFlow(false)
    val isShowOverdueDialog= _isShowOverdueDialog

    private val _isShowSuspensionDateDialog= MutableStateFlow(false)
    val isShowSuspensionDateDialog= _isShowSuspensionDateDialog

    private val _isShowReservationDialog= MutableStateFlow(false)
    val isShowReservationDialog= _isShowReservationDialog

    private val _reservationCount= MutableStateFlow<Map<String, Int>>(emptyMap())
    val reservationCount= _reservationCount

    private val _loadCompleteLoad = MutableStateFlow(false)
    private val _loadCompleteLoadForCnt = MutableStateFlow(false)

    private var bookStatusListener: ListenerRegistration? =null

    fun updateCurrentItem(library: Library){
        _uiState.value=LibraryDetailsUiState.Loading
        _currentLibrary.value= library
        _uiState.value=LibraryDetailsUiState.Success
    }

    fun loanLibrary(
        keyword: String,
        page: String
    ){
        scope.launch {
            _loadCompleteLoad.value=false
            _uiState.value=LibraryDetailsUiState.Loading

            val id= awaitUserId()
            val bookStatus= _currentLibrary.value.bookStatus

            if(bookStatus== BookStatus.Available|| bookStatus== BookStatus.UnAvailable){
                val isOverdue= firebaseBookService.isOverdueBook(id)
                isOverdue.getOrNull()?.let { _isShowOverdueDialog.value=it }

                val overdueList= firebaseBookService.getUserLoanBookList(id)
                if(overdueList.isSuccess){
                    val resultList= overdueList.getOrNull()
                    if(resultList!=null){
                        val suspensionEndDate= resultList.getSuspensionEndDateToLong()
                        _isShowSuspensionDateDialog.value= isSuspensionOver(suspensionEndDate)
                    }
                }
            }

            _uiState.value=try{
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

                if(isSave.isSuccess) {
                    _loadCompleteLoad.value=true
                    LibraryDetailsUiState.Success
                }
                else LibraryDetailsUiState.Error
            }catch(e: Exception){
                LibraryDetailsUiState.Error
            }
        }
    }

    //_loadCompleteLoad 필터링 없이
    //getBookStatus() 단독 사용을 위해 매개변수로 타이밍 조절
    fun getBookStatus(b:Boolean){
        bookStatusListener?.remove()

        scope.launch {
            _loadCompleteLoadForCnt.value=b
            _loadCompleteLoad.filter { it }.first()
            _loadCompleteLoadForCnt.filter { it }.first()

            val uid= awaitUserId()
            val isUserReserved=firebaseBookService.isUserReservedBook(uid)
            val isUserReservedResult= isUserReserved.getOrNull()
            val isReservedBook= firebaseBookService.isReservedBook(_currentLibrary.value.book.id)
            val isReservedBookResult= isReservedBook.getOrNull()

            val registration = firebaseBookService.getLibraryStatus(
                bookId = _currentLibrary.value.book.id,
                callback = {
                    updateBookStatus(
                        uid,
                        isUserReservedResult,
                        isReservedBookResult,
                        it
                    )
                }
            )

            bookStatusListener = registration
            _loadCompleteLoadForCnt.value=false
        }
    }

    private fun updateBookStatus(
        userId:String,
        isUserReserved:Boolean?,
        isReservedBook:Boolean?,
        libraryHistory: LibraryHistory
    ) {
        val bookId= _currentLibrary.value.book.id
        if (bookId == libraryHistory.bookId){
            val bookStatus:BookStatus = when(libraryHistory.status){
                BookStatusType.AVAILABLE.name-> BookStatus.Available
                BookStatusType.RETURNED.name -> {
                    if(isReservedBook!=null&&isReservedBook){
                        BookStatus.Reserved
                    }else{
                        BookStatus.Available
                    }
                }
                BookStatusType.UNAVAILABLE.name -> BookStatus.UnAvailable
                BookStatusType.BORROWED.name ->{
                    if(userId == libraryHistory.userId){
                        BookStatus.Borrowed(
                            userId = libraryHistory.userId,
                            borrowedAt = Instant.ofEpochMilli(libraryHistory.loanDate),
                            dueDate = Instant.ofEpochMilli(libraryHistory.dueDate)
                        )
                    }else {
                        updateReservationDialog(false)
                        if(_reservationCount.value.contains(bookId)){
                            val count= _reservationCount.value[bookId]
                            if(count!=null&&count>0){
                                //예약 취소
                                if(isUserReserved!=null&&!isUserReserved){
                                    BookStatus.UnAvailable
                                }else{
                                    //예약
                                    BookStatus.Reserved
                                }
                            }
                            else{
                                BookStatus.UnAvailable
                            }
                        }else{
                            BookStatus.UnAvailable
                        }
                    }
                }
                BookStatusType.OVERDUE.name -> {
                    if(userId== libraryHistory.userId){
                        BookStatus.OverDue(
                            userId = libraryHistory.userId,
                            overdueDate = calculateOverDueDate(libraryHistory.dueDate)
                        )
                    }else{
                        BookStatus.UnAvailable
                    }
                }
                BookStatusType.RESERVED.name ->  BookStatus.Reserved
                else -> BookStatus.UnAvailable
            }

            _currentLibrary.value= _currentLibrary.value.copy(bookStatus= bookStatus)
        }
        else _currentLibrary.value
    }

    //_loadCompleteLoad 필터링 없이
    //getReservationCount() 단독 사용을 위해 매개변수로 타이밍 조절
    fun getReservationCount(b:Boolean){
        scope.launch {
            _loadCompleteLoad.value=b
            _loadCompleteLoadForCnt.value=false
            _loadCompleteLoad.filter { it }.first()
            val count=firebaseBookService.getLibraryReservationCount(_currentLibrary.value.book.id)
            val countResult= count.getOrNull()
            if(countResult!=null){
                _reservationCount.update { it+(_currentLibrary.value.book.id to countResult) }
                updateReservationDialog(true)
                _loadCompleteLoadForCnt.value=true
            }
        }
    }

    fun getReservedStatus(){
        scope.launch {
            val uid= awaitUserId()
            val status= firebaseBookService.getReservedStatus(uid, _currentLibrary.value.book.id)
            val statusResult= status.getOrNull()
            if(statusResult!=null){
                _reservationStatus.value= statusResult
            }
        }
    }

    fun updateOverdueDialog(b:Boolean){
        _isShowOverdueDialog.value=b
    }

    fun updateSuspensionDialog(b:Boolean){
        _isShowSuspensionDateDialog.value=b
    }

    fun updateReservationDialog(b:Boolean){
        _isShowReservationDialog.value=b
    }

    private suspend fun awaitUserId(): String {
        return userPreferences.filterNotNull().first().uid
    }

}
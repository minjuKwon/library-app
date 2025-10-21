package com.example.library.ui.screens.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.library.data.entity.Book
import com.example.library.di.ApplicationScope
import com.example.library.domain.LibrarySyncService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val librarySyncService: LibrarySyncService,
    @ApplicationScope externalScope: CoroutineScope? = null
):ViewModel() {

    private val scope = externalScope ?: viewModelScope

    var libraryUiState: LibraryUiState by mutableStateOf(LibraryUiState.Loading)
        private set

    private val _textFieldKeyword = mutableStateOf("android")
    val textFieldKeyword=_textFieldKeyword

    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage

    private val _backPressedTime= MutableStateFlow(0L)

    init {
        getInformation()
    }

    fun getInformation(
        search:String=_textFieldKeyword.value,
        page:Int=1
    ){
        scope.launch {
            libraryUiState= LibraryUiState.Loading

            libraryUiState = try{
                _currentPage.value=page

                val list= librarySyncService.getSearchBooks(search, page)
                val totalItemCount= librarySyncService.getTotalCntForKeyword(search)

                if(list!=null&&totalItemCount!=null) {
                    LibraryUiState.Success(totalItemCount,list)
                }else{
                    LibraryUiState.Success(0, emptyList())
                }
            }catch (e: IOException){
                LibraryUiState.Error
            }
        }
    }

    fun updateKeyword(input:String){
        _textFieldKeyword.value=input
    }

    fun updateBackPressedTime(currentTime:Long){
        _backPressedTime.value= currentTime
    }

    fun isBackPressedDouble():Boolean{
        //백 스택의 루트일 때(첫 화면), 뒤로가기 버튼을 연속 두번 누르면(2000미만) 앱 종료
        val currentTime= System.currentTimeMillis()
        return currentTime - _backPressedTime.value<2000
    }

    fun updateBookmarkList(book: Book){
        book.bookInfo.isBookmarked= !book.bookInfo.isBookmarked
        libraryUiState=updateCopiedUiState(libraryUiState){
            var tempList:MutableList<Book> = it.bookmarkList
            tempList = if(book.bookInfo.isBookmarked){
                (tempList+book) as MutableList<Book>
            }else{
                (tempList-book) as MutableList<Book>
            }
            it.copy(bookmarkList = tempList)
        }
    }

    private fun updateCopiedUiState(
        uiState: LibraryUiState,
        copyOperation:(LibraryUiState.Success)-> LibraryUiState.Success
    ): LibraryUiState
    {
        return when(uiState){
            is LibraryUiState.Success -> copyOperation(uiState)
            else ->uiState
        }
    }

}
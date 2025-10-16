package com.example.library.ui.screens.search

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.library.data.entity.Book
import com.example.library.di.ApplicationScope
import com.example.library.di.IoDispatcher
import com.example.library.domain.RemoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val bookRepository: RemoteRepository,
    @IoDispatcher private val ioDispatcher:CoroutineDispatcher = Dispatchers.IO,
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

//        scope.launch {
//            libraryUiState = try{
//                val totalItemCount=withContext(ioDispatcher){
//                    bookRepository
//                        .searchVolume(search,10,0).totalCount
//                }
//                _currentPage.value=page
//                when(libraryUiState){
//                    is LibraryUiState.Success ->{
//                        val pageDataBookmarked=pageData.map { page->
//                            page.map { data->
//                                data.copy(
//                                    bookInfo = data.bookInfo.copy(
//                                        isBookmarked =
//                                        (libraryUiState as LibraryUiState.Success)
//                                            .bookmarkList.any { it.id==data.id }
//                                    )
//                                )
//                            }
//                        }
//                        (libraryUiState as LibraryUiState.Success)
//                            .copy(list= PageData(pageDataBookmarked,totalItemCount))
//                    }
//                    else-> LibraryUiState.Success(
//                        list = PageData(pageData, totalItemCount)
//                    )
//                }
//            }catch (e: IOException){
//                LibraryUiState.Error
//            }
//        }

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
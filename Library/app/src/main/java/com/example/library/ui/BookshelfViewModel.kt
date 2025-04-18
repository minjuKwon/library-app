package com.example.library.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.example.library.BookshelfApplication
import com.example.library.data.BookPagingSource
import com.example.library.data.BookType
import com.example.library.data.BookshelfRepository
import com.example.library.network.Book
import com.example.library.network.BookInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

const val PAGE_SIZE=10

class BookshelfViewModel(
    private val bookshelfRepository: BookshelfRepository
):ViewModel() {

    var bookshelfUiState: BookshelfUiState by mutableStateOf(BookshelfUiState.Loading)
        private set

    private val _currentPage = MutableStateFlow(1)
    val currentPage: StateFlow<Int> = _currentPage

    private val _currentOrder = MutableStateFlow(false)
    val currentOrder:StateFlow<Boolean> = _currentOrder

    private val _textFieldKeyword = mutableStateOf("android")
    val textFieldKeyword=_textFieldKeyword

    fun updateOrder(b:Boolean){
        _currentOrder.value= b
    }

    fun updateKeyword(input:String){
        _textFieldKeyword.value=input
    }

    init {
        getInformation()
    }

    fun getInformation(
        search:String=_textFieldKeyword.value,
        page:Int=1
    ){

        val pageData: Flow<PagingData<Book>> = Pager(
            config= PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            )){BookPagingSource(bookshelfRepository,input=search,page=page,pageSize=PAGE_SIZE)}
            .flow.cachedIn(viewModelScope)

        viewModelScope.launch {
            bookshelfUiState = try{

                val totalCount=withContext(Dispatchers.IO){
                    bookshelfRepository
                        .getBookListInformation(search,10,0).totalCount
                }

                _currentPage.value=page

                when(bookshelfUiState){
                    is BookshelfUiState.Success->{

                        val pageDataBookmarked=pageData.map { page->
                            page.map { data->
                                data.copy(
                                    bookInfo = data.bookInfo.copy(
                                        isBookmarked =
                                        (bookshelfUiState as BookshelfUiState.Success)
                                            .bookmarkList.any { it.id==data.id }
                                    )
                                )
                            }
                        }

                        (bookshelfUiState as BookshelfUiState.Success)
                            .copy(list= PageData(pageDataBookmarked,totalCount))

                    }
                    else-> BookshelfUiState.Success(
                        list= PageData(pageData,totalCount)
                    )
                }
            }catch (e: IOException){
                BookshelfUiState.Error
            }
        }

    }

    companion object{
        val Factory : ViewModelProvider.Factory = viewModelFactory{
            initializer{
                val application = (this[APPLICATION_KEY] as BookshelfApplication)
                val bookshelfRepository = application.container.bookshelfRepository
                BookshelfViewModel(bookshelfRepository)
            }
        }
    }

    fun updateDetailsScreenState(bookInfo: BookInfo){
        bookshelfUiState=updateCopiedUiState(bookshelfUiState){
            it.currentItem[it.currentTabType] = bookInfo
            it.copy(currentItem = it.currentItem, isShowingHomepage = false)
        }
    }

    fun resetHomeScreenState(bookInfo:BookInfo){
        bookshelfUiState=updateCopiedUiState(bookshelfUiState){
            it.currentItem[it.currentTabType] = bookInfo
            it.copy(
                currentItem = it.currentItem, isShowingHomepage = true
            )
        }
    }

    fun updateCurrentBookTabType(bookType: BookType){
        bookshelfUiState=updateCopiedUiState(bookshelfUiState){
            it.copy(currentTabType = bookType)
        }
    }

    fun updateBookmarkList(book:Book){
        book.bookInfo.isBookmarked= !book.bookInfo.isBookmarked
        bookshelfUiState=updateCopiedUiState(bookshelfUiState){
            var tempList:MutableList<Book> = it.bookmarkList
            tempList = if(book.bookInfo.isBookmarked){
                (tempList+book) as MutableList<Book>
            }else{
                (tempList-book) as MutableList<Book>
            }
            it.copy(bookmarkList = tempList)
        }
    }

    fun initCurrentItem(bookType: BookType,bookInfo: BookInfo){
        bookshelfUiState=updateCopiedUiState(bookshelfUiState){
            it.currentItem[bookType]=bookInfo
            it.copy(currentItem = it.currentItem)
        }
    }

    private fun updateCopiedUiState(
        uiState:BookshelfUiState,
        copyOperation:(BookshelfUiState.Success)-> BookshelfUiState.Success
    ):BookshelfUiState
    {
        return when(uiState){
            is BookshelfUiState.Success-> copyOperation(uiState)
            else ->uiState
        }
    }

}
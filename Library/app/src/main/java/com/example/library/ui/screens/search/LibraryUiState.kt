package com.example.library.ui.screens.search

import androidx.paging.PagingData
import com.example.library.data.entity.Book
import com.example.library.data.entity.BookImage
import com.example.library.data.entity.BookInfo
import com.example.library.data.entity.BookStatus
import com.example.library.data.entity.Library

sealed class LibraryUiState{
    data class Success(
        val totalCount:Int,
        val list:List<Library>,
        val bookmarkList:MutableList<Book> = mutableListOf(),
        var currentItem:Library = defaultLibrary,
    ): LibraryUiState()
    object Error : LibraryUiState()
    object Loading: LibraryUiState()
}

val defaultBookInfo = BookInfo(
    "", emptyList(),"","","",
    BookImage("","","","")
)

data class PageData(
    val book: Flow<PagingData<Book>>,
    val totalCount:Int
)
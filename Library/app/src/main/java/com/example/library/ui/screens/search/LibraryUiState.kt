package com.example.library.ui.screens.search

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

val defaultLibrary = Library(
        "", Book("", defaultBookInfo), BookStatus.Available,
        "","",0
    )
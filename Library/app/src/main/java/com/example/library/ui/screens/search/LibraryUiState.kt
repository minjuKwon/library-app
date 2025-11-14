package com.example.library.ui.screens.search

import com.example.library.data.entity.Book
import com.example.library.data.entity.BookImage
import com.example.library.data.entity.BookInfo
import com.example.library.data.entity.BookStatus
import com.example.library.data.entity.Library
import com.example.library.ui.common.LibraryUiModel

sealed class LibraryUiState{
    data class Success(
        val totalCount:Int,
        val list:List<LibraryUiModel>
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
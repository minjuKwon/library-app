package com.example.library.ui.screens.search

import androidx.paging.PagingData
import com.example.library.data.api.Book
import com.example.library.data.api.BookInfo
import com.example.library.data.api.Image
import kotlinx.coroutines.flow.Flow

sealed class LibraryUiState{
    data class Success(
        val list : PageData,
        val bookmarkList:MutableList<Book> = mutableListOf(),
        var currentItem : BookInfo = defaultBookInfo,
    ): LibraryUiState()
    object Error : LibraryUiState()
    object Loading: LibraryUiState()
}

val defaultBookInfo
= BookInfo("", emptyList(),"","","",
    Image("","","","")
)

data class PageData(
    val book: Flow<PagingData<Book>>,
    val totalCount:Int
)
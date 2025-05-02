package com.example.library.ui

import androidx.paging.PagingData
import com.example.library.data.NavigationMenuType
import com.example.library.network.Book
import com.example.library.network.BookInfo
import com.example.library.network.Image
import kotlinx.coroutines.flow.Flow

sealed class LibraryUiState{
    data class Success(
        val list :PageData,
        val bookmarkList:MutableList<Book> = mutableListOf(),
        val currentTabType:NavigationMenuType=NavigationMenuType.Books,
        val currentItem : MutableMap <NavigationMenuType,BookInfo> = mutableMapOf(),
        val isShowingHomepage: Boolean = true
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
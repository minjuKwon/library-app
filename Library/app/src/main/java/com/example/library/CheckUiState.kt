package com.example.library

import androidx.paging.PagingData
import com.example.library.data.BookType
import com.example.library.network.Book
import com.example.library.network.BookInfo
import com.example.library.ui.BookshelfUiState
import com.example.library.ui.defaultBookInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

fun getTabPressed(
    bookshelfUiState: BookshelfUiState
): BookType
{
    return getDataByUiState(
        bookshelfUiState=bookshelfUiState,
        onSuccess={it.currentTabType},
        onFailure={BookType.Books}
    )
}

fun isBookmarkListEmpty(
    bookshelfUiState: BookshelfUiState
):Boolean
{
    return getDataByUiState(
        bookshelfUiState=bookshelfUiState,
        onSuccess={it.bookmarkList.isEmpty()},
        onFailure={false}
    )
}

fun getBookmarkList(
    bookshelfUiState: BookshelfUiState
):MutableList<Book>
{
    return getDataByUiState(
        bookshelfUiState=bookshelfUiState,
        onSuccess={it.bookmarkList},
        onFailure={ mutableListOf()}
    )
}

fun getCurrentItem(
    bookshelfUiState: BookshelfUiState
):BookInfo
{
    return getDataByUiState(
        bookshelfUiState=bookshelfUiState,
        onSuccess={ it.currentItem[it.currentTabType] ?: defaultBookInfo},
        onFailure={ defaultBookInfo}
    )
}

fun getBookList(
    bookshelfUiState: BookshelfUiState
): Flow<PagingData<Book>>
{
    return getDataByUiState(
        bookshelfUiState=bookshelfUiState,
        onSuccess={it.list.book},
        onFailure={ MutableStateFlow(PagingData.from(emptyList())) }
    )
}

fun getTotalItemCount(
    bookshelfUiState: BookshelfUiState
):Int{
    return getDataByUiState(
        bookshelfUiState=bookshelfUiState,
        onSuccess={it.list.totalCount},
        onFailure={0}
    )
}

fun <T> getDataByUiState(
    bookshelfUiState: BookshelfUiState,
    onSuccess :(BookshelfUiState.Success)->T,
    onFailure:()->T
):T{
    return when(bookshelfUiState){
        is BookshelfUiState.Success->onSuccess(bookshelfUiState)
        else->onFailure()
    }
}
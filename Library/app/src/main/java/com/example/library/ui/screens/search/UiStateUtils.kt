package com.example.library.ui.screens.search

import androidx.paging.PagingData
import com.example.library.ui.navigation.NavigationMenuType
import com.example.library.data.api.Book
import com.example.library.data.api.BookInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

fun isBookmarkListEmpty(
    libraryUiState: LibraryUiState
):Boolean
{
    return getDataByUiState(
        libraryUiState=libraryUiState,
        onSuccess={it.bookmarkList.isEmpty()},
        onFailure={false}
    )
}

fun getTotalItemCount(
    libraryUiState: LibraryUiState
):Int{
    return getDataByUiState(
        libraryUiState=libraryUiState,
        onSuccess={it.list.totalCount},
        onFailure={0}
    )
}

fun getTabPressed(
    libraryUiState: LibraryUiState
): NavigationMenuType
{
    return getDataByUiState(
        libraryUiState=libraryUiState,
        onSuccess={it.currentTabType},
        onFailure={ NavigationMenuType.Books}
    )
}

fun getCurrentItem(
    libraryUiState: LibraryUiState
): BookInfo
{
    return getDataByUiState(
        libraryUiState=libraryUiState,
        onSuccess={ it.currentItem[it.currentTabType] ?: defaultBookInfo },
        onFailure={ defaultBookInfo }
    )
}

fun getBookmarkList(
    libraryUiState: LibraryUiState
):MutableList<Book>
{
    return getDataByUiState(
        libraryUiState=libraryUiState,
        onSuccess={it.bookmarkList},
        onFailure={ mutableListOf()}
    )
}

fun getBookList(
    libraryUiState: LibraryUiState
): Flow<PagingData<Book>>
{
    return getDataByUiState(
        libraryUiState=libraryUiState,
        onSuccess={it.list.book},
        onFailure={ MutableStateFlow(PagingData.from(emptyList())) }
    )
}

fun <T> getDataByUiState(
    libraryUiState: LibraryUiState,
    onSuccess :(LibraryUiState.Success)->T,
    onFailure:()->T
):T{
    return when(libraryUiState){
        is LibraryUiState.Success->onSuccess(libraryUiState)
        else->onFailure()
    }
}
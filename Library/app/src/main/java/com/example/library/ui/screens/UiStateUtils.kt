package com.example.library.ui.screens

import com.example.library.ui.common.LibraryUiModel
import com.example.library.ui.screens.search.LibraryUiState

fun getTotalItemCount(
    libraryUiState: LibraryUiState
):Int{
    return getDataByUiState(
        libraryUiState=libraryUiState,
        onSuccess={it.totalCount},
        onFailure={0}
    )
}

fun getLikedList(
    libraryUiState: LibraryUiState
):List<LibraryUiModel>
{
    return getDataByUiState(
        libraryUiState=libraryUiState,
        onSuccess={it.list.filter { item-> item.isLiked }},
        onFailure={ emptyList()}
    )
}

fun getBookList(
    libraryUiState: LibraryUiState
): List<LibraryUiModel>
{
    return getDataByUiState(
        libraryUiState=libraryUiState,
        onSuccess={it.list},
        onFailure={ emptyList() }
    )
}

fun <T> getDataByUiState(
    libraryUiState: LibraryUiState,
    onSuccess :(LibraryUiState.Success)->T,
    onFailure:()->T
):T{
    return when(libraryUiState){
        is LibraryUiState.Success ->onSuccess(libraryUiState)
        else->onFailure()
    }
}
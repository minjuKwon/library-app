package com.example.library.ui.screens.search

import com.example.library.ui.common.LibraryUiModel

fun getTotalItemCount(
    libraryUiState: LibraryUiState
):Int{
    return getDataByUiState(
        libraryUiState=libraryUiState,
        onSuccess={it.totalCount},
        onFailure={0}
    )
}

    libraryUiState: LibraryUiState
{
    return getDataByUiState(
        libraryUiState=libraryUiState,
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
        is LibraryUiState.Success->onSuccess(libraryUiState)
        else->onFailure()
    }
}
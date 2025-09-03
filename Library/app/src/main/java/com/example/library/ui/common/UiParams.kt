package com.example.library.ui.common

import androidx.compose.foundation.lazy.LazyListState
import com.example.library.data.Book
import com.example.library.ui.navigation.destination.LibraryDestination
import com.example.library.ui.navigation.destination.NavigationItemContent
import com.example.library.ui.screens.detail.LibraryDetailsUiState

data class NavigationConfig(
    val contentType: ContentType,
    val navigationType: NavigationType,
    val currentTab: LibraryDestination,
    val navigationItemContentList: List<NavigationItemContent>,
    val onTabPressed: (LibraryDestination) -> Unit
)

data class TextFieldParams(
    val textFieldKeyword:String,
    val updateKeyword:(String)->Unit,
    val onSearch:(String)->Unit
)

data class ListContentParams(
    val scrollState: LazyListState,
    val currentPage:Int,
    val updatePage:(Int)->Unit,
    val updateBackPressedTime:(Long)->Unit,
    val isBackPressedDouble:()->Boolean,
    val onBookmarkPressed:(Book)->Unit,
    val onBookItemPressed: (Book) -> Unit,
    val updateCurrentBook:(Book)->Unit
)

data class DetailsScreenParams(
    val uiState:LibraryDetailsUiState,
    val isDataReadyForUi:Boolean,
    val textFieldKeyword:String,
    val currentBook:Book,
    val updateDataReadyForUi: (Boolean)->Unit,
    val getBookById: (String)-> Book,
)
package com.example.library.ui.common

import androidx.compose.foundation.lazy.LazyListState
import com.example.library.data.entity.Library
import com.example.library.domain.DueCheckResult
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
    val dueCheckResult:DueCheckResult,
    val updatePage:(Int)->Unit,
    val updateBackPressedTime:(Long)->Unit,
    val isBackPressedDouble:()->Boolean,
    val onLikedPressed:(String, Boolean)->Unit,
    val onBookItemPressed: (Library) -> Unit,
    val updateCurrentBook:(Library)->Unit
)

data class DetailsScreenParams(
    val uiState:LibraryDetailsUiState,
    val currentPage:Int,
    val textFieldKeyword:String,
    val isShowOverdueDialog:Boolean,
    val currentBook: Library,
    val loanLibrary: () -> Unit,
    val getBookStatus:()->Unit,
    val updateOverdueDialog:(Boolean) -> Unit
)
package com.example.library.ui.utils

import androidx.compose.foundation.lazy.LazyListState
import com.example.library.data.api.Book
import com.example.library.data.api.BookInfo
import com.example.library.ui.navigation.NavigationMenuType

data class NavigationConfig(
    val contentType: ContentType,
    val navigationType: NavigationType,
    val onTabPressed: (NavigationMenuType) -> Unit
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
    val onBookmarkPressed:(Book)->Unit,
    val onBookItemPressed: (BookInfo) -> Unit,
    val initCurrentItem:(NavigationMenuType, BookInfo)->Unit,
)

data class DetailsScreenParams(
    val isDataReadyForUi:Boolean,
    val updateDataReadyForUi: (Boolean)->Unit,
    val onBackPressed:(BookInfo)->Unit
)
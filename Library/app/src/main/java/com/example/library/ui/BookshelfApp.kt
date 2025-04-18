package com.example.library.ui

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.library.data.BookType
import com.example.library.network.Book
import com.example.library.network.BookInfo
import com.example.library.ui.screens.BookshelfHomeScreen
import com.example.library.ui.utils.ContentType
import com.example.library.ui.utils.NavigationType

@Composable
fun BookshelfApp(
    windowSize: WindowWidthSizeClass,
    modifier:Modifier= Modifier
){
    val bookshelfViewModel: BookshelfViewModel = viewModel(factory=BookshelfViewModel.Factory)
    val navigationType:NavigationType
    val contentType:ContentType

    val currentPage by bookshelfViewModel.currentPage.collectAsState()
    val scrollState  = rememberLazyListState()

    val currentOrder by bookshelfViewModel.currentOrder.collectAsState()
    val textFieldKeyword by bookshelfViewModel.textFieldKeyword

    when(windowSize){
        WindowWidthSizeClass.Compact->{
            navigationType=NavigationType.BOTTOM_NAVIGATION
            contentType=ContentType.LIST_ONLY
        }
        WindowWidthSizeClass.Medium->{
            navigationType=NavigationType.NAVIGATION_RAIL
            contentType=ContentType.LIST_ONLY
        }
        WindowWidthSizeClass.Expanded->{
            navigationType=NavigationType.PERMANENT_NAVIGATION_DRAWER
            contentType=ContentType.LIST_AND_DETAIL
        }
        else->{
            navigationType=NavigationType.BOTTOM_NAVIGATION
            contentType=ContentType.LIST_ONLY
        }
    }

    BookshelfHomeScreen(
        bookshelfUiState=bookshelfViewModel.bookshelfUiState,
        navigationConfig = NavigationConfig(
            contentType=contentType,
            navigationType = navigationType,
            onTabPressed = { bookshelfViewModel.updateCurrentBookTabType(it) }
        ),
        textFieldParams = TextFieldParams(
            textFieldKeyword=textFieldKeyword,
            updateKeyword={bookshelfViewModel.updateKeyword(it)},
            onSearch = { bookshelfViewModel.getInformation(it)}
        ),
        listContentParams = ListContentParams(
            scrollState=scrollState,
            currentPage=currentPage,
            updatePage={bookshelfViewModel.getInformation(page=it)},
            onBookmarkPressed={bookshelfViewModel.updateBookmarkList(it)},
            onBookItemPressed={
                bookshelfViewModel.updateOrder(true)
                bookshelfViewModel.updateDetailsScreenState(it)},
            initCurrentItem={v1,v2->
                bookshelfViewModel.initCurrentItem(v1,v2)}
        ),
        detailsScreenParams = DetailsScreenParams(
            currentOrder=currentOrder,
            updateOrder={bookshelfViewModel.updateOrder(it)},
            onBackPressed={bookshelfViewModel.resetHomeScreenState(it)}
        ),
        modifier=modifier
    )
}

data class NavigationConfig(
    val contentType: ContentType,
    val navigationType: NavigationType,
    val onTabPressed: (BookType) -> Unit
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
    val initCurrentItem:(BookType,BookInfo)->Unit,
)

data class DetailsScreenParams(
    val currentOrder:Boolean,
    val updateOrder: (Boolean)->Unit,
    val onBackPressed:(BookInfo)->Unit
)
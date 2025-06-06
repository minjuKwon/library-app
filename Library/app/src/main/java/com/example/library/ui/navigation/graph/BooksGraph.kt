package com.example.library.ui.navigation.graph

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.library.R
import com.example.library.ui.navigation.LibraryDestination
import com.example.library.ui.screens.search.ErrorScreen
import com.example.library.ui.screens.search.LibraryListAndDetailContent
import com.example.library.ui.screens.search.LibraryListOnlyContent
import com.example.library.ui.screens.search.LibraryUiState
import com.example.library.ui.screens.search.LoadingScreen
import com.example.library.ui.screens.search.getBookList
import com.example.library.ui.utils.ContentType
import com.example.library.ui.utils.DetailsScreenParams
import com.example.library.ui.utils.ListContentParams
import com.example.library.ui.utils.NavigationConfig
import com.example.library.ui.utils.TextFieldParams

fun NavGraphBuilder.booksDestination(
    libraryUiState: LibraryUiState,
    navigationConfig: NavigationConfig,
    textFieldParams: TextFieldParams,
    listContentParams: ListContentParams,
    detailsScreenParams: DetailsScreenParams,
){
    composable(route= LibraryDestination.Books.route){
        if(navigationConfig.contentType== ContentType.LIST_AND_DETAIL){
            LibraryListAndDetailContent(
                libraryUiState = libraryUiState,
                books = getBookList(libraryUiState).collectAsLazyPagingItems() ,
                listContentParams=listContentParams,
                textFieldParams=textFieldParams,
                detailsScreenParams=detailsScreenParams,
            )
        }else{
            Column {
                when(libraryUiState){
                    is LibraryUiState.Success -> LibraryListOnlyContent(
                        libraryUiState=libraryUiState,
                        books=libraryUiState.list.book.collectAsLazyPagingItems(),
                        textFieldParams=textFieldParams,
                        listContentParams=listContentParams,
                        modifier= Modifier
                            .padding(dimensionResource(R.dimen.padding_sm))
                            .fillMaxSize()

                    )
                    is LibraryUiState.Loading -> {
                        LoadingScreen(modifier= Modifier.fillMaxSize())
                    }
                    is LibraryUiState.Error -> {
                        ErrorScreen(
                            input=textFieldParams.textFieldKeyword,
                            retryAction = textFieldParams.onSearch,
                            modifier= Modifier.fillMaxSize()
                        )
                    }
                }
            }
        }
    }
}
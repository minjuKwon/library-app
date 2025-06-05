package com.example.library.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.library.R
import com.example.library.ui.screens.ranking.LibraryRankingScreen
import com.example.library.ui.screens.search.ErrorScreen
import com.example.library.ui.screens.search.LibraryListAndDetailContent
import com.example.library.ui.screens.search.LibraryListOnlyContent
import com.example.library.ui.screens.search.LibraryUiState
import com.example.library.ui.screens.search.LoadingScreen
import com.example.library.ui.screens.search.getBookList
import com.example.library.ui.screens.user.LibraryUserScreen
import com.example.library.ui.screens.user.NonMemberScreen
import com.example.library.ui.utils.ContentType
import com.example.library.ui.utils.DetailsScreenParams
import com.example.library.ui.utils.ListContentParams
import com.example.library.ui.utils.NavigationConfig
import com.example.library.ui.utils.TextFieldParams

@Composable
fun NavigationGraph(
    navController: NavHostController,
    libraryUiState: LibraryUiState,
    navigationConfig: NavigationConfig,
    textFieldParams: TextFieldParams,
    listContentParams: ListContentParams,
    detailsScreenParams: DetailsScreenParams,
    modifier:Modifier=Modifier
){
    val isLogIn=true

    NavHost(
        navController= navController,
        startDestination =LibraryDestination.Books.route,
        modifier=modifier
    ){
        composable(route=LibraryDestination.Books.route){
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
        composable(route=LibraryDestination.Ranking.route){
            LibraryRankingScreen()
        }
        composable(route=LibraryDestination.Setting.route){
            if(isLogIn){
                LibraryUserScreen()
            }else{
                NonMemberScreen()
            }
        }
    }
}
package com.example.library.ui.navigation.graph

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.library.R
import com.example.library.ui.navigation.destination.GraphRoutes
import com.example.library.ui.navigation.destination.LibraryDestination
import com.example.library.ui.navigation.destination.LibraryDestination.Details.routeWithArgs
import com.example.library.ui.navigation.navigateSingle
import com.example.library.ui.screens.detail.LibraryDetailsScreen
import com.example.library.ui.screens.search.ErrorScreen
import com.example.library.ui.screens.search.LibraryListAndDetailContent
import com.example.library.ui.screens.search.LibraryListOnlyContent
import com.example.library.ui.screens.search.LibraryUiState
import com.example.library.ui.screens.search.LoadingScreen
import com.example.library.ui.screens.search.getBookList
import com.example.library.ui.common.ContentType
import com.example.library.ui.common.DetailsScreenParams
import com.example.library.ui.common.ListContentParams
import com.example.library.ui.common.NavigationConfig
import com.example.library.ui.common.TextFieldParams

fun NavGraphBuilder.booksDestination(
    libraryUiState: LibraryUiState,
    navController: NavHostController,
    navigationConfig: NavigationConfig,
    textFieldParams: TextFieldParams,
    listContentParams: ListContentParams,
    detailsScreenParams: DetailsScreenParams,
){
    navigation(
        startDestination = LibraryDestination.Books.route,
        route=GraphRoutes.HOME
    ){
        composable(route= LibraryDestination.Books.route){
            if(navigationConfig.contentType== ContentType.LIST_AND_DETAIL){
                LibraryListAndDetailContent(
                    libraryUiState = libraryUiState,
                    isAtRoot=navController.previousBackStackEntry == null,
                    list = getBookList(libraryUiState) ,
                    listContentParams=listContentParams,
                    textFieldParams=textFieldParams,
                    detailsScreenParams=detailsScreenParams,
                    onNavigateToDetails={ itemId->
                        navController.navigateSingle("${LibraryDestination.Details.route}/$itemId")
                    }
                )
            }else{
                Column {
                    when(libraryUiState){
                        is LibraryUiState.Success -> LibraryListOnlyContent(
                            libraryUiState=libraryUiState,
                            list=libraryUiState.list,
                            textFieldParams=textFieldParams,
                            listContentParams=listContentParams,
                            isAtRoot= navController.previousBackStackEntry == null,
                            isNotFullScreen = true,
                            onNavigateToDetails={
                                navController
                                    .navigateSingle("${LibraryDestination.Details.route}/$it")
                            },
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

        composable(
            route=routeWithArgs,
            arguments= LibraryDestination.Details.arguments
        ){ navBackStackEntry->
            val id= navBackStackEntry.arguments?.getString(LibraryDestination.Details.BOOK_ID_ARGS)

            if (id != null) {
                LibraryDetailsScreen(
                    detailsScreenParams= detailsScreenParams,
                    onBackPressed={navController.popBackStack()}
                )
            }
        }
    }
}
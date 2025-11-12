package com.example.library.ui.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.library.ui.navigation.destination.GraphRoutes
import com.example.library.ui.screens.search.LibraryUiState
import com.example.library.ui.screens.user.UserViewModel
import com.example.library.ui.common.DetailsScreenParams
import com.example.library.ui.common.ListContentParams
import com.example.library.ui.common.NavigationConfig
import com.example.library.ui.common.TextFieldParams

@Composable
fun NavigationGraph(
    navController: NavHostController,
    libraryUiState: LibraryUiState,
    navigationConfig: NavigationConfig,
    textFieldParams: TextFieldParams,
    listContentParams: ListContentParams,
    detailsScreenParams: DetailsScreenParams,
    userViewModel: UserViewModel,
    resetLibraryList:()->Unit,
    resetLiked:()->Unit,
    modifier:Modifier=Modifier
){
    NavHost(
        navController= navController,
        startDestination = GraphRoutes.HOME,
        modifier=modifier
    ){
        booksDestination(
            libraryUiState,
            navController,
            navigationConfig,
            textFieldParams,
            listContentParams,
            detailsScreenParams
        )
        rankingDestination(listContentParams)
        settingDestination(
            navController,
            userViewModel,
            resetLibraryList,
            resetLiked
        )
    }
}
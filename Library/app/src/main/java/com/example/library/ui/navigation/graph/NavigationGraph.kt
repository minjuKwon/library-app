package com.example.library.ui.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.library.ui.navigation.LibraryDestination
import com.example.library.ui.screens.search.LibraryUiState
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
        startDestination = LibraryDestination.Books.route,
        modifier=modifier
    ){
        booksDestination(
            libraryUiState,
            navigationConfig,
            textFieldParams,
            listContentParams,
            detailsScreenParams
        )
        rankingDestination()
        settingDestination(isLogIn)
    }
}
package com.example.library.ui.navigation.graph

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.library.ui.navigation.destination.GraphRoutes
import com.example.library.ui.common.DetailsScreenParams
import com.example.library.ui.common.ListContentParams
import com.example.library.ui.common.NavigationConfig
import com.example.library.ui.common.TextFieldParams
import com.example.library.ui.common.UserScreenParams

@Composable
fun NavigationGraph(
    navController: NavHostController,
    navigationConfig: NavigationConfig,
    textFieldParams: TextFieldParams,
    listContentParams: ListContentParams,
    detailsScreenParams: DetailsScreenParams,
    userScreenParams:UserScreenParams,
    modifier:Modifier=Modifier
){
    NavHost(
        navController= navController,
        startDestination = GraphRoutes.HOME,
        modifier=modifier
    ){
        booksDestination(
            navController,
            navigationConfig,
            textFieldParams,
            listContentParams,
            detailsScreenParams,
            userScreenParams
        )
        //rankingDestination(listContentParams)
        settingDestination(
            navController,
            listContentParams,
            userScreenParams
        )
    }
}
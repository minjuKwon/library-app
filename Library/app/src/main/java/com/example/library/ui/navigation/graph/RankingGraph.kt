package com.example.library.ui.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.library.ui.navigation.destination.LibraryDestination
import com.example.library.ui.screens.ranking.LibraryRankingScreen
import com.example.library.ui.common.ListContentParams

fun NavGraphBuilder.rankingDestination(
    listContentParams: ListContentParams
){
    composable(route= LibraryDestination.Ranking.route){
        LibraryRankingScreen(listContentParams)
    }
}
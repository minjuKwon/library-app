package com.example.library.ui.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.library.ui.navigation.destination.LibraryDestination
import com.example.library.ui.screens.ranking.LibraryRankingScreen

fun NavGraphBuilder.rankingDestination(){
    composable(route= LibraryDestination.Ranking.route){
        LibraryRankingScreen()
    }
}
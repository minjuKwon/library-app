package com.example.library.ui.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.library.ui.navigation.destination.LibraryDestination
import com.example.library.ui.screens.user.LibraryUserScreen
import com.example.library.ui.screens.user.NonMemberScreen

fun NavGraphBuilder.settingDestination(
    isLogIn:Boolean
){
    composable(route= LibraryDestination.Setting.route){
        if(isLogIn){
            LibraryUserScreen()
        }else{
            NonMemberScreen()
        }
    }
}
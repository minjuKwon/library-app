package com.example.library.ui.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.library.ui.navigation.destination.LibraryDestination

fun NavHostController.navigateSingleTopTo(route:String)=
    this.navigate(route){
        popUpTo(this@navigateSingleTopTo.graph.findStartDestination().id)
        launchSingleTop=true
    }

fun NavHostController.navigateToSetting()=
    this.navigate(LibraryDestination.Setting.route){
        popUpTo(LibraryDestination.Setting.route){
            inclusive=true
        }
    }
package com.example.library.ui.navigation

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController

fun NavHostController.navigateSingleTopTo(route:String)=
    this.navigate(route){
        popUpTo(this@navigateSingleTopTo.graph.findStartDestination().id)
        launchSingleTop=true
    }
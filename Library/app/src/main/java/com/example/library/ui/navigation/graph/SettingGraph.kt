package com.example.library.ui.navigation.graph

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.library.ui.navigation.destination.LibraryDestination
import com.example.library.ui.navigation.destination.UserRoutes
import com.example.library.ui.navigation.navigateSingle
import com.example.library.ui.navigation.navigateToSetting
import com.example.library.ui.screens.user.LibraryUserScreen
import com.example.library.ui.screens.user.LoanHistoryScreen
import com.example.library.ui.screens.user.LoanStatusScreen
import com.example.library.ui.screens.user.LogInScreen
import com.example.library.ui.screens.user.NonMemberScreen
import com.example.library.ui.screens.user.RegisterScreen
import com.example.library.ui.screens.user.ReservationStatusScreen
import com.example.library.ui.screens.user.UserInformationEditScreen
import com.example.library.ui.screens.user.UserViewModel

fun NavGraphBuilder.settingDestination(
    navController: NavHostController,
    userViewModel: UserViewModel
){
    navigation(
        startDestination = LibraryDestination.Setting.route,
        route= UserRoutes.USER
    ){
        composable(route= LibraryDestination.Setting.route){
            val isLogIn by userViewModel.isLogIn.collectAsState()
            if(isLogIn){
                LibraryUserScreen(
                    userViewModel=userViewModel,
                    onNavigationToEdit={
                        navController.navigateSingle(LibraryDestination.UserEdit.route)
                    },
                    onNavigationToSetting={
                        navController.navigateToSetting()
                    },
                    onNavigationToLoanHistory={
                        navController.navigateSingle(LibraryDestination.LoanHistory.route)
                    },
                    onNavigationToLoanStatus={
                        navController.navigateSingle(LibraryDestination.LoanStatus.route)
                    },
                    onNavigationToReservation={
                        navController.navigateSingle(LibraryDestination.ReservationStatus.route)
                    }
                )
            }else{
                NonMemberScreen(
                    onNavigationToLogIn =
                    {navController.navigateSingle(LibraryDestination.LogIn.route)},
                    onNavigationToRegister =
                    {navController.navigateSingle(LibraryDestination.Register.route)}
                )
            }
        }

        composable(route= LibraryDestination.LogIn.route) {
            LogInScreen(
                userViewModel= userViewModel,
                onBackPressed = {navController.popBackStack()},
                onNavigationToSetting={
                    navController.navigateToSetting()
                }
            )
        }

        composable(route= LibraryDestination.Register.route) {
            RegisterScreen(
                userViewModel=userViewModel,
                onBackPressed = {navController.popBackStack()},
                onNavigationToLogIn={
                    navController.navigate(LibraryDestination.LogIn.route){
                        popUpTo(LibraryDestination.Setting.route)
                        launchSingleTop=true
                    }
                }
            )
        }

        composable(route=LibraryDestination.UserEdit.route){
            UserInformationEditScreen(
                onBackPressed = {navController.popBackStack()}
            )
        }

        composable(route=LibraryDestination.LoanHistory.route){
            LoanHistoryScreen(
                onBackPressed = {navController.popBackStack()}
            )
        }

        composable(route=LibraryDestination.LoanStatus.route){
            LoanStatusScreen(
                onBackPressed = {navController.popBackStack()}
            )
        }

        composable(route=LibraryDestination.ReservationStatus.route){
            ReservationStatusScreen(
                onBackPressed = {navController.popBackStack()}
            )
        }
    }
}
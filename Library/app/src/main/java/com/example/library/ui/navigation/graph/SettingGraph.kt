package com.example.library.ui.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.library.ui.navigation.destination.LibraryDestination
import com.example.library.ui.navigation.destination.UserRoutes
import com.example.library.ui.navigation.navigateToSetting
import com.example.library.ui.screens.user.LibraryUserScreen
import com.example.library.ui.screens.user.LoanHistoryScreen
import com.example.library.ui.screens.user.LoanStatusScreen
import com.example.library.ui.screens.user.LogInScreen
import com.example.library.ui.screens.user.NonMemberScreen
import com.example.library.ui.screens.user.RegisterScreen
import com.example.library.ui.screens.user.ReservationStatusScreen
import com.example.library.ui.screens.user.UserInformationEditScreen

fun NavGraphBuilder.settingDestination(
    navController: NavHostController,
    isLogIn:Boolean,
    onLoggedInChange:(Boolean)->Unit
){
    navigation(
        startDestination = LibraryDestination.Setting.route,
        route= UserRoutes.USER
    ){
        composable(route= LibraryDestination.Setting.route){
            if(isLogIn){
                LibraryUserScreen(
                    onNavigationToEdit={
                        navController.navigate(LibraryDestination.UserEdit.route)
                    },
                    onLogOut={
                        onLoggedInChange(false)
                        navController.navigateToSetting()
                    },
                    onNavigationToLoanHistory={
                        navController.navigate(LibraryDestination.LoanHistory.route)
                    },
                    onNavigationToLoanStatus={
                        navController.navigate(LibraryDestination.LoanStatus.route)
                    },
                    onNavigationToReservation={
                        navController.navigate(LibraryDestination.ReservationStatus.route)
                    },
                    onUnregister={
                        onLoggedInChange(false)
                        navController.navigateToSetting()
                    }
                )
            }else{
                NonMemberScreen(
                    onNavigationToLogIn =
                    {navController.navigate(LibraryDestination.LogIn.route)},
                    onNavigationToRegister =
                    {navController.navigate(LibraryDestination.Register.route)}
                )
            }
        }

        composable(route= LibraryDestination.LogIn.route) {
            LogInScreen(
                onLoggedInChange=onLoggedInChange,
                onBackPressed = {navController.popBackStack()},
                onNavigationToSetting={
                    navController.navigateToSetting()
                }
            )
        }

        composable(route= LibraryDestination.Register.route) {
            RegisterScreen(
                onBackPressed = {navController.popBackStack()},
                onNavigationToLogIn={
                    navController.navigate(LibraryDestination.LogIn.route){
                        popUpTo(LibraryDestination.Setting.route)
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
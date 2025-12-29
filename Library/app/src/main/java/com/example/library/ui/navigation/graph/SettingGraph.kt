package com.example.library.ui.navigation.graph

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.library.ui.common.ListContentParams
import com.example.library.ui.common.UserScreenParams
import com.example.library.ui.navigation.destination.LibraryDestination
import com.example.library.ui.navigation.destination.UserRoutes
import com.example.library.ui.navigation.navigateSingle
import com.example.library.ui.navigation.navigateToSetting
import com.example.library.ui.screens.getLikedList
import com.example.library.ui.screens.user.LibraryUserScreen
import com.example.library.ui.screens.user.LikedListScreen
import com.example.library.ui.screens.user.LoanHistoryScreen
import com.example.library.ui.screens.user.LoanStatusScreen
import com.example.library.ui.screens.user.LogInScreen
import com.example.library.ui.screens.user.NonMemberScreen
import com.example.library.ui.screens.user.NotVerificationScreen
import com.example.library.ui.screens.user.RegisterScreen
import com.example.library.ui.screens.user.ReservationStatusScreen
import com.example.library.ui.screens.user.UserInformationEditScreen

fun NavGraphBuilder.settingDestination(
    navController: NavHostController,
    listContentParams: ListContentParams,
    userScreenParams: UserScreenParams
){
    navigation(
        startDestination = LibraryDestination.Setting.route,
        route= UserRoutes.USER
    ){
        composable(route= LibraryDestination.Setting.route){
            if(userScreenParams.isLogIn){
                LibraryUserScreen(
                    userScreenParams= userScreenParams,
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
                    },
                    onNavigationToLiked = {
                        navController.navigateSingle(LibraryDestination.LikedList.route)
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
            if(userScreenParams.isUserVerified){
                LogInScreen(
                    userScreenParams= userScreenParams,
                    onBackPressed = {navController.popBackStack()},
                    onNavigationToSetting={
                        navController.navigateToSetting()
                        userScreenParams.checkUserVerified()
                    }
                )
            }else{
                NotVerificationScreen(userScreenParams)
            }
        }

        composable(route= LibraryDestination.Register.route) {
            RegisterScreen(
                userScreenParams=userScreenParams,
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
                userScreenParams=userScreenParams,
                onBackPressed = {navController.popBackStack()},
                onNavigationToSetting={
                    navController.navigateToSetting()
                }
            )
        }

        composable(route=LibraryDestination.LoanHistory.route){
            LoanHistoryScreen(
                list= userScreenParams.loanHistoryList,
                onBackPressed = {navController.popBackStack()}
            )
        }

        composable(route=LibraryDestination.LoanStatus.route){
            LoanStatusScreen(
                loanList = userScreenParams.loanBookList,
                overdueList= userScreenParams.overdueList,
                suspensionDate=userScreenParams.suspensionDate,
                onBackPressed = {navController.popBackStack()}
            )
        }

        composable(route=LibraryDestination.ReservationStatus.route){
            ReservationStatusScreen(
                reservationList=userScreenParams.reservationList,
                onBackPressed = {navController.popBackStack()}
            )
        }

        composable(route=LibraryDestination.LikedList.route){
            LikedListScreen(
                list= getLikedList(listContentParams.libraryUiState),
                listContentParams=listContentParams,
                onBackPressed = {navController.popBackStack()},
                onNavigateToDetails={
                    navController
                        .navigateSingle("${LibraryDestination.Details.route}/$it")
                }
            )
        }

    }
}
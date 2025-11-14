package com.example.library.ui.navigation.graph

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.library.ui.common.ListContentParams
import com.example.library.ui.navigation.destination.LibraryDestination
import com.example.library.ui.navigation.destination.UserRoutes
import com.example.library.ui.navigation.navigateSingle
import com.example.library.ui.navigation.navigateToSetting
import com.example.library.ui.screens.search.LibraryUiState
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
import com.example.library.ui.screens.user.UserViewModel

fun NavGraphBuilder.settingDestination(
    libraryUiState:LibraryUiState,
    navController: NavHostController,
    userViewModel: UserViewModel,
    listContentParams: ListContentParams,
    resetLibraryList:()->Unit,
    resetLiked:()->Unit
){
    navigation(
        startDestination = LibraryDestination.Setting.route,
        route= UserRoutes.USER
    ){
        composable(route= LibraryDestination.Setting.route){
            val isLogIn by userViewModel.isLogIn.collectAsState()
            val userInfo by userViewModel.userPreferences.collectAsState()
            if(isLogIn){
                LibraryUserScreen(
                    userViewModel=userViewModel,
                    userInfo= userInfo,
                    resetLiked= resetLiked,
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
            val isUserVerified by userViewModel.isUserVerified
            val isClickEmailLink by userViewModel.isClickEmailLink.collectAsState()

            if(isUserVerified){
                LogInScreen(
                    userViewModel= userViewModel,
                    isClickEmailLink=isClickEmailLink,
                    resetLibraryList=resetLibraryList,
                    onBackPressed = {navController.popBackStack()},
                    onNavigationToSetting={
                        navController.navigateToSetting()
                        userViewModel.checkUserVerified()
                    }
                )
            }else{
                NotVerificationScreen(userViewModel= userViewModel)
            }
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
            val userInfo by userViewModel.userPreferences.collectAsState()
            UserInformationEditScreen(
                userViewModel=userViewModel,
                userInfo= userInfo,
                onBackPressed = {navController.popBackStack()},
                onNavigationToSetting={
                    navController.navigateToSetting()
                }
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

        composable(route=LibraryDestination.LikedList.route){
            LikedListScreen(
                list= getLikedList(libraryUiState),
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
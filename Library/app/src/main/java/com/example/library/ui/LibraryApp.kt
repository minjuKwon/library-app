package com.example.library.ui

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.library.ui.navigation.destination.LibraryDestination
import com.example.library.ui.navigation.navigateSingleTopTo
import com.example.library.ui.navigation.destination.navigationItemContentList
import com.example.library.ui.screens.detail.LibraryDetailsViewModel
import com.example.library.ui.screens.search.LibraryViewModel
import com.example.library.ui.screens.user.UserViewModel
import com.example.library.ui.common.ContentType
import com.example.library.ui.common.DetailsScreenParams
import com.example.library.ui.common.ListContentParams
import com.example.library.ui.common.NavigationConfig
import com.example.library.ui.common.NavigationType
import com.example.library.ui.common.TextFieldParams
import com.example.library.ui.common.UserScreenParams

@Composable
fun LibraryApp(
    windowSize: WindowWidthSizeClass,
    libraryViewModel: LibraryViewModel,
    libraryDetailsViewModel: LibraryDetailsViewModel,
    userViewModel: UserViewModel,
    modifier:Modifier= Modifier
){
    val scrollState  = rememberLazyListState()

    val navController= rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination= currentBackStack?.destination
    val currentTab = navigationItemContentList.find {
        currentDestination?.route?.startsWith(it.navigationMenuType.route) == true
        }?.navigationMenuType?: LibraryDestination.Books

    val libraryUiState by libraryViewModel.libraryUiState.collectAsState()
    val textFieldKeyword by libraryViewModel.textFieldKeyword.collectAsState()
    val dueCheckResult by libraryViewModel.dueCheckResult.collectAsState()
    val reservedBookList by libraryViewModel.reservedBookList.collectAsState()
    val currentPage by libraryViewModel.currentPage.collectAsState()

    val detailsUiState by libraryDetailsViewModel.uiState.collectAsState()
    val isShowOverdueDialog by libraryDetailsViewModel.isShowOverdueDialog.collectAsState()
    val isShowSuspensionDialog by libraryDetailsViewModel.isShowSuspensionDateDialog.collectAsState()
    val isShowReservationDialog by libraryDetailsViewModel.isShowReservationDialog.collectAsState()
    val currentBook by libraryDetailsViewModel.currentLibrary.collectAsState()
    val reservationStatus by libraryDetailsViewModel.reservationStatus.collectAsState()
    val reservationCount by libraryDetailsViewModel.reservationCount.collectAsState()
    val userPreferences by libraryDetailsViewModel.userPreferences.collectAsState()

    val userUiState = userViewModel.event
    val isLogIn by userViewModel.isLogIn.collectAsState()
    val isUserVerified by userViewModel.isUserVerified.collectAsState()
    val isClickEmailLink by userViewModel.isClickEmailLink.collectAsState()
    val isPasswordVerified by userViewModel.isPasswordVerified.collectAsState()
    val suspensionDate by userViewModel.suspensionEnd.collectAsState()
    val userInfo by userViewModel.userPreferences.collectAsState()
    val loanHistoryList by userViewModel.userLoanHistoryList.collectAsState()
    val loanBookList by userViewModel.userLoanBookList.collectAsState()
    val overdueList by userViewModel.userOverdueBookList.collectAsState()
    val reservationList by userViewModel.userReservationList.collectAsState()

    val navigationType:NavigationType
    val contentType:ContentType
    when(windowSize){
        WindowWidthSizeClass.Compact->{
            navigationType=NavigationType.BOTTOM_NAVIGATION
            contentType=ContentType.LIST_ONLY
        }
        WindowWidthSizeClass.Medium->{
            navigationType=NavigationType.NAVIGATION_RAIL
            contentType=ContentType.LIST_ONLY
        }
        WindowWidthSizeClass.Expanded->{
            navigationType=NavigationType.PERMANENT_NAVIGATION_DRAWER
            contentType=ContentType.LIST_AND_DETAIL
        }
        else->{
            navigationType=NavigationType.BOTTOM_NAVIGATION
            contentType=ContentType.LIST_ONLY
        }
    }
    LibraryAppContent(
        navController=navController,
        navigationConfig = NavigationConfig(
            contentType=contentType,
            navigationType = navigationType,
            currentTab= currentTab,
            navigationItemContentList= navigationItemContentList,
            onTabPressed = {type->
                navController.navigateSingleTopTo(type.route)
            }
        ),
        textFieldParams = TextFieldParams(
            textFieldKeyword=textFieldKeyword,
            updateKeyword={ libraryViewModel.updateKeyword(it) },
            onSearch = {
                libraryViewModel.getInformation(it)
                libraryViewModel.getItem()
            }
        ),
        listContentParams = ListContentParams(
            libraryUiState=libraryUiState,
            scrollState=scrollState,
            currentPage=currentPage,
            dueCheckResult= dueCheckResult,
            reservedBookList=reservedBookList,
            updatePage={
                libraryViewModel.getInformation(page=it)
                libraryViewModel.getItem()
            },
            updateBackPressedTime = {libraryViewModel.updateBackPressedTime(it)},
            isBackPressedDouble={libraryViewModel.isBackPressedDouble()},
            getBookStatus = {
                libraryViewModel.getReservationCount()
                libraryViewModel.getBookStatus()
            },
            onLikedPressed={ id, isLiked ->
                libraryViewModel.toggleLike(id, isLiked)
            },
            onBookItemPressed={ libraryDetailsViewModel.updateCurrentItem(it) },
            updateCurrentBook={ libraryDetailsViewModel.updateCurrentItem(it) }
        ),
        detailsScreenParams = DetailsScreenParams(
            uiState= detailsUiState,
            currentPage = currentPage,
            userId = userPreferences?.uid?:"",
            reservationCount = reservationCount,
            textFieldKeyword = textFieldKeyword,
            reservationStatus=reservationStatus,
            isShowOverdueDialog=isShowOverdueDialog,
            isShowSuspensionDialog=isShowSuspensionDialog,
            isShowReservationDialog = isShowReservationDialog,
            currentBook= currentBook,
            loanLibrary = {
                libraryDetailsViewModel.loanLibrary(textFieldKeyword, currentPage.toString())
            },
            getReservedStatus = {libraryDetailsViewModel.getReservedStatus()},
            getBookStatus = { libraryDetailsViewModel.getBookStatus(it) },
            getReservationCount = {libraryDetailsViewModel.getReservationCount(it)},
            updateOverdueDialog={libraryDetailsViewModel.updateOverdueDialog(it)},
            updateSuspensionDialog={libraryDetailsViewModel.updateSuspensionDialog(it)},
            updateReservationDialog = {libraryDetailsViewModel.updateReservationDialog(it)}
        ),
        userScreenParams= UserScreenParams(
            uiState = userUiState,
            isLogIn = isLogIn,
            isUserVerified= isUserVerified,
            isClickEmailLink = isClickEmailLink,
            isPasswordVerified=isPasswordVerified,
            suspensionDate = suspensionDate,
            userInfo = userInfo,
            loanHistoryList = loanHistoryList,
            loanBookList=loanBookList,
            overdueList=overdueList,
            reservationList=reservationList,
            signOut = {userViewModel.signOut()},
            checkUserVerified={userViewModel.checkUserVerified()},
            getUserLoanHistoryList={userViewModel.getUserLoanHistoryList()},
            getUserLoanBookList={userViewModel.getUserLoanBookList()},
            getReservationList={userViewModel.getReservationList()},
            sendVerificationEmail={userViewModel.sendVerificationEmail()},
            resetLibraryList={libraryViewModel.getLiked()},
            resetBookStatus = {libraryViewModel.getBookStatus()},
            resetUserBookStatus={
                libraryViewModel.getBookStatus()
                libraryViewModel.resetLiked()
                libraryDetailsViewModel.updateOverdueDialog(false)
                libraryDetailsViewModel.updateSuspensionDialog(false)
                libraryDetailsViewModel.updateReservationDialog(false)
            },
            updateLogInState={userViewModel.updateLogInState(it)},
            updatePasswordVerifiedState={userViewModel.updatePasswordVerifiedState(it)},
            updateEmailVerifiedState={userViewModel.updateEmailVerifiedState(it)},
            unregister = {userViewModel.unregister(it)},
            findPassword = {userViewModel.findPassword(it)},
            verifyCurrentPassword = {userViewModel.verifyCurrentPassword(it)},
            changePassword = {userViewModel.changePassword(it)},
            changeUserInfo={userViewModel.changeUserInfo(it)},
            register = { user, password ->
                userViewModel.register(user, password)
            },
            signIn = { email, password ->
                userViewModel.signIn(email, password)
            }
        ),
        modifier=modifier
    )
}
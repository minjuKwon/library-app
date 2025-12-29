package com.example.library.ui.common

import androidx.compose.foundation.lazy.LazyListState
import com.example.library.data.entity.Library
import com.example.library.data.entity.User
import com.example.library.domain.DueCheckResult
import com.example.library.ui.navigation.destination.LibraryDestination
import com.example.library.ui.navigation.destination.NavigationItemContent
import com.example.library.ui.screens.detail.LibraryDetailsUiState
import com.example.library.ui.screens.search.LibraryUiState
import com.example.library.ui.screens.user.UserUiState
import kotlinx.coroutines.flow.SharedFlow

data class NavigationConfig(
    val contentType: ContentType,
    val navigationType: NavigationType,
    val currentTab: LibraryDestination,
    val navigationItemContentList: List<NavigationItemContent>,
    val onTabPressed: (LibraryDestination) -> Unit
)

data class TextFieldParams(
    val textFieldKeyword:String,
    val updateKeyword:(String)->Unit,
    val onSearch:(String)->Unit
)

data class ListContentParams(
    val libraryUiState: LibraryUiState,
    val scrollState: LazyListState,
    val currentPage:Int,
    val dueCheckResult:DueCheckResult,
    val reservedBookList:List<String>,
    val updatePage:(Int)->Unit,
    val updateBackPressedTime:(Long)->Unit,
    val isBackPressedDouble:()->Boolean,
    val getBookStatus:()->Unit,
    val onLikedPressed:(String, Boolean)->Unit,
    val onBookItemPressed: (Library) -> Unit,
    val updateCurrentBook:(Library)->Unit
)

data class DetailsScreenParams(
    val uiState:LibraryDetailsUiState,
    val currentPage:Int,
    val userId:String,
    val textFieldKeyword:String,
    val reservationStatus:String,
    val isShowOverdueDialog:Boolean,
    val isShowSuspensionDialog:Boolean,
    val isShowReservationDialog:Boolean,
    val currentBook: Library,
    val reservationCount:Map<String, Int>,
    val loanLibrary: () -> Unit,
    val getReservedStatus:()->Unit,
    val getBookStatus:(Boolean)->Unit,
    val getReservationCount:(Boolean)->Unit,
    val updateOverdueDialog:(Boolean) -> Unit,
    val updateSuspensionDialog:(Boolean) -> Unit,
    val updateReservationDialog:(Boolean) -> Unit
)

data class UserScreenParams(
    val uiState: SharedFlow<UserUiState>,
    val isLogIn:Boolean,
    val isUserVerified:Boolean,
    val isClickEmailLink:Boolean,
    val isPasswordVerified:Boolean,
    val suspensionDate:String,
    val userInfo:User,
    val loanHistoryList:List<List<String>>,
    val loanBookList:List<List<String>>,
    val overdueList:List<List<String>>,
    val reservationList:List<List<String>>,
    val signOut:()->Unit,
    val checkUserVerified:()->Unit,
    val getUserLoanHistoryList:()->Unit,
    val getUserLoanBookList:()->Unit,
    val getReservationList:()->Unit,
    val sendVerificationEmail:()->Unit,
    val resetLibraryList:()->Unit,
    val resetBookStatus:()->Unit,
    val resetUserBookStatus:()->Unit,
    val updateLogInState:(Boolean) ->Unit,
    val updatePasswordVerifiedState:(Boolean) ->Unit,
    val updateEmailVerifiedState:(Boolean) ->Unit,
    val unregister:(String)->Unit,
    val findPassword:(String)->Unit,
    val verifyCurrentPassword:(String)->Unit,
    val changePassword:(String)->Unit,
    val changeUserInfo:(Map<String, Any>)->Unit,
    val register:(User, String)->Unit,
    val signIn:(String, String)->Unit,
)
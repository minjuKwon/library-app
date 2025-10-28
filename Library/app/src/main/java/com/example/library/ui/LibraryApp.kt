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

    val textFieldKeyword by libraryViewModel.textFieldKeyword
    val currentPage by libraryViewModel.currentPage.collectAsState()

    val currentBook by libraryDetailsViewModel.currentLibrary

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
        userViewModel=userViewModel,
        libraryUiState=libraryViewModel.libraryUiState,
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
            onSearch = { libraryViewModel.getInformation(it)}
        ),
        listContentParams = ListContentParams(
            scrollState=scrollState,
            currentPage=currentPage,
            updatePage={libraryViewModel.getInformation(page=it)},
            updateBackPressedTime = {libraryViewModel.updateBackPressedTime(it)},
            isBackPressedDouble={libraryViewModel.isBackPressedDouble()},
            onBookmarkPressed={libraryViewModel.updateBookmarkList(it)},
            onBookItemPressed={ libraryDetailsViewModel.updateCurrentItem(it) },
            updateCurrentBook={ libraryDetailsViewModel.updateCurrentItem(it) }
        ),
        detailsScreenParams = DetailsScreenParams(
            uiState= libraryDetailsViewModel.uiState,
            textFieldKeyword = textFieldKeyword,
            currentBook= currentBook
        ),
        modifier=modifier
    )
}
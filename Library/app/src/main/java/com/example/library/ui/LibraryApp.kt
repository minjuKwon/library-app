package com.example.library.ui

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.library.ui.screens.LibraryAppContent
import com.example.library.ui.screens.search.LibraryViewModel
import com.example.library.ui.utils.ContentType
import com.example.library.ui.utils.DetailsScreenParams
import com.example.library.ui.utils.ListContentParams
import com.example.library.ui.utils.NavigationConfig
import com.example.library.ui.utils.NavigationType
import com.example.library.ui.utils.TextFieldParams

@Composable
fun LibraryApp(
    windowSize: WindowWidthSizeClass,
    libraryViewModel: LibraryViewModel = viewModel(factory= LibraryViewModel.Factory),
    modifier:Modifier= Modifier
){
    val navigationType:NavigationType
    val contentType:ContentType

    val scrollState  = rememberLazyListState()

    val textFieldKeyword by libraryViewModel.textFieldKeyword
    val currentPage by libraryViewModel.currentPage.collectAsState()
    val isDataReadyForUi by libraryViewModel.isDataReadyForUi.collectAsState()

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
        libraryUiState=libraryViewModel.libraryUiState,
        navigationConfig = NavigationConfig(
            contentType=contentType,
            navigationType = navigationType,
            onTabPressed = { libraryViewModel.updateCurrentBookTabType(it) }
        ),
        textFieldParams = TextFieldParams(
            textFieldKeyword=textFieldKeyword,
            updateKeyword={libraryViewModel.updateKeyword(it)},
            onSearch = { libraryViewModel.getInformation(it)}
        ),
        listContentParams = ListContentParams(
            scrollState=scrollState,
            currentPage=currentPage,
            updatePage={libraryViewModel.getInformation(page=it)},
            onBookmarkPressed={libraryViewModel.updateBookmarkList(it)},
            onBookItemPressed={
                libraryViewModel.updateDataReadyForUi(true)
                libraryViewModel.updateDetailsScreenState(it)
            },
            initCurrentItem={v1,v2->
                libraryViewModel.initCurrentItem(v1,v2)
            }
        ),
        detailsScreenParams = DetailsScreenParams(
            isDataReadyForUi= isDataReadyForUi,
            updateDataReadyForUi={libraryViewModel.updateDataReadyForUi(it)},
            onBackPressed={libraryViewModel.resetHomeScreenState(it)}
        ),
        modifier=modifier
    )
}
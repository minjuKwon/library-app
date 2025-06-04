package com.example.library.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.library.R
import com.example.library.ui.screens.search.getBookList
import com.example.library.ui.screens.search.getTabPressed
import com.example.library.ui.screens.search.LibraryUiState
import com.example.library.ui.utils.DetailsScreenParams
import com.example.library.ui.utils.ListContentParams
import com.example.library.ui.utils.NavigationConfig
import com.example.library.ui.utils.TextFieldParams
import com.example.library.ui.screens.search.LibraryListAndDetailContent
import com.example.library.ui.screens.search.LibraryListOnlyContent
import com.example.library.ui.utils.ContentType
import com.example.library.ui.utils.NavigationType

@Composable
fun LibraryScreen(
    libraryUiState: LibraryUiState,
    navigationItemContent: List<NavigationItemContent>,
    navigationConfig: NavigationConfig,
    textFieldParams: TextFieldParams,
    listContentParams: ListContentParams,
    detailsScreenParams: DetailsScreenParams,
    modifier:Modifier=Modifier
){

    Box(modifier=modifier){
        Row(modifier=Modifier.fillMaxSize()){

            AnimatedVisibility(
                visible=navigationConfig.navigationType==NavigationType.NAVIGATION_RAIL
            ){
                BookNavigationRail(
                    currentTab = getTabPressed(libraryUiState),
                    navigationItemContentList = navigationItemContent,
                    onTabPressed = navigationConfig.onTabPressed,
                    modifier=Modifier.testTag(stringResource(R.string.navigation_rail))
                )
            }

            Column(modifier=Modifier.fillMaxSize()) {
                if(navigationConfig.contentType==ContentType.LIST_AND_DETAIL){
                    LibraryListAndDetailContent(
                        libraryUiState = libraryUiState,
                        books = getBookList(libraryUiState).collectAsLazyPagingItems() ,
                        listContentParams=listContentParams,
                        textFieldParams=textFieldParams,
                        detailsScreenParams=detailsScreenParams,
                    )
                }else{
                    when(libraryUiState){
                        is LibraryUiState.Success -> LibraryListOnlyContent(
                            libraryUiState=libraryUiState,
                            books=libraryUiState.list.book.collectAsLazyPagingItems(),
                            textFieldParams=textFieldParams,
                            listContentParams=listContentParams,
                            modifier= Modifier
                                .padding(dimensionResource(R.dimen.padding_sm))
                                .fillMaxSize()
                                .weight(1f)
                        )
                        is LibraryUiState.Loading -> {
                            LoadingScreen(modifier= Modifier.fillMaxSize().weight(1f))
                        }
                        is LibraryUiState.Error -> {
                            ErrorScreen(
                                input=textFieldParams.textFieldKeyword,
                                retryAction = textFieldParams.onSearch,
                                modifier= Modifier.fillMaxSize().weight(1f)
                            )
                        }
                    }
                }
                AnimatedVisibility(
                    visible = navigationConfig.navigationType==NavigationType.BOTTOM_NAVIGATION
                ) {
                    BookBottomNavigationBar(
                        currentTab = getTabPressed(libraryUiState),
                        navigationItemContentList = navigationItemContent,
                        onTabPressed = navigationConfig.onTabPressed,
                        modifier=Modifier.fillMaxWidth()
                    )
                }
            }

        }
    }

}

@Composable
private fun LoadingScreen(modifier: Modifier=Modifier){
    Image(
        painter=painterResource(R.drawable.baseline_rotate_left_24),
        contentDescription=stringResource(R.string.loading),
        modifier=modifier.size(dimensionResource(R.dimen.loading_screen_image_size))
    )
}

@Composable
private fun ErrorScreen(
    input:String,
    retryAction:(String)->Unit,
    modifier: Modifier=Modifier
){
    Column(
        modifier=modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Image(
            painter = painterResource(R.drawable.baseline_error_outline_24),
            contentDescription = stringResource(R.string.error)
        )
        Text(
            text=stringResource(R.string.error),
            modifier=Modifier.padding(dimensionResource(R.dimen.padding_md))
        )
        Button(onClick = {retryAction(input)}) {
            Text(text=stringResource(R.string.retry))
        }
    }
}
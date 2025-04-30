package com.example.library.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.library.R
import com.example.library.data.NavigationMenuType
import com.example.library.getBookList
import com.example.library.getTabPressed
import com.example.library.isBookmarkListEmpty
import com.example.library.ui.LibraryUiState
import com.example.library.ui.DetailsScreenParams
import com.example.library.ui.ListContentParams
import com.example.library.ui.NavigationConfig
import com.example.library.ui.TextFieldParams
import com.example.library.ui.utils.ContentType
import com.example.library.ui.utils.NavigationType

@Composable
fun LibraryScreen(
    libraryUiState: LibraryUiState,
    navigationConfig: NavigationConfig,
    textFieldParams: TextFieldParams,
    listContentParams: ListContentParams,
    detailsScreenParams: DetailsScreenParams,
    modifier:Modifier=Modifier
){

    val navigationItemContentList = listOf(
        NavigationItemContent(
            navigationMenuType=NavigationMenuType.Books,
            icon= Icons.Filled.Book,
            text= stringResource(R.string.book)
        ),
        NavigationItemContent(
            navigationMenuType=NavigationMenuType.Bookmark,
            icon=Icons.Filled.Bookmark,
            text=stringResource(R.string.bookmark)
        )
    )

    if(navigationConfig.navigationType==NavigationType.PERMANENT_NAVIGATION_DRAWER){
        PermanentNavigationDrawer(
            drawerContent = {
                PermanentDrawerSheet(modifier = Modifier.fillMaxWidth(0.2f)) {
                    NavigationDrawerContent(
                        selectedTab = getTabPressed(libraryUiState) ,
                        navigationItemList = navigationItemContentList,
                        onTabPressed = navigationConfig.onTabPressed,
                        modifier= Modifier
                            .fillMaxHeight()
                            .wrapContentWidth()
                            .padding(
                                dimensionResource(
                                    R.dimen.permanent_navigation_drawer_content_padding
                                )
                            )
                    )
                }
            },
            modifier=Modifier.testTag(stringResource(R.string.navigation_drawer))
        ) {
            LibraryContent(
                libraryUiState = libraryUiState,
                navigationItemContent = navigationItemContentList,
                navigationConfig=navigationConfig,
                textFieldParams=textFieldParams,
                listContentParams=listContentParams,
                detailsScreenParams=detailsScreenParams,
                modifier = modifier
            )
        }
    }else{
        when(libraryUiState){
            is LibraryUiState.Success -> {
                if(libraryUiState.isShowingHomepage) {
                    LibraryContent(
                        libraryUiState = libraryUiState,
                        navigationItemContent = navigationItemContentList,
                        navigationConfig=navigationConfig,
                        textFieldParams=textFieldParams,
                        listContentParams=listContentParams,
                        detailsScreenParams=detailsScreenParams,
                        modifier = modifier
                    )
                }
                else {
                    libraryUiState.currentItem[libraryUiState.currentTabType]?.let {
                        LibraryDetailsScreen(
                            libraryUiState = libraryUiState,
                            detailsScreenParams=detailsScreenParams,
                            modifier=modifier
                        )
                    }
                }
            }
            else ->{
                LibraryContent(
                    libraryUiState = libraryUiState,
                    navigationItemContent = navigationItemContentList,
                    navigationConfig=navigationConfig,
                    textFieldParams=textFieldParams,
                    listContentParams=listContentParams,
                    detailsScreenParams=detailsScreenParams,
                    modifier = modifier
                )
            }
        }
    }
}

@Composable
private fun LibraryContent(
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
                    if(getTabPressed(libraryUiState)==NavigationMenuType.Bookmark
                        && isBookmarkListEmpty(libraryUiState)
                    ){
                        BookmarkEmptyScreen(modifier= Modifier.fillMaxSize().weight(1f))
                    }else{
                        when(libraryUiState){
                            is LibraryUiState.Success -> LibraryListOnlyContent(
                                libraryUiState=libraryUiState,
                                books=libraryUiState.list.book.collectAsLazyPagingItems(),
                                textFieldParams=textFieldParams,
                                listContentParams=listContentParams,
                                modifier= Modifier
                                    .padding(dimensionResource(R.dimen.list_only_content_column_padding))
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
            modifier=Modifier.padding(dimensionResource(R.dimen.error_screen_text_padding))
        )
        Button(onClick = {retryAction(input)}) {
            Text(text=stringResource(R.string.retry))
        }
    }
}

@Composable
private fun NavigationDrawerContent(
    selectedTab:NavigationMenuType,
    navigationItemList:List<NavigationItemContent>,
    onTabPressed:(NavigationMenuType)->Unit,
    modifier:Modifier=Modifier
){
    Column(modifier=modifier) {
        for(naviItem in navigationItemList){
            NavigationDrawerItem(
                selected = selectedTab==naviItem.navigationMenuType,
                onClick = { onTabPressed(naviItem.navigationMenuType) },
                label = {
                    Text(
                        text=naviItem.text,
                        modifier=Modifier
                            .padding(start= dimensionResource(
                                R.dimen.permanent_navigation_drawer_content_text_padding)
                            )
                    )
                },
                icon={
                    Icon(
                        imageVector=naviItem.icon,
                        contentDescription=naviItem.text,
                        modifier=Modifier
                            .padding(start= dimensionResource(
                                R.dimen.permanent_navigation_drawer_content_icon_padding)
                            )
                    )
                }
            )
        }
    }
}

@Composable
private fun BookNavigationRail(
    currentTab:NavigationMenuType,
    navigationItemContentList:List<NavigationItemContent>,
    onTabPressed: (NavigationMenuType) -> Unit,
    modifier:Modifier=Modifier
){
    NavigationRail(modifier=modifier) {
        Column(
            modifier=Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            for(naviItem in navigationItemContentList){
                NavigationRailItem(
                    selected = currentTab== naviItem.navigationMenuType,
                    onClick = {onTabPressed(naviItem.navigationMenuType)},
                    icon={
                        Icon(imageVector = naviItem.icon,
                            contentDescription = naviItem.text
                        )
                    },
                    modifier=Modifier.padding(
                        dimensionResource(R.dimen.navigation_rail_item_padding)
                    )
                )
            }
        }
    }
}

@Composable
private fun BookBottomNavigationBar(
    currentTab: NavigationMenuType,
    navigationItemContentList: List<NavigationItemContent>,
    onTabPressed: (NavigationMenuType) -> Unit,
    modifier:Modifier=Modifier
){
    NavigationBar(modifier=modifier.testTag(stringResource(R.string.navigation_bottom))) {
        for(naviItem in navigationItemContentList){
            NavigationBarItem(
                selected = currentTab== naviItem.navigationMenuType,
                onClick = {onTabPressed(naviItem.navigationMenuType)},
                icon={
                    Icon(imageVector=naviItem.icon, contentDescription =naviItem.text)
                }
            )
        }
    }
}

private data class NavigationItemContent(
    val navigationMenuType: NavigationMenuType,
    val icon: ImageVector,
    val text:String
)

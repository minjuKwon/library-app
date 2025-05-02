package com.example.library.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Bookmark
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
import androidx.compose.ui.res.stringResource
import com.example.library.R
import com.example.library.ui.navigation.NavigationMenuType
import com.example.library.ui.screens.search.getTabPressed
import com.example.library.ui.utils.DetailsScreenParams
import com.example.library.ui.screens.search.LibraryUiState
import com.example.library.ui.utils.ListContentParams
import com.example.library.ui.utils.NavigationConfig
import com.example.library.ui.utils.TextFieldParams
import com.example.library.ui.screens.detail.LibraryDetailsScreen
import com.example.library.ui.utils.NavigationType

@Composable
fun LibraryAppContent(
    libraryUiState: LibraryUiState,
    navigationConfig: NavigationConfig,
    textFieldParams: TextFieldParams,
    listContentParams: ListContentParams,
    detailsScreenParams: DetailsScreenParams,
    modifier: Modifier = Modifier
){

    val navigationItemContentList = listOf(
        NavigationItemContent(
            navigationMenuType= NavigationMenuType.Books,
            icon= Icons.Filled.Book,
            text= stringResource(R.string.book)
        ),
        NavigationItemContent(
            navigationMenuType= NavigationMenuType.Bookmark,
            icon= Icons.Filled.Bookmark,
            text= stringResource(R.string.bookmark)
        )
    )

    if(navigationConfig.navigationType== NavigationType.PERMANENT_NAVIGATION_DRAWER){
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
            modifier= Modifier.testTag(stringResource(R.string.navigation_drawer))
        ) {
            LibraryScreen(
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
                    LibraryScreen(
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
                LibraryScreen(
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
private fun NavigationDrawerContent(
    selectedTab: NavigationMenuType,
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
fun BookNavigationRail(
    currentTab: NavigationMenuType,
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
fun BookBottomNavigationBar(
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

data class NavigationItemContent(
    val navigationMenuType: NavigationMenuType,
    val icon: ImageVector,
    val text:String
)

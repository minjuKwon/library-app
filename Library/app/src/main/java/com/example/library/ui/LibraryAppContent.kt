package com.example.library.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import com.example.library.R
import com.example.library.ui.navigation.destination.LibraryDestination
import com.example.library.ui.navigation.graph.NavigationGraph
import com.example.library.ui.navigation.destination.NavigationItemContent
import com.example.library.ui.common.DetailsScreenParams
import com.example.library.ui.common.ListContentParams
import com.example.library.ui.common.NavigationConfig
import com.example.library.ui.common.TextFieldParams
import com.example.library.ui.common.NavigationType
import com.example.library.ui.common.UserScreenParams

@Composable
fun LibraryAppContent(
    navController: NavHostController,
    navigationConfig: NavigationConfig,
    textFieldParams: TextFieldParams,
    listContentParams: ListContentParams,
    detailsScreenParams: DetailsScreenParams,
    userScreenParams: UserScreenParams,
    modifier: Modifier = Modifier
){

    if(navigationConfig.navigationType== NavigationType.PERMANENT_NAVIGATION_DRAWER){
        PermanentNavigationDrawer(
            drawerContent = {
                PermanentDrawerSheet(modifier = Modifier.fillMaxWidth(0.2f)) {
                    NavigationDrawerContent(
                        currentTab = navigationConfig.currentTab ,
                        navigationItemList = navigationConfig.navigationItemContentList,
                        onTabPressed = navigationConfig.onTabPressed,
                        modifier= Modifier
                            .fillMaxHeight()
                            .wrapContentWidth()
                            .padding(
                                dimensionResource(
                                    R.dimen.padding_md
                                )
                            )
                    )
                }
            },
            modifier= Modifier.testTag(stringResource(R.string.navigation_drawer))
        ) {
            NavigationGraph(
                navController= navController,
                navigationConfig= navigationConfig,
                textFieldParams= textFieldParams,
                listContentParams= listContentParams,
                detailsScreenParams= detailsScreenParams,
                userScreenParams = userScreenParams
            )
        }
    }else{
        Box(modifier=modifier){
            Row(modifier=Modifier.fillMaxSize()){
                AnimatedVisibility(
                    visible=navigationConfig.navigationType==NavigationType.NAVIGATION_RAIL
                ){
                    BookNavigationRail(
                        currentTab = navigationConfig.currentTab,
                        navigationItemContentList = navigationConfig.navigationItemContentList,
                        onTabPressed = navigationConfig.onTabPressed,
                        modifier=Modifier.testTag(stringResource(R.string.navigation_rail))
                    )
                }

                Column(modifier=Modifier.fillMaxSize()) {
                    NavigationGraph(
                        navController= navController,
                        navigationConfig= navigationConfig,
                        textFieldParams= textFieldParams,
                        listContentParams= listContentParams,
                        detailsScreenParams= detailsScreenParams,
                        userScreenParams = userScreenParams,
                        modifier=Modifier.weight(1f)
                    )

                    AnimatedVisibility(
                        visible = navigationConfig.navigationType==NavigationType.BOTTOM_NAVIGATION
                    ) {
                        BookBottomNavigationBar(
                            currentTab = navigationConfig.currentTab,
                            navigationItemContentList = navigationConfig.navigationItemContentList,
                            onTabPressed = navigationConfig.onTabPressed,
                            modifier=Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun NavigationDrawerContent(
    currentTab: LibraryDestination,
    navigationItemList:List<NavigationItemContent>,
    onTabPressed:(LibraryDestination)->Unit,
    modifier:Modifier=Modifier
){
    Column(modifier=modifier) {
        for(naviItem in navigationItemList){
            NavigationDrawerItem(
                selected = currentTab==naviItem.navigationMenuType,
                onClick = { onTabPressed(naviItem.navigationMenuType) },
                label = {
                    Text(
                        text= stringResource(naviItem.textId),
                        modifier=Modifier
                            .padding(start= dimensionResource(
                                R.dimen.padding_xl)
                            )
                    )
                },
                icon={
                    Icon(
                        imageVector=naviItem.icon,
                        contentDescription=stringResource(naviItem.textId),
                        modifier=Modifier
                            .padding(start= dimensionResource(
                                R.dimen.padding_sm)
                            )
                    )
                }
            )
        }
    }
}

@Composable
fun BookNavigationRail(
    currentTab: LibraryDestination,
    navigationItemContentList:List<NavigationItemContent>,
    onTabPressed: (LibraryDestination) -> Unit,
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
                            contentDescription = stringResource(naviItem.textId)
                        )
                    },
                    modifier=Modifier.padding(
                        dimensionResource(R.dimen.padding_xxl)
                    )
                )
            }
        }
    }
}

@Composable
fun BookBottomNavigationBar(
    currentTab: LibraryDestination,
    navigationItemContentList: List<NavigationItemContent>,
    onTabPressed: (LibraryDestination) -> Unit,
    modifier:Modifier=Modifier
){
    NavigationBar(modifier=modifier.testTag(stringResource(R.string.navigation_bottom))) {
        for(naviItem in navigationItemContentList){
            NavigationBarItem(
                selected = currentTab== naviItem.navigationMenuType,
                onClick = {onTabPressed(naviItem.navigationMenuType)},
                icon={
                    Icon(
                        imageVector=naviItem.icon,
                        contentDescription =stringResource(naviItem.textId)
                    )
                }
            )
        }
    }
}
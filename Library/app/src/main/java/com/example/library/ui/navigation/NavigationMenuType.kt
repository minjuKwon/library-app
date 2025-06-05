package com.example.library.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.library.R

data class NavigationItemContent(
    val navigationMenuType: LibraryDestination,
    val icon: ImageVector,
    @StringRes val textId:Int
)

val navigationItemContentList = listOf(
    NavigationItemContent(
        navigationMenuType= LibraryDestination.Books,
        icon= Icons.Filled.Book,
        textId= R.string.book
    ),
    NavigationItemContent(
        navigationMenuType= LibraryDestination.Ranking,
        icon= Icons.Filled.Leaderboard,
        textId= R.string.ranking
    ),
    NavigationItemContent(
        navigationMenuType= LibraryDestination.Setting,
        icon= Icons.Filled.AccountBox,
        textId= R.string.setting
    )
)
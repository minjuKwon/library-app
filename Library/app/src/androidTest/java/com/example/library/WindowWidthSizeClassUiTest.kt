package com.example.library

import androidx.activity.ComponentActivity
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.library.fake.FakeNetworkBookshelfRepository
import com.example.library.ui.BookshelfApp
import com.example.library.ui.BookshelfViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Rule
import org.junit.Test

class WindowWidthSizeClassUiTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun compactDevice_verifyUsingNavigationBottom(){
        val dispatcher = StandardTestDispatcher()
        val scope = CoroutineScope(dispatcher)
        val fakeRepository = FakeNetworkBookshelfRepository()
        val viewModel = BookshelfViewModel(
            bookshelfRepository = fakeRepository,
            ioDispatcher = dispatcher,
            externalScope = scope
        )

        composeTestRule.setContent {
            BookshelfApp(WindowWidthSizeClass.Compact,viewModel)
        }
        composeTestRule.onNodeWithTagForStringId(
            R.string.navigation_bottom
        ).assertExists()

        scope.cancel()
    }

    @Test
    fun mediumDevice_verifyUsingNavigationRail(){
        val dispatcher = StandardTestDispatcher()
        val scope = CoroutineScope(dispatcher)
        val fakeRepository = FakeNetworkBookshelfRepository()
        val viewModel = BookshelfViewModel(
            bookshelfRepository = fakeRepository,
            ioDispatcher = dispatcher,
            externalScope = scope
        )

        composeTestRule.setContent {
            BookshelfApp(WindowWidthSizeClass.Medium, viewModel)
        }
        composeTestRule.onNodeWithTagForStringId(
            R.string.navigation_rail
        ).assertExists()

        scope.cancel()
    }

    @Test
    fun expandedDevice_verifyUsingNavigationDrawer(){
        val dispatcher = StandardTestDispatcher()
        val scope = CoroutineScope(dispatcher)
        val fakeRepository = FakeNetworkBookshelfRepository()
        val viewModel = BookshelfViewModel(
            bookshelfRepository = fakeRepository,
            ioDispatcher = dispatcher,
            externalScope = scope
        )

        composeTestRule.setContent {
            BookshelfApp(WindowWidthSizeClass.Expanded,viewModel)
        }
        composeTestRule.onNodeWithTagForStringId(
            R.string.navigation_drawer
        ).assertExists()

        scope.cancel()
    }

}
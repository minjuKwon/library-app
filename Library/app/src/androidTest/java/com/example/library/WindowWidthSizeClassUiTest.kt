package com.example.library

import androidx.activity.ComponentActivity
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.lifecycle.testing.TestLifecycleOwner
import com.example.library.fake.FakeNetworkBookRepository
import com.example.library.fake.FakeUserService
import com.example.library.rules.onNodeWithTagForStringId
import com.example.library.ui.screens.search.LibraryViewModel
import com.example.library.ui.LibraryApp
import com.example.library.ui.screens.detail.LibraryDetailsViewModel
import com.example.library.ui.screens.user.UserViewModel
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
        verifyWindowWidthSize(WindowWidthSizeClass.Compact, R.string.navigation_bottom)
    }

    @Test
    fun mediumDevice_verifyUsingNavigationRail(){
        verifyWindowWidthSize(WindowWidthSizeClass.Medium, R.string.navigation_rail)
    }

    @Test
    fun expandedDevice_verifyUsingNavigationDrawer(){
        verifyWindowWidthSize(WindowWidthSizeClass.Expanded, R.string.navigation_drawer)
    }

    private fun verifyWindowWidthSize(
        windowWidthSizeClass: WindowWidthSizeClass,
        stringId:Int
    ){
        val dispatcher = StandardTestDispatcher()
        val scope = CoroutineScope(dispatcher)
        val fakeRepository = FakeNetworkBookRepository()
        val viewModel = LibraryViewModel(
            bookRepository = fakeRepository,
            ioDispatcher = dispatcher,
            externalScope = scope
        )
        val dummyDetailsViewModel= LibraryDetailsViewModel(fakeRepository, dispatcher, scope)
        val dummyUserViewModel= UserViewModel(FakeUserService(), scope)
        val testLifecycleOwner = TestLifecycleOwner()

        composeTestRule.setContent {
            LibraryApp(
                windowSize = windowWidthSizeClass,
                lifecycleOwner = testLifecycleOwner,
                libraryViewModel = viewModel,
                libraryDetailsViewModel = dummyDetailsViewModel,
                userViewModel = dummyUserViewModel
            )
        }
        composeTestRule.onNodeWithTagForStringId(
            stringId
        ).assertExists()

        scope.cancel()
    }

}
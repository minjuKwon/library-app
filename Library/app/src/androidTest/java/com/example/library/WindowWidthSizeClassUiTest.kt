package com.example.library

import androidx.activity.ComponentActivity
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.library.fake.FakeSessionManager
import com.example.library.fake.FakeTimeProvider
import com.example.library.fake.repository.FakeBookRepository
import com.example.library.fake.service.FakeLibrarySyncService
import com.example.library.fake.service.FakeUserService
import com.example.library.rules.onNodeWithTagForStringId
import com.example.library.service.FirebaseBookService
import com.example.library.ui.LibraryApp
import com.example.library.ui.screens.detail.LibraryDetailsViewModel
import com.example.library.ui.screens.search.LibraryViewModel
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

        val fakeFirebaseBookService= FirebaseBookService(FakeBookRepository(), FakeTimeProvider())
        val fakeSessionManager= FakeSessionManager()
        val dummyLibraryViewModel = LibraryViewModel(
            librarySyncService = FakeLibrarySyncService(),
            firebaseBookService = fakeFirebaseBookService,
            defaultSessionManager = fakeSessionManager,
            externalScope = scope
        )
        val dummyDetailsViewModel= LibraryDetailsViewModel(fakeFirebaseBookService,fakeSessionManager,scope)
        val dummyUserViewModel= UserViewModel(FakeUserService(), fakeFirebaseBookService, scope)

        composeTestRule.setContent {
            LibraryApp(
                windowSize = windowWidthSizeClass,
                libraryViewModel= dummyLibraryViewModel,
                libraryDetailsViewModel= dummyDetailsViewModel,
                userViewModel = dummyUserViewModel
            )
        }
        composeTestRule.onNodeWithTagForStringId(
            stringId
        ).assertExists()

        scope.cancel()
    }

}
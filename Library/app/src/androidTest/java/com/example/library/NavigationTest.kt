package com.example.library

import androidx.activity.ComponentActivity
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.lifecycle.testing.TestLifecycleOwner
import com.example.library.common.TestUtil.waitForToast
import com.example.library.data.fake.FakeNetworkBookRepository
import com.example.library.fake.FakeUserService
import com.example.library.rules.onNodeWithContentDescriptionForStringId
import com.example.library.rules.onNodeWithTagForStringId
import com.example.library.ui.LibraryApp
import com.example.library.ui.screens.detail.LibraryDetailsViewModel
import com.example.library.ui.screens.search.LibraryViewModel
import com.example.library.ui.screens.user.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NavigationTest {

    @get:Rule
    val composeTestRule= createAndroidComposeRule<ComponentActivity>()

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = CoroutineScope(testDispatcher)

    @Before
    fun setUp(){
        init()
    }

    @After
    fun closeResource(){
        testScope.cancel()
    }

    @Test
    fun mainScreen_clickBookTab_displaysBookScreen(){
        pressTab(R.string.book)

        composeTestRule.onNodeWithTagForStringId(
            R.string.test_itemCount
        ).assertExists()
    }

    @Test
    fun mainScreen_clickRankingTab_displaysRankingScreen(){
        pressTab( R.string.ranking)

        composeTestRule.onNodeWithContentDescriptionForStringId(
            R.string.ranking_options
        ).assertExists()
    }

    @Test
    fun mainScreen_clickSettingTab_displaysNoMemberScreen(){
        pressTab(R.string.setting)

        composeTestRule.onNodeWithTagForStringId(
            R.string.test_noMember
        ).assertExists()
    }

    @Test
    fun mainScreen_pressBack_showCorrectToast(){
        composeTestRule.onNodeWithContentDescriptionForStringId(
            R.string.book
        ).assertExists()

        composeTestRule.runOnUiThread {
            composeTestRule.activity.onBackPressedDispatcher.onBackPressed()
        }

        composeTestRule.waitForToast(R.string.toast_finish)
    }

    private fun init(
        fakeRepository:FakeNetworkBookRepository= FakeNetworkBookRepository(),
        libraryViewModel:LibraryViewModel
        = LibraryViewModel(fakeRepository, testDispatcher, testScope)
    ){
        val dummyDetailsViewModel= LibraryDetailsViewModel(fakeRepository, testDispatcher, testScope)
        val dummyUserViewModel= UserViewModel(FakeUserService(), testScope)
        val testLifecycleOwner= TestLifecycleOwner()

        composeTestRule.setContent {
            LibraryApp(
                WindowWidthSizeClass.Compact,
                testLifecycleOwner,
                libraryViewModel,
                dummyDetailsViewModel,
                dummyUserViewModel
            )
        }

        composeTestRule.runOnIdle {
            testDispatcher.scheduler.advanceUntilIdle()
        }
    }

    private fun pressTab(tabId:Int){
        composeTestRule.onNodeWithContentDescriptionForStringId(
            tabId
        ).assertExists()
        composeTestRule.onNodeWithContentDescriptionForStringId(
            tabId
        ).performClick()
    }

}
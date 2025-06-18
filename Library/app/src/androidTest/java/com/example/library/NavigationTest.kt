package com.example.library

import androidx.activity.ComponentActivity
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.lifecycle.testing.TestLifecycleOwner
import com.example.library.data.fake.FakeNetworkBookRepository
import com.example.library.rules.onNodeWithContentDescriptionForStringId
import com.example.library.rules.onNodeWithTagForStringId
import com.example.library.ui.LibraryApp
import com.example.library.ui.screens.detail.LibraryDetailsViewModel
import com.example.library.ui.screens.search.LibraryViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.After
import org.junit.Rule
import org.junit.Test

class NavigationTest {

    @get:Rule
    val composeTestRule= createAndroidComposeRule<ComponentActivity>()

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = CoroutineScope(testDispatcher)

    @After
    fun closeResource(){
        testScope.cancel()
    }

    @Test
    fun mainScreen_clickBookTab_displaysBookScreen(){
        init()

        pressTab(R.string.book)

        composeTestRule.onNodeWithTagForStringId(
            R.string.test_itemCount
        ).assertExists()
    }

    @Test
    fun mainScreen_clickRankingTab_displaysRankingScreen(){
        init()

        pressTab( R.string.ranking)

        composeTestRule.onNodeWithContentDescriptionForStringId(
            R.string.ranking_options
        ).assertExists()
    }

    @Test
    fun mainScreen_clickSettingTab_displaysNoMemberScreen(){
        init()

        pressTab(R.string.setting)

        composeTestRule.onNodeWithTagForStringId(
            R.string.test_noMember
        ).assertExists()
    }

    @Test
    fun noMemberScreen_logIn_displaysSettingScreen(){
        logIn()

        composeTestRule.onNodeWithTagForStringId(
            R.string.test_account_edit
        ).assertExists()

        //원 상태로 되돌리기
        composeTestRule.onNodeWithTagForStringId(
            R.string.test_logOut
        ).performClick()
    }

    @Test
    fun settingScreen_clickLogOut_displaysNoMemberScreen(){
        logIn()

        composeTestRule.onNodeWithTagForStringId(
            R.string.test_logOut
        ).performClick()

        composeTestRule.onNodeWithTagForStringId(
            R.string.test_noMember
        ).assertExists()
    }

    @Test
    fun settingScreen_clickUnregister_displaysNoMemberScreen(){
        logIn()

        composeTestRule.onNodeWithTagForStringId(
            R.string.test_unregister
        ).performClick()

        composeTestRule.onNodeWithTagForStringId(
            R.string.test_noMember
        ).assertExists()
    }

    @Test
    fun mainScreen_pressBack_showCorrectToast(){
        val fakeRepository= FakeNetworkBookRepository()
        val libraryViewModel= LibraryViewModel(fakeRepository, testDispatcher, testScope)

        init(libraryViewModel = libraryViewModel)

        composeTestRule.onNodeWithContentDescriptionForStringId(
            R.string.book
        ).assertExists()

        composeTestRule.runOnUiThread {
            composeTestRule.activity.onBackPressedDispatcher.onBackPressed()
        }

        assertEquals(true, libraryViewModel.isBackPressedDouble() )
    }

    private fun init(
        fakeRepository:FakeNetworkBookRepository= FakeNetworkBookRepository(),
        libraryViewModel:LibraryViewModel
        = LibraryViewModel(fakeRepository, testDispatcher, testScope)
    ){
        val dummyViewModel= LibraryDetailsViewModel(fakeRepository, testDispatcher, testScope)
        val testLifecycleOwner= TestLifecycleOwner()

        composeTestRule.setContent {
            LibraryApp(
                WindowWidthSizeClass.Compact,
                testLifecycleOwner,
                libraryViewModel,
                dummyViewModel
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

    private fun logIn(){
        init()

        pressTab( R.string.setting)
        composeTestRule
            .onNodeWithTagForStringId(id=R.string.test_logIn,useUnmergedTree=true)
            .performClick()
        composeTestRule
            .onNodeWithTagForStringId(id=R.string.test_logIn,useUnmergedTree=true)
            .performClick()
    }

}
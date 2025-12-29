package com.example.library

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import com.example.library.common.TestUtil.waitForToast
import com.example.library.rules.onNodeWithContentDescriptionForStringId
import com.example.library.rules.onNodeWithTagForStringId
import com.example.library.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class NavigationTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    @get:Rule
    val composeTestRule= createAndroidComposeRule<MainActivity>()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp(){
        hiltRule.inject()
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

        waitForToast(R.string.toast_finish)
    }

    private fun pressTab(tabId:Int){
        composeTestRule.runOnIdle {
            testDispatcher.scheduler.advanceUntilIdle()
        }
        composeTestRule.onNodeWithContentDescriptionForStringId(
            tabId
        ).assertExists()
        composeTestRule.onNodeWithContentDescriptionForStringId(
            tabId
        ).performClick()
    }

}
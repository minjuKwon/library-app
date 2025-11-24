package com.example.library

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import com.example.library.common.UserTestHelper.logIn
import com.example.library.common.UserTestHelper.register
import com.example.library.common.UserTestHelper.unregister
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
class LibraryLikeTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp(){
        hiltRule.inject()
    }

    @Test
    fun libraryListScreen_clickLikeButton_displaysDialog(){
        composeTestRule
            .onNodeWithTag("android_1false", true)
            .performClick()

        composeTestRule
            .onNodeWithTagForStringId(
                id=R.string.test_check_like_dialog,
                useUnmergedTree = true
            )
            .assertExists()
    }

    @Test
    fun libraryListScreen_clickLikeButton_displaysButtonChange(){
        composeTestRule.runOnIdle {
            testDispatcher.scheduler.advanceUntilIdle()
        }
        composeTestRule.onNodeWithContentDescriptionForStringId(R.string.setting).assertExists()
        composeTestRule.onNodeWithContentDescriptionForStringId(R.string.setting).performClick()
        register(composeTestRule,testDispatcher)
        logIn(composeTestRule,testDispatcher,false)

        composeTestRule.onNodeWithContentDescriptionForStringId(R.string.book).assertExists()
        composeTestRule.onNodeWithContentDescriptionForStringId(R.string.book).performClick()

        composeTestRule
            .onNodeWithTag("android_1false",true)
            .assertExists()
        composeTestRule
            .onNodeWithTag("android_1false",true)
            .performClick()
        composeTestRule
            .onNodeWithTag("android_1true",true)
            .assertExists()
        composeTestRule
            .onNodeWithTag("android_1true",true)
            .performClick()
        composeTestRule
            .onNodeWithTag("android_1false",true)
            .assertExists()

        composeTestRule.onNodeWithContentDescriptionForStringId(R.string.setting).assertExists()
        composeTestRule.onNodeWithContentDescriptionForStringId(R.string.setting).performClick()
        unregister(composeTestRule,testDispatcher)
    }

    @Test
    fun libraryListScreen_clickLikeButton_showCorrectList(){
        composeTestRule.runOnIdle {
            testDispatcher.scheduler.advanceUntilIdle()
        }
        composeTestRule.onNodeWithContentDescriptionForStringId(R.string.setting).assertExists()
        composeTestRule.onNodeWithContentDescriptionForStringId(R.string.setting).performClick()
        register(composeTestRule,testDispatcher)
        logIn(composeTestRule,testDispatcher,false)

        composeTestRule.onNodeWithContentDescriptionForStringId(R.string.book).assertExists()
        composeTestRule.onNodeWithContentDescriptionForStringId(R.string.book).performClick()

        composeTestRule
            .onNodeWithTag("android_1false",true)
            .performClick()
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_list)
            .performScrollToIndex(2)
        composeTestRule
            .onNodeWithTag("android_3false",true)
            .performClick()

        composeTestRule.onNodeWithContentDescriptionForStringId(R.string.setting).assertExists()
        composeTestRule.onNodeWithContentDescriptionForStringId(R.string.setting).performClick()

        composeTestRule
            .onNodeWithTagForStringId(id=R.string.liked_list, useUnmergedTree = true)
            .performClick()

        composeTestRule
            .onNodeWithTag("android_1",   true)
            .assertTextEquals("android_1")
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_list)
            .performScrollToIndex(1)
        composeTestRule
            .onNodeWithTag("android_3", true)
            .assertTextEquals("android_3")

        composeTestRule
            .onNodeWithTagForStringId(R.string.test_back, useUnmergedTree=true)
            .performClick()

        unregister(composeTestRule,testDispatcher)
    }

}
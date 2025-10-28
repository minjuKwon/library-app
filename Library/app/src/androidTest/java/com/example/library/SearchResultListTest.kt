package com.example.library

import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import com.example.library.data.room.LibraryDatabase
import com.example.library.rules.onNodeWithTagForStringId
import com.example.library.ui.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
class SearchResultListTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()
    @Inject
    lateinit var db: LibraryDatabase

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp(){
        hiltRule.inject()
        composeTestRule.runOnIdle {
            testDispatcher.scheduler.advanceUntilIdle()
        }
    }

    @After
    fun closeResource(){
        db.close()
    }

    @Test
    fun mainScreen_searchResults_showsCorrectItemCount(){
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_itemCount)
            .assertTextEquals(" 15")
    }

    @Test
    fun mainScreen_searchResults_showsCorrectItemList(){
        verifyListItemText(testDispatcher,0, "android_1")

        composeTestRule
            .onNodeWithTagForStringId(R.string.test_list)
            .performScrollToIndex(9)

        verifyListItemText(testDispatcher,9, "android_10")
    }

    @Test
    fun mainScreen_onNextPage_showsNextItemList(){
        moveNextPage(9,"2", testDispatcher)

        verifyListItemText(testDispatcher,0, "android_11")

        composeTestRule
            .onNodeWithTagForStringId(R.string.test_list)
            .performScrollToIndex(4)

        verifyListItemText(testDispatcher,4, "android_15")
    }

    private fun moveNextPage(
        idx:Int,
        pageNumber:String,
        testDispatcher: TestDispatcher
    ){
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_list)
            .performScrollToIndex(idx)

        composeTestRule
            .onNodeWithTagForStringId(R.string.test_pageNum,pageNumber,useUnmergedTree = true)
            .performClick()

        //ui 안정화 위해 여러번 호출
        composeTestRule.runOnIdle {
            testDispatcher.scheduler.advanceUntilIdle()
        }
        composeTestRule.runOnIdle {
            testDispatcher.scheduler.advanceUntilIdle()
        }
    }

    private fun verifyListItemText(
        testDispatcher: TestDispatcher,
        index:Int,
        str:String
    ){
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_list)
            .performScrollToIndex(index)
        composeTestRule
            .onNodeWithTag(str, useUnmergedTree = true)
            .assertTextEquals(str)
        composeTestRule
            .onNodeWithTag(str, useUnmergedTree = true)
            .performClick()
        composeTestRule.runOnIdle {
            testDispatcher.scheduler.advanceUntilIdle()
        }

        composeTestRule
            .onNodeWithTag(str)
            .assertTextEquals(str)
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_back,useUnmergedTree=true)
            .performClick()
        composeTestRule.runOnIdle {
            testDispatcher.scheduler.advanceUntilIdle()
        }

        composeTestRule
            .onNodeWithTagForStringId(R.string.test_list)
            .performScrollToIndex(index)
        composeTestRule
            .onNodeWithTag(str, useUnmergedTree = true)
            .assertTextEquals(str)
    }

}
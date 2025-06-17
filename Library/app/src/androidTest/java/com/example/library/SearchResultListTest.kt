package com.example.library

import androidx.activity.ComponentActivity
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import androidx.lifecycle.testing.TestLifecycleOwner
import com.example.library.fake.FakeNetworkBookRepository
import com.example.library.rules.onNodeWithTagForStringId
import com.example.library.ui.screens.search.LibraryViewModel
import com.example.library.ui.LibraryApp
import com.example.library.ui.screens.detail.LibraryDetailsViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import org.junit.Rule
import org.junit.Test

class SearchResultListTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun mainScreen_searchResults_showsCorrectItemCount(){
        val testDispatcher = StandardTestDispatcher()
        val testScope = CoroutineScope(testDispatcher)

        initial(testDispatcher, testScope)

        composeTestRule
            .onNodeWithTagForStringId(R.string.test_itemCount)
            .assertTextEquals(" 15")

        testScope.cancel()
    }

    @Test
    fun mainScreen_searchResults_showsCorrectItemList(){
        val testDispatcher = StandardTestDispatcher()
        val testScope = CoroutineScope(testDispatcher)

        initial(testDispatcher, testScope)

        verifyListItemText(testDispatcher,0, "android_1")

        composeTestRule
            .onNodeWithTagForStringId(R.string.test_list)
            .performScrollToIndex(9)

        verifyListItemText(testDispatcher,9, "android_10")

        testScope.cancel()
    }

    @Test
    fun mainScreen_onNextPage_showsNextItemList(){
        val testDispatcher = StandardTestDispatcher()
        val testScope = CoroutineScope(testDispatcher)

        initial(testDispatcher, testScope)

        moveNextPage(9,"2", testDispatcher)

        verifyListItemText(testDispatcher,0, "android_11")

        composeTestRule
            .onNodeWithTagForStringId(R.string.test_list)
            .performScrollToIndex(4)

        verifyListItemText(testDispatcher,4, "android_15")


        testScope.cancel()
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

    private fun initial(testDispatcher: TestDispatcher, testScope: CoroutineScope){
        val fakeRepository= FakeNetworkBookRepository()
        val libraryViewModel= LibraryViewModel(
            bookRepository = fakeRepository,
            ioDispatcher = testDispatcher,
            externalScope = testScope
        )

        composeTestRule.runOnIdle {
            testDispatcher.scheduler.advanceUntilIdle()
        }

        val testLifecycleOwner = TestLifecycleOwner()
        val dummyViewModel= LibraryDetailsViewModel(fakeRepository, testDispatcher, testScope)

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

    private fun verifyListItemText(
        testDispatcher: TestDispatcher,
        index:Int,
        str:String
    ){
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
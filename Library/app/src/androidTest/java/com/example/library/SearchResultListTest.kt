package com.example.library

import androidx.activity.ComponentActivity
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import com.example.library.fake.FakeNetworkBookshelfRepository
import com.example.library.ui.BookshelfApp
import com.example.library.ui.BookshelfViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
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
            .assertTextEquals("15")

        testScope.cancel()
    }


    private fun initial(testDispatcher: TestDispatcher, testScope: CoroutineScope){
        val fakeRepository= FakeNetworkBookshelfRepository()
        val bookshelfViewModel= BookshelfViewModel(
            bookshelfRepository = fakeRepository,
            ioDispatcher = testDispatcher,
            externalScope = testScope
        )
        composeTestRule.runOnIdle {
            testDispatcher.scheduler.advanceUntilIdle()
        }

        composeTestRule.setContent {
            BookshelfApp(WindowWidthSizeClass.Compact,bookshelfViewModel)
        }

        composeTestRule.runOnIdle {
            testDispatcher.scheduler.advanceUntilIdle()
        }
    }

}
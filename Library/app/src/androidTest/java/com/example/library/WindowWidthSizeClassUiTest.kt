package com.example.library

import androidx.activity.ComponentActivity
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.example.library.ui.BookshelfApp
import org.junit.Rule
import org.junit.Test

class WindowWidthSizeClassUiTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun compactDevice_verifyUsingNavigationBottom(){
        composeTestRule.setContent {
            BookshelfApp(WindowWidthSizeClass.Compact)
        }
        composeTestRule.onNodeWithTagForStringId(
            R.string.navigation_bottom
        ).assertExists()
    }

    @Test
    fun mediumDevice_verifyUsingNavigationRail(){
        composeTestRule.setContent {
            BookshelfApp(WindowWidthSizeClass.Medium)
        }
        composeTestRule.onNodeWithTagForStringId(
            R.string.navigation_rail
        ).assertExists()
    }

    @Test
    fun expandedDevice_verifyUsingNavigationDrawer(){
        composeTestRule.setContent {
            BookshelfApp(WindowWidthSizeClass.Expanded)
        }
        composeTestRule.onNodeWithTagForStringId(
            R.string.navigation_drawer
        ).assertExists()
    }

}
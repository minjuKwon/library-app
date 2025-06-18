package com.example.library.rules

import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.test.ext.junit.rules.ActivityScenarioRule

fun <A: ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>
        .onNodeWithTagForStringId(
            @StringRes id:Int,
            string:String="",
            useUnmergedTree:Boolean=false
        ):SemanticsNodeInteraction
        =onNodeWithTag(activity.getString(id)+string, useUnmergedTree)

fun <A: ComponentActivity> AndroidComposeTestRule<ActivityScenarioRule<A>, A>
        .onNodeWithContentDescriptionForStringId(
            @StringRes id:Int
        ):SemanticsNodeInteraction
        =onNodeWithContentDescription(activity.getString(id))
package com.example.library.common

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.Lifecycle
import com.example.library.R
import com.example.library.rules.onNodeWithContentDescriptionForStringId
import com.example.library.rules.onNodeWithTagForStringId
import kotlinx.coroutines.test.TestDispatcher
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule

typealias ComposeRule<A> = AndroidComposeTestRule<ActivityScenarioRule<A>, A>

object UserTestHelper {

    const val EMAIL="android@gmail.com"
    const val PASSWORD="123456"
    const val WRONG_PASSWORD="111111"
    const val NAME="Android"

    fun <A: ComponentActivity> initial(
        composeTestRule: ComposeRule<A>,
        testDispatcher: TestDispatcher
    ){
        composeTestRule.runOnIdle {
            testDispatcher.scheduler.advanceUntilIdle()
        }
        composeTestRule.onNodeWithContentDescriptionForStringId(R.string.setting).assertExists()
        composeTestRule.onNodeWithContentDescriptionForStringId(R.string.setting).performClick()
    }

    fun <A: ComponentActivity> register(
        composeTestRule: ComposeRule<A>,
        testDispatcher: TestDispatcher,
        isClick:Boolean=true
    ){
        //회원가입 화면 이동
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_register, useUnmergedTree=true)
            .performClick()

        //이름 입력
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_register_name, useUnmergedTree=true)
            .assertExists()
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_register_name)
            .performTextInput(NAME)
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_register_name)
            .performImeAction()

        //나이 입력
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_register_age)
            .performTextInput("10")
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_register_age)
            .performImeAction()

        //메일 입력
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_register_email)
            .performTextInput(EMAIL)
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_register_email)
            .performImeAction()

        //비밀번호 입력
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_register_password)
            .performTextInput(PASSWORD)
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_register_password)
            .performImeAction()

        //비밀번호 재입력
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_register_verification_password)
            .performTextInput(PASSWORD)
        if(isClick){
            composeTestRule
                .onNodeWithTagForStringId(R.string.test_register_verification_password)
                .performImeAction()
            composeTestRule.runOnIdle {
                testDispatcher.scheduler.advanceUntilIdle()
            }
        }
    }

    fun <A: ComponentActivity>unregister(
        composeTestRule: ComposeRule<A>,
        testDispatcher: TestDispatcher,
        userPassword:String=PASSWORD
    ){
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_unregister, useUnmergedTree=true)
            .assertExists()
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_unregister, useUnmergedTree=true)
            .performClick()

        composeTestRule
            .onNodeWithTagForStringId(R.string.test_unregister_email)
            .assertTextEquals("아이디 : $EMAIL")
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_unregister_password)
            .performTextInput(userPassword)
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_unregister_confirm, useUnmergedTree=true)
            .performClick()
        composeTestRule.runOnIdle {
            testDispatcher.scheduler.advanceUntilIdle()
        }

        composeTestRule
            .onNodeWithTagForStringId(R.string.test_noMember, useUnmergedTree=true)
            .assertExists()
    }

    fun <A: ComponentActivity> logIn(
        composeTestRule: ComposeRule<A>,
        testDispatcher: TestDispatcher,
        isVerified:Boolean,
        userName:String=NAME,
        userPassword:String=PASSWORD
    ){
        if(isVerified){
            composeTestRule
                .onNodeWithContentDescriptionForStringId(R.string.setting)
                .performClick()
            composeTestRule
                .onNodeWithTagForStringId(R.string.test_logIn, useUnmergedTree=true)
                .performClick()

            composeTestRule
                .onNodeWithTagForStringId(R.string.test_logIn_reauthorization)
                .assertDoesNotExist()
            composeTestRule
                .onNodeWithTagForStringId(R.string.test_logIn, useUnmergedTree=true)
                .assertExists()
        }else{
            verifyUser(composeTestRule, testDispatcher)
        }

        inputLogInInfo(composeTestRule,testDispatcher,userName, userPassword)
    }

    fun <A: ComponentActivity> verifyUser(
        composeTestRule: ComposeRule<A>,
        testDispatcher: TestDispatcher,
    ){
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_not_verification)
            .assertExists()
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_resend, useUnmergedTree=true)
            .performClick()
        composeTestRule.activityRule.scenario.moveToState(Lifecycle.State.STARTED)
        composeTestRule.activityRule.scenario.moveToState(Lifecycle.State.RESUMED)
        composeTestRule.runOnIdle {
            testDispatcher.scheduler.advanceUntilIdle()
        }

        composeTestRule
            .onNodeWithTagForStringId(R.string.test_logIn_reauthorization)
            .assertExists()
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_logIn, useUnmergedTree=true)
            .assertExists()
    }

    fun <A: ComponentActivity> inputLogInInfo(
        composeTestRule: ComposeRule<A>,
        testDispatcher: TestDispatcher,
        userName:String=NAME,
        userPassword:String=PASSWORD
    ){
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_logIn_email)
            .performTextInput(EMAIL)
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_logIn_email)
            .performImeAction()
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_logIn_password)
            .performTextInput(userPassword)
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_logIn_password)
            .performImeAction()
        composeTestRule.runOnIdle {
            testDispatcher.scheduler.advanceUntilIdle()
        }

        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_name)
            .assertExists()
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_name)
            .assertTextEquals(userName)
    }

    fun <A: ComponentActivity>logOut(
        composeTestRule: ComposeRule<A>,
        testDispatcher: TestDispatcher
    ){
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_logOut)
            .assertExists()
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_logOut, useUnmergedTree = true)
            .performClick()

        composeTestRule.runOnIdle {
            testDispatcher.scheduler.advanceUntilIdle()
        }
    }
}
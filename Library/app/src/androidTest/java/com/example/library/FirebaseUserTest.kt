package com.example.library

import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.example.library.common.UserTestHelper.AGE
import com.example.library.common.UserTestHelper.EMAIL
import com.example.library.common.UserTestHelper.NAME
import com.example.library.common.UserTestHelper.initial
import com.example.library.common.UserTestHelper.inputLogInInfo
import com.example.library.common.UserTestHelper.logIn
import com.example.library.common.UserTestHelper.logOut
import com.example.library.common.UserTestHelper.PASSWORD
import com.example.library.common.UserTestHelper.register
import com.example.library.common.UserTestHelper.unregister
import com.example.library.common.UserTestHelper.verifyUser
import com.example.library.fake.FakeUserRepository.Companion.NEW_PASSWORD
import com.example.library.rules.onNodeWithContentDescriptionForStringId
import com.example.library.rules.onNodeWithTagForStringId
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class FirebaseUserTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)
    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setUp(){
        hiltRule.inject()
        initial(composeTestRule,testDispatcher)
    }

    //회원가입 -> 로그인 -> 로그아웃 -> 로그인 -> 회원 탈퇴
    @Test
    fun registerScreen_checkVerificationAndLogin_displayMemberScreen(){
        register(composeTestRule,testDispatcher)
        
        logIn(composeTestRule,testDispatcher,false)

        logOut(composeTestRule,testDispatcher)

        logIn(composeTestRule,testDispatcher,true)

        unregister(composeTestRule,testDispatcher)
    }

    //회원가입 -> 사용자 인증X -> 로그인 -> 로그아웃 -> 로그인 -> 회원 탈퇴
    @Test
    fun loginScreen_checkVerification_displayMemberScreen() {
        register(composeTestRule,testDispatcher)

        //사용자 인증 안받게 이동
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_not_verification)
            .assertExists()
        composeTestRule
            .onNodeWithContentDescriptionForStringId(R.string.book)
            .performClick()
        composeTestRule
            .onNodeWithContentDescriptionForStringId(R.string.setting)
            .performClick()
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_logIn, useUnmergedTree=true)
            .performClick()

        logIn(composeTestRule,testDispatcher,false)

        logOut(composeTestRule,testDispatcher)

        logIn(composeTestRule,testDispatcher,true)

        unregister(composeTestRule,testDispatcher)
    }

    //회원가입 -> 비밀번호 찾기 -> 로그아웃 -> 로그인 -> 회원 탈퇴
    @Test
    fun loginScreen_findPassword_displayMemberScreen(){
        register(composeTestRule,testDispatcher)

        verifyUser(composeTestRule,testDispatcher)

        //비밀번호 찾기
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_logIn_find_password)
            .performClick()
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_logIn_find_password_email, useUnmergedTree=true)
            .performTextInput(EMAIL)
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_logIn_find_password_confirm, useUnmergedTree=true)
            .performClick()
        composeTestRule.runOnIdle {
            testDispatcher.scheduler.advanceUntilIdle()
        }

        inputLogInInfo(composeTestRule,testDispatcher,userPassword= NEW_PASSWORD)

        unregister(composeTestRule,testDispatcher,NEW_PASSWORD)
    }

    //회원가입 -> 로그인 -> 이름 변경 -> 비밀번호 변경 -> 로그아웃 -> 로그인 -> 회원 탈퇴
    @Test
    fun memberScreen_onEditUserInfo_showCorrectValue(){
        val newName="Android_Kim"
        val newPassword="121212"

        register(composeTestRule,testDispatcher)

        logIn(composeTestRule,testDispatcher,false)

        //사용자 이름 변경
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit, useUnmergedTree = true)
            .assertExists()
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit, useUnmergedTree = true)
            .performClick()
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit_name)
            .performTextClearance()
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit_name)
            .performTextInput(newName)
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit_name)
            .performImeAction()
        composeTestRule.runOnIdle {
            testDispatcher.scheduler.advanceUntilIdle()
        }
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit, useUnmergedTree = true)
            .assertExists()

        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit, useUnmergedTree = true)
            .assertExists()
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit, useUnmergedTree = true)
            .performClick()

        //비밀번호 변경
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit_current_password)
            .performTextInput(PASSWORD)
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit_current_password)
            .performImeAction()
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit_new_password)
            .performTextInput(newPassword)
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit_new_password)
            .performImeAction()
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit_confirm_new_password)
            .performTextInput(newPassword)
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit_confirm_new_password)
            .performImeAction()
        composeTestRule.runOnIdle {
            testDispatcher.scheduler.advanceUntilIdle()
        }
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit, useUnmergedTree = true)
            .assertExists()

        logOut(composeTestRule,testDispatcher)

        logIn(composeTestRule,testDispatcher,true, newName, newPassword)

        unregister(composeTestRule,testDispatcher,newPassword)
    }

    //회원가입 -> 로그인 -> 회원 정보 일치 여부 확인 -> 회원 탈퇴
    @Test
    fun memberScreen_checkUserInformation_showCorrectValue(){
        register(composeTestRule,testDispatcher)

        logIn(composeTestRule,testDispatcher,false)

        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit, useUnmergedTree = true)
            .assertExists()
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit, useUnmergedTree = true)
            .performClick()

        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit_name)
            .assertTextContains(NAME)
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit_gender)
            .assertTextEquals("남")
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit_age)
            .assertTextEquals(AGE)

        composeTestRule
            .onNodeWithTagForStringId(R.string.test_back, useUnmergedTree=true)
            .performClick()

        unregister(composeTestRule,testDispatcher)
    }

}
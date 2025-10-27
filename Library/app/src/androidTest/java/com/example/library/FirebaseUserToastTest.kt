package com.example.library

import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.example.library.common.TestUtil.waitForToast
import com.example.library.common.UserTestHelper.WRONG_PASSWORD
import com.example.library.common.UserTestHelper.EMAIL
import com.example.library.common.UserTestHelper.initial
import com.example.library.common.UserTestHelper.logIn
import com.example.library.common.UserTestHelper.logOut
import com.example.library.common.UserTestHelper.PASSWORD
import com.example.library.common.UserTestHelper.register
import com.example.library.common.UserTestHelper.unregister
import com.example.library.common.UserTestHelper.verifyUser
import com.example.library.fake.repository.FakeUserRepository.Companion.NEW_PASSWORD
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
class FirebaseUserToastTest {

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

    @Test
    fun registerScreen_inputWrongEmail_showCorrectToast(){
        register(composeTestRule,testDispatcher,false)

        //회원 가입 시 빈 이메일 검사
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_register_email)
            .performTextClearance()
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_register_button, useUnmergedTree=true)
            .performClick()
        composeTestRule.waitForToast(R.string.blank_email)

        //회원 가입 시 잘못된 이메일 형식 검사
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_register_email)
            .performTextInput("email")
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_register_button, useUnmergedTree=true)
            .performClick()
        composeTestRule.waitForToast(R.string.loading_register)
        composeTestRule.waitForToast(R.string.invalid_email)
    }

    @Test
    fun registerScreen_inputEmailInUse_showCorrectToast(){
        //회원 가입
        register(composeTestRule,testDispatcher)
        composeTestRule.waitForToast(R.string.loading_register)
        composeTestRule.waitForToast(R.string.success_register)
        logIn(composeTestRule,testDispatcher,false)
        composeTestRule.waitForToast(R.string.send_email)
        composeTestRule.waitForToast(R.string.loading_signIn)
        logOut(composeTestRule,testDispatcher)

        composeTestRule
            .onNodeWithContentDescriptionForStringId(R.string.setting)
            .performClick()
        register(composeTestRule,testDispatcher,false)

        //이전 가입한 이메일로 재가입
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_register_email)
            .performTextClearance()
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_register_email)
            .performTextInput(EMAIL)
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_register_button, useUnmergedTree=true)
            .performClick()
        composeTestRule.runOnIdle {
            testDispatcher.scheduler.advanceUntilIdle()
        }
        composeTestRule.waitForToast(R.string.loading_register)
        composeTestRule.waitForToast(R.string.already_email)

        //이전 가입한 이메일 탈퇴
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_back, useUnmergedTree=true)
            .performClick()
        logIn(composeTestRule,testDispatcher,true)
        composeTestRule.waitForToast(R.string.loading_signIn)
        unregister(composeTestRule,testDispatcher)
    }

    @Test
    fun registerScreen_inputWrongPassword_showCorrectToast(){
        register(composeTestRule,testDispatcher,false)

        //회원 가입 시 빈 비밀번호 검사
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_register_password)
            .performTextClearance()
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_register_button, useUnmergedTree=true)
            .performClick()
        composeTestRule.waitForToast(R.string.blank_password)

        //회원 가입 시 확인용 비밀번호 동일 여부 검사
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_register_password)
            .performTextInput("123")
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_register_button, useUnmergedTree=true)
            .performClick()
        composeTestRule.waitForToast(R.string.invalid_password)

        //회원 가입 시 최소 비밀번호 길이 검사
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_register_verification_password)
            .performTextClearance()
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_register_verification_password)
            .performTextInput("123")
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_register_button, useUnmergedTree=true)
            .performClick()
        composeTestRule.waitForToast(R.string.loading_register)
        composeTestRule.waitForToast(R.string.weak_password)
    }

    @Test
    fun unregisterDialog_inputWrongPassword_showCorrectToast(){
        register(composeTestRule,testDispatcher)
        composeTestRule.waitForToast(R.string.loading_register)
        composeTestRule.waitForToast(R.string.success_register)

        logIn(composeTestRule,testDispatcher,false)
        composeTestRule.waitForToast(R.string.send_email)
        composeTestRule.waitForToast(R.string.loading_signIn)

        //회원 탈퇴 시 잘못된 비밀번호 입력 검사
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_unregister, useUnmergedTree=true)
            .performClick()
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_unregister_password)
            .performTextInput(WRONG_PASSWORD)
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_unregister_confirm, useUnmergedTree=true)
            .performClick()
        composeTestRule.waitForToast(R.string.invalid_password,8000)

        //회원 탈퇴
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_unregister, useUnmergedTree=true)
            .performClick()
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_unregister_password)
            .performTextClearance()
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_unregister_password)
            .performTextInput(PASSWORD)
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

    @Test
    fun logInScreen_inputWrongEmail_showCorrectToast(){
        register(composeTestRule,testDispatcher)
        composeTestRule.waitForToast(R.string.loading_register)
        composeTestRule.waitForToast(R.string.success_register)

        verifyUser(composeTestRule,testDispatcher)
        composeTestRule.waitForToast(R.string.send_email)

        //로그인 시 빈 이메일 검사
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_logIn_email)
            .performImeAction()
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_logIn_password)
            .performTextInput(PASSWORD)
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_logIn, useUnmergedTree = true)
            .performClick()
        composeTestRule.waitForToast(R.string.blank_email)

        //로그인 시 잘못된 이메일 형식 검사
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_logIn_email)
            .performTextInput("email")
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_logIn, useUnmergedTree = true)
            .performClick()
        composeTestRule.waitForToast(R.string.loading_signIn)
        composeTestRule.waitForToast(R.string.invalid_email)

        //로그인 시 틀린 이메일 입력 검사
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_logIn_email)
            .performTextClearance()
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_logIn_email)
            .performTextInput("email@gmail.com")
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_logIn, useUnmergedTree = true)
            .performClick()
        composeTestRule.waitForToast(R.string.loading_signIn)
        composeTestRule.waitForToast(R.string.invalid_credential)

        //로그인
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_logIn_email)
            .performTextClearance()
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_logIn_email)
            .performTextInput(EMAIL)
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_logIn, useUnmergedTree = true)
            .performClick()
        composeTestRule.runOnIdle {
            testDispatcher.scheduler.advanceUntilIdle()
        }
        
        unregister(composeTestRule,testDispatcher)
    }

    @Test
    fun logInScreen_inputWrongPassword_showCorrectToast(){
        register(composeTestRule,testDispatcher)
        composeTestRule.waitForToast(R.string.loading_register)
        composeTestRule.waitForToast(R.string.success_register)
        
        verifyUser(composeTestRule,testDispatcher)
        composeTestRule.waitForToast(R.string.send_email)

        //로그인 시 빈 비밀번호 검사
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_logIn_email)
            .performTextInput(EMAIL)
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_logIn_email)
            .performImeAction()
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_logIn, useUnmergedTree = true)
            .performClick()
        composeTestRule.waitForToast(R.string.blank_password)

        //로그인 시 틀린 비밀번호 입력 검사
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_logIn_password)
            .performTextInput(WRONG_PASSWORD)
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_logIn, useUnmergedTree = true)
            .performClick()
        composeTestRule.waitForToast(R.string.loading_signIn)
        composeTestRule.waitForToast(R.string.invalid_credential)

        //로그인
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_logIn_password)
            .performTextClearance()
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_logIn_password)
            .performTextInput(PASSWORD)
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_logIn, useUnmergedTree = true)
            .performClick()
        composeTestRule.runOnIdle {
            testDispatcher.scheduler.advanceUntilIdle()
        }
        composeTestRule.waitForToast(R.string.loading_signIn)

        unregister(composeTestRule,testDispatcher)
    }

    @Test
    fun editScreen_inputWrongPassword_showCorrectToast(){
        register(composeTestRule,testDispatcher)
        composeTestRule.waitForToast(R.string.loading_register)
        composeTestRule.waitForToast(R.string.success_register)

        logIn(composeTestRule,testDispatcher,false)
        composeTestRule.waitForToast(R.string.send_email)
        composeTestRule.waitForToast(R.string.loading_signIn)

        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit, useUnmergedTree = true)
            .assertExists()
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit, useUnmergedTree = true)
            .performClick()

        //현재 비밀번호 인증 확인 검사
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit_current_password)
            .performTextInput(PASSWORD)
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit_new_password)
            .performTextInput(NEW_PASSWORD)
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit_confirm_new_password)
            .performTextInput(NEW_PASSWORD)
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit_confirm_button, useUnmergedTree = true)
            .performClick()
        composeTestRule.waitForToast(R.string.check_password,8000)

        //틀린 비밀번호 입력 후 인증 확인 검사
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit_current_password)
            .performTextClearance()
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit_current_password)
            .performTextInput(WRONG_PASSWORD)
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit_current_password_button, useUnmergedTree = true)
            .performClick()
        composeTestRule.waitForToast(R.string.invalid_password)

        //현재 비밀번호 입력 후 인증 확인 검사
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit_current_password)
            .performTextClearance()
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit_current_password)
            .performTextInput(PASSWORD)
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit_current_password_button, useUnmergedTree = true)
            .performClick()
        composeTestRule.waitForToast(R.string.already_reauthorization)
        
        //변경할 비밀번호에 일치하지 않는 번호 입력 검사
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit_new_password)
            .performTextClearance()
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit_new_password)
            .performTextInput("123")
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit_current_password_button, useUnmergedTree = true)
            .performClick()
        composeTestRule.waitForToast(R.string.already_reauthorization)
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit_confirm_button, useUnmergedTree = true)
            .performClick()
        composeTestRule.waitForToast(R.string.incorrect_new_password,8000)

        //변경할 비밀번호에 6자 미만 번호 입력 검사
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit_confirm_new_password)
            .performTextClearance()
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit_confirm_new_password)
            .performTextInput("123")
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit_confirm_button, useUnmergedTree = true)
            .performClick()
        composeTestRule.waitForToast(R.string.weak_password)

        //변경할 비밀번호에 올바른 번호 입력 검사
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit_new_password)
            .performTextClearance()
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit_new_password)
            .performTextInput(NEW_PASSWORD)
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit_confirm_new_password)
            .performTextClearance()
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit_confirm_new_password)
            .performTextInput(NEW_PASSWORD)
        composeTestRule
            .onNodeWithTagForStringId(R.string.test_account_edit_confirm_button, useUnmergedTree = true)
            .performClick()
        composeTestRule.waitForToast(R.string.success_edit)
        
        unregister(composeTestRule,testDispatcher,NEW_PASSWORD)
    }

}
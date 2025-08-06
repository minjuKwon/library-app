package com.example.library

import com.example.library.data.User
import com.example.library.domain.UserRepository
import com.example.library.fake.FakeExternalUser
import com.example.library.fake.FakeSessionManager
import com.example.library.service.FirebaseUserService
import com.example.library.service.SaveUserInfoFailedException
import com.example.library.service.SignOutFailedException
import com.example.library.service.VerificationFailedException
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertFailsWith

class FirebaseUserServiceTest {

    private val mockRepo = mockk<UserRepository>()
    private val service = FirebaseUserService(mockRepo, FakeSessionManager())

    @Test
    fun firebaseService_register_verifySuccess()= runTest {
        coEvery { mockRepo.createUser(any(),any()) } returns
                Result.success(FakeExternalUser())
        coEvery { mockRepo.saveUser(any()) } returns Result.success(Unit)
        coEvery { mockRepo.sendVerificationEmail(any()) } returns Result.success(Unit)

        service.register(User(),"")
        coVerifySequence {
            mockRepo.createUser(any(),any())
            mockRepo.saveUser(any())
            mockRepo.sendVerificationEmail(any())
        }
    }

    @Test
    fun firebaseService_register_verifyFailure_createUser() = runTest {
        coEvery { mockRepo.createUser(any(),any()) } returns Result.failure(Exception())

        assertFailsWith<Exception>{
            service.register(User(),"")
        }
        coVerify{
            mockRepo.createUser(any(),any())
        }
    }

    @Test
    fun firebaseService_register_verifyFailure_saveUser() = runTest {
        coEvery { mockRepo.createUser(any(),any()) } returns
                Result.success(FakeExternalUser())
        coEvery { mockRepo.saveUser(any()) } returns Result.failure(Exception())
        coEvery { mockRepo.deleteUserAccount(any()) } returns Result.success(Unit)

        assertFailsWith<SaveUserInfoFailedException>{
            service.register(User(),"")
        }
        coVerifySequence {
            mockRepo.createUser(any(),any())
            mockRepo.saveUser(any())
            mockRepo.deleteUserAccount(any())
        }
    }

    @Test
    fun firebaseService_unregister_verifySuccess()= runTest {
        coEvery { mockRepo.reAuthenticateUser(any()) } returns
                Result.success(FakeExternalUser())
        coEvery { mockRepo.deleteUserData(any()) } returns Result.success(User())
        coEvery { mockRepo.deleteUserAccount(any()) } returns Result.success(Unit)

        service.unregister("")
        coVerifySequence {
            mockRepo.reAuthenticateUser(any())
            mockRepo.deleteUserData(any())
            mockRepo.deleteUserAccount(any())
        }
    }

    @Test
    fun firebaseService_unregister_verifyFailure_reAuthenticateUser()= runTest {
        coEvery { mockRepo.reAuthenticateUser(any()) } returns Result.failure(Exception())

        assertFailsWith<Exception>{
            service.unregister("")
        }
        coVerify{
            mockRepo.reAuthenticateUser(any())
        }
    }

    @Test
    fun firebaseService_unregister_verifyFailure_deleteUserData()= runTest {
        coEvery { mockRepo.reAuthenticateUser(any()) } returns
                Result.success(FakeExternalUser())
        coEvery { mockRepo.deleteUserData(any()) } returns Result.failure(Exception())

        assertFailsWith<Exception>{
            service.unregister("")
        }
        coVerify{
            mockRepo.reAuthenticateUser(any())
            mockRepo.deleteUserData(any())
        }
    }

    @Test
    fun firebaseService_unregister_verifyFailure_deleteUserAccount()= runTest {
        coEvery { mockRepo.reAuthenticateUser(any()) } returns
                Result.success(FakeExternalUser())
        coEvery { mockRepo.deleteUserData(any()) } returns Result.success(User())
        coEvery { mockRepo.deleteUserAccount(any()) } returns Result.failure(Exception())
        coEvery { mockRepo.saveUser(any()) } returns Result.success(Unit)

        assertFailsWith<Exception>{
            service.unregister("")
        }
        coVerify{
            mockRepo.reAuthenticateUser(any())
            mockRepo.deleteUserData(any())
            mockRepo.deleteUserAccount(any())
            mockRepo.saveUser(any())
        }
    }

    @Test
    fun firebaseService_changeUserInfo_verifySuccess()= runTest {
        coEvery { mockRepo.updateUser(any()) } returns
                Result.success(Unit)

        service.changeUserInfo(mapOf())
        coVerifySequence {
            mockRepo.updateUser(any())
        }
    }

    @Test
    fun firebaseService_changeUserInfo_verifyFailure()= runTest {
        coEvery { mockRepo.updateUser(any()) } returns Result.failure(Exception())

        assertFailsWith<Exception>{
            service.changeUserInfo(mapOf())
        }
        coVerify{
            mockRepo.updateUser(any())
        }
    }

    @Test
    fun firebaseService_signIn_verifySuccess_isEmailVerified()= runTest {
        val fakeSessionManager = FakeSessionManager()
        val spySessionManager = spyk(fakeSessionManager)
        every { spySessionManager.userPreferences } returns flowOf(User())

        val fakeService= FirebaseUserService(mockRepo, spySessionManager)
        val fakeUser = FakeExternalUser(isEmailVerified = true)
        verify { spySessionManager.userPreferences }

        coEvery { mockRepo.signInUser(any(),any()) } returns
                Result.success(fakeUser)
        coEvery { mockRepo.getUser(any()) } returns Result.success(User())
        coEvery { spySessionManager.saveUserData(any()) } just Runs

        fakeService.signIn("","")
        coVerify {
            mockRepo.signInUser(any(),any())
            mockRepo.getUser(any())
            spySessionManager.saveUserData(any())
        }
    }

    @Test
    fun firebaseService_signIn_verifyFailure_signInUser()= runTest {
        coEvery { mockRepo.signInUser(any(),any()) } returns Result.failure(Exception())

        assertFailsWith<Exception>{
            service.signIn("","")
        }
        coVerify {
            mockRepo.signInUser(any(),any())
        }
    }

    @Test
    fun firebaseService_signIn_verifyFailure_isNotEmailVerified()= runTest {
        coEvery { mockRepo.signInUser(any(),any()) } returns
                Result.success(FakeExternalUser())
        coEvery { mockRepo.sendVerificationEmail(any()) } returns
                Result.failure(VerificationFailedException())

        assertFailsWith<VerificationFailedException>{
            service.signIn("","")
        }
        coVerify {
            mockRepo.signInUser(any(),any())
            mockRepo.sendVerificationEmail(any())
        }
    }

    @Test
    fun firebaseService_signIn_verifyFailure_getUser()= runTest {
        val fakeUser = FakeExternalUser(isEmailVerified = true)
        coEvery { mockRepo.signInUser(any(),any()) } returns
                Result.success(fakeUser)
        coEvery { mockRepo.getUser(any()) } returns Result.failure(Exception())

        assertFailsWith<Exception>{
            service.signIn("","")
        }
        coVerify {
            mockRepo.signInUser(any(),any())
            mockRepo.getUser(any())
        }
    }

    @Test
    fun firebaseService_signOut_verifySuccess()= runTest {
        val fakeSessionManager = FakeSessionManager()
        val spySessionManager = spyk(fakeSessionManager)
        val fakeService = FirebaseUserService(mockRepo, spySessionManager)

        every{ spySessionManager.userPreferences} returns flowOf(User())
        every { spySessionManager.logInPreferences } returns flowOf(true)

        coEvery{ mockRepo.signOutUser() } returns Result.success(Unit)
        coEvery { spySessionManager.removeUserData() } just Runs
        coEvery { spySessionManager.removeLogInState() } just Runs

        fakeService.signOut()
        coVerifySequence {
            spySessionManager.userPreferences
            spySessionManager.logInPreferences
            mockRepo.signOutUser()
            spySessionManager.removeUserData()
            spySessionManager.removeLogInState()
        }
    }

    @Test
    fun firebaseService_signOut_verifyFailure()= runTest{
        coEvery{ mockRepo.signOutUser() } returns Result.failure(SignOutFailedException())

        assertFailsWith<SignOutFailedException> {
            service.signOut()
        }

        coVerify {
            mockRepo.signOutUser()
        }
    }

}
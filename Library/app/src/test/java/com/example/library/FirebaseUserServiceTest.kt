package com.example.library

import com.example.library.data.User
import com.example.library.domain.UserRepository
import com.example.library.fake.FakeExternalUser
import com.example.library.fake.FakeSessionManager
import com.example.library.service.FirebaseUserService
import com.example.library.service.SaveUserInfoFailedException
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifySequence
import io.mockk.mockk
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

}
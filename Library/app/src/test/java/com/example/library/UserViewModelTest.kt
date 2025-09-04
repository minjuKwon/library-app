package com.example.library

import com.example.library.data.entity.User
import com.example.library.fake.FakeUserService
import com.example.library.rules.TestDispatcherRule
import com.example.library.ui.screens.user.UserUiState
import com.example.library.ui.screens.user.UserViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserViewModelTest {

    private lateinit var fakeService:FakeUserService
    private lateinit var viewModel:UserViewModel
    private lateinit var testScope:CoroutineScope

    @get:Rule
    val testDispatcherRule= TestDispatcherRule()

    @Before
    fun setUpt(){
        fakeService = FakeUserService()
        testScope = CoroutineScope(testDispatcherRule.testDispatcher)
        viewModel = UserViewModel(fakeService, testScope)
    }

    @After
    fun closeResource(){
        testScope.cancel()
    }

    @Test
    fun userViewModel_register_verifyUserUiStateSuccess()= runTest {
        fakeService.isThrowException= false
        val emittedStates = mutableListOf<UserUiState>()
        val job = launch {
            viewModel.event.collect {
                emittedStates.add(it)
            }
        }

        viewModel.register(User(),"")
        testScheduler.advanceUntilIdle()
        Assert.assertFalse(viewModel.isUserVerified.value)
        Assert.assertEquals(UserUiState.Success(), emittedStates.last())

        job.cancel()
    }

    @Test
    fun userViewModel_register_verifyUserUiStateFailure()= runTest {
        fakeService.isThrowException= true
        val emittedStates = mutableListOf<UserUiState>()
        val job = launch {
            viewModel.event.collect {
                emittedStates.add(it)
            }
        }

        viewModel.register(User(),"")
        testScheduler.advanceUntilIdle()

        Assert.assertEquals(UserUiState.Failure("fail"),emittedStates.last())

        job.cancel()
    }

    @Test
    fun userViewModel_unregister_verifyUserUiStateSuccess()= runTest {
        fakeService.isThrowException= false
        val emittedStates = mutableListOf<UserUiState>()
        val job = launch {
            viewModel.event.collect {
                emittedStates.add(it)
            }
        }

        viewModel.unregister("")
        testScheduler.advanceUntilIdle()

        Assert.assertEquals(UserUiState.Success(), emittedStates.last())

        job.cancel()
    }

    @Test
    fun userViewModel_unregister_verifyUserUiStateFailure()= runTest {
        fakeService.isThrowException= true
        val emittedStates = mutableListOf<UserUiState>()
        val job = launch {
            viewModel.event.collect {
                emittedStates.add(it)
            }
        }

        viewModel.unregister("")
        testScheduler.advanceUntilIdle()

        Assert.assertEquals(UserUiState.Failure("fail"),emittedStates.last())

        job.cancel()
    }

    @Test
    fun userViewModel_changeUserInfo_verifyUserUiStateSuccess()= runTest {
        fakeService.isThrowException= false
        val emittedStates = mutableListOf<UserUiState>()
        val job = launch {
            viewModel.event.collect {
                emittedStates.add(it)
            }
        }

        viewModel.changeUserInfo(mapOf())
        testScheduler.advanceUntilIdle()

        Assert.assertEquals(UserUiState.Success(), emittedStates.last())

        job.cancel()
    }

    @Test
    fun userViewModel_changeUserInfo_verifyUserUiStateFailure()= runTest {
        fakeService.isThrowException= true
        val emittedStates = mutableListOf<UserUiState>()
        val job = launch {
            viewModel.event.collect {
                emittedStates.add(it)
            }
        }

        viewModel.changeUserInfo(mapOf())
        testScheduler.advanceUntilIdle()

        Assert.assertEquals(UserUiState.Failure("fail"),emittedStates.last())

        job.cancel()
    }

    @Test
    fun userViewModel_signIn_verifyUserUiStateSuccess()= runTest {
        fakeService.isThrowException= false
        val emittedStates = mutableListOf<UserUiState>()
        val job = launch {
            viewModel.event.collect {
                emittedStates.add(it)
            }
        }

        viewModel.signIn("","")
        testScheduler.advanceUntilIdle()

        Assert.assertEquals(UserUiState.Success(), emittedStates.last())

        job.cancel()
    }

    @Test
    fun userViewModel_signIn_verifyUserUiStateFailure()= runTest {
        fakeService.isThrowException= true
        val emittedStates = mutableListOf<UserUiState>()
        val job = launch {
            viewModel.event.collect {
                emittedStates.add(it)
            }
        }

        viewModel.signIn("","")
        testScheduler.advanceUntilIdle()

        Assert.assertEquals(UserUiState.Failure("fail"),emittedStates.last())

        job.cancel()
    }

    @Test
    fun userViewModel_signOut_verifyUserUiStateSuccess()= runTest {
        fakeService.isThrowException= false
        val emittedStates = mutableListOf<UserUiState>()
        val job = launch {
            viewModel.event.collect {
                emittedStates.add(it)
            }
        }

        viewModel.signOut()
        testScheduler.advanceUntilIdle()

        Assert.assertEquals(UserUiState.Success(), emittedStates.last())

        job.cancel()
    }

    @Test
    fun userViewModel_signOut_verifyUserUiStateFailure()= runTest {
        fakeService.isThrowException= true
        val emittedStates = mutableListOf<UserUiState>()
        val job = launch {
            viewModel.event.collect {
                emittedStates.add(it)
            }
        }

        viewModel.signOut()
        testScheduler.advanceUntilIdle()

        Assert.assertEquals(UserUiState.Failure("fail"),emittedStates.last())

        job.cancel()
    }

    @Test
    fun userViewModel_sendVerificationEmail_verifyUserUiStateSuccess()= runTest {
        fakeService.isThrowException= false
        val emittedStates = mutableListOf<UserUiState>()
        val job = launch {
            viewModel.event.collect {
                emittedStates.add(it)
            }
        }

        viewModel.sendVerificationEmail()
        testScheduler.advanceUntilIdle()

        Assert.assertEquals(UserUiState.Success(), emittedStates.last())

        job.cancel()
    }

    @Test
    fun userViewModel_sendVerificationEmail_verifyUserUiStateFailure()= runTest {
        fakeService.isThrowException= true
        val emittedStates = mutableListOf<UserUiState>()
        val job = launch {
            viewModel.event.collect {
                emittedStates.add(it)
            }
        }

        viewModel.sendVerificationEmail()
        testScheduler.advanceUntilIdle()

        Assert.assertEquals(UserUiState.Failure("fail"),emittedStates.last())

        job.cancel()
    }

    @Test
    fun userViewModel_verifyCurrentPassword_verifyUserUiStateSuccess()= runTest {
        fakeService.isThrowException= false

        viewModel.verifyCurrentPassword("")
        testScheduler.advanceUntilIdle()

        Assert.assertTrue(viewModel.isPasswordVerified.value)
    }

    @Test
    fun userViewModel_verifyCurrentPassword_verifyUserUiStateFailure()= runTest {
        fakeService.isThrowException= true
        val emittedStates = mutableListOf<UserUiState>()
        val job = launch {
            viewModel.event.collect {
                emittedStates.add(it)
            }
        }

        viewModel.verifyCurrentPassword("")
        testScheduler.advanceUntilIdle()

        Assert.assertEquals(UserUiState.Failure("fail"),emittedStates.last())

        job.cancel()
    }

    @Test
    fun userViewModel_changePassword_verifyUserUiStateSuccess()= runTest {
        fakeService.isThrowException= false
        val emittedStates = mutableListOf<UserUiState>()
        val job = launch {
            viewModel.event.collect {
                emittedStates.add(it)
            }
        }
        viewModel.isPasswordVerified.value=true

        viewModel.changePassword("")
        testScheduler.advanceUntilIdle()

        Assert.assertTrue(viewModel.isPasswordVerified.value)
        Assert.assertEquals(UserUiState.Success(), emittedStates.last())

        job.cancel()
    }

    @Test
    fun userViewModel_changePassword_verifyUserUiStateFailure()= runTest {
        fakeService.isThrowException= true
        val emittedStates = mutableListOf<UserUiState>()
        val job = launch {
            viewModel.event.collect {
                emittedStates.add(it)
            }
        }
        viewModel.isPasswordVerified.value=true
        viewModel.changePassword("")
        testScheduler.advanceUntilIdle()

        Assert.assertEquals(UserUiState.Failure("fail"),emittedStates.last())

        job.cancel()
    }

    @Test
    fun userViewModel_findPassword_verifyUserUiStateSuccess()= runTest {
        fakeService.isThrowException= false
        val emittedStates = mutableListOf<UserUiState>()
        val job = launch {
            viewModel.event.collect {
                emittedStates.add(it)
            }
        }

        viewModel.findPassword("")
        testScheduler.advanceUntilIdle()

        Assert.assertTrue(emittedStates.isEmpty())

        job.cancel()
    }

    @Test
    fun userViewModel_findPassword_verifyUserUiStateFailure()= runTest {
        fakeService.isThrowException= true
        val emittedStates = mutableListOf<UserUiState>()
        val job = launch {
            viewModel.event.collect {
                emittedStates.add(it)
            }
        }

        viewModel.findPassword("")
        testScheduler.advanceUntilIdle()

        Assert.assertEquals(UserUiState.Failure("fail"),emittedStates.last())

        job.cancel()
    }


    @Test
    fun userViewModel_checkUserVerified_returnCorrectValue()= runTest {
        val emittedStates = mutableListOf<UserUiState>()
        val job = launch {
            viewModel.event.collect {
                emittedStates.add(it)
            }
        }

        viewModel.checkUserVerified()
        testScheduler.advanceUntilIdle()

        Assert.assertTrue(viewModel.isUserVerified.value)

        job.cancel()
    }


    @Test
    fun userViewModel_updatePasswordVerifiedState_returnCorrectValue()= runTest {
        val emittedStates = mutableListOf<UserUiState>()
        val job = launch {
            viewModel.event.collect {
                emittedStates.add(it)
            }
        }
        viewModel.updatePasswordVerifiedState(true)
        testScheduler.advanceUntilIdle()

        Assert.assertTrue(viewModel.isPasswordVerified.value)

        job.cancel()
    }


    @Test
    fun userViewModel_updateEmailVerifiedState_returnCorrectValue()= runTest {
        val emittedStates = mutableListOf<UserUiState>()
        val job = launch {
            viewModel.event.collect {
                emittedStates.add(it)
            }
        }

        viewModel.updateEmailVerifiedState(true)
        testScheduler.advanceUntilIdle()

        Assert.assertTrue(viewModel.isClickEmailLink.value)

        job.cancel()
    }


}

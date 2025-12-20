package com.example.library

import com.example.library.data.entity.User
import com.example.library.fake.FakeTimeProvider
import com.example.library.fake.repository.FakeBookRepository
import com.example.library.fake.service.FakeUserService
import com.example.library.rules.TestDispatcherRule
import com.example.library.service.FirebaseBookService
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

    private lateinit var fakeUserService: FakeUserService
    private lateinit var viewModel:UserViewModel
    private lateinit var testScope:CoroutineScope

    @get:Rule
    val testDispatcherRule= TestDispatcherRule()

    @Before
    fun setUpt(){
        val dummyFirebaseBookService= FirebaseBookService(FakeBookRepository(), FakeTimeProvider())
        fakeUserService = FakeUserService()
        testScope = CoroutineScope(testDispatcherRule.testDispatcher)
        viewModel = UserViewModel(fakeUserService, dummyFirebaseBookService, testScope)
    }

    @After
    fun closeResource(){
        testScope.cancel()
    }

    @Test
    fun userViewModel_register_verifyUserUiStateSuccess()= runTest {
        fakeUserService.isThrowException= false
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
        fakeUserService.isThrowException= true
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
        fakeUserService.isThrowException= false
        val emittedStates = mutableListOf<UserUiState>()
        val job = launch {
            viewModel.event.collect {
                emittedStates.add(it)
            }
        }

        viewModel.unregister("")
        testScheduler.advanceUntilIdle()

        Assert.assertEquals(UserUiState.Success("unregister"), emittedStates.last())

        job.cancel()
    }

    @Test
    fun userViewModel_unregister_verifyUserUiStateFailure()= runTest {
        fakeUserService.isThrowException= true
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
        fakeUserService.isThrowException= false
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
        fakeUserService.isThrowException= true
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
        fakeUserService.isThrowException= false
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
        fakeUserService.isThrowException= true
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
        fakeUserService.isThrowException= false
        val emittedStates = mutableListOf<UserUiState>()
        val job = launch {
            viewModel.event.collect {
                emittedStates.add(it)
            }
        }

        viewModel.signOut()
        testScheduler.advanceUntilIdle()

        Assert.assertEquals(UserUiState.Success("signOut"), emittedStates.last())

        job.cancel()
    }

    @Test
    fun userViewModel_signOut_verifyUserUiStateFailure()= runTest {
        fakeUserService.isThrowException= true
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
        fakeUserService.isThrowException= false
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
        fakeUserService.isThrowException= true
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
        fakeUserService.isThrowException= false

        viewModel.verifyCurrentPassword("")
        testScheduler.advanceUntilIdle()

        Assert.assertTrue(viewModel.isPasswordVerified.value)
    }

    @Test
    fun userViewModel_verifyCurrentPassword_verifyUserUiStateFailure()= runTest {
        fakeUserService.isThrowException= true
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
        fakeUserService.isThrowException= false
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
        fakeUserService.isThrowException= true
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
        fakeUserService.isThrowException= false
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
        fakeUserService.isThrowException= true
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

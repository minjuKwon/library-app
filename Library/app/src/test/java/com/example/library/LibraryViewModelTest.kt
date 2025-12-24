package com.example.library

import com.example.library.data.entity.LibraryReservation
import com.example.library.data.entity.ReservationStatusType
import com.example.library.fake.repository.FakeBookRepository
import com.example.library.fake.repository.FakeCacheBookRepository
import com.example.library.fake.repository.FakeNetworkBookRepository
import com.example.library.fake.FakeSessionManager
import com.example.library.fake.FakeSessionManager.Companion.UID
import com.example.library.fake.FakeTimeProvider
import com.example.library.fake.repository.FakeExceptionNetworkBookRepository
import com.example.library.rules.TestDispatcherRule
import com.example.library.service.CacheBookService
import com.example.library.service.DefaultLibrarySyncService
import com.example.library.service.FirebaseBookService
import com.example.library.ui.screens.search.LibraryUiState
import com.example.library.ui.screens.search.LibraryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class LibraryViewModelTest {

    @get:Rule
    val testDispatcherRule= TestDispatcherRule()

    @Test
    fun libraryViewModel_getBookListInformation_verifyLibraryUiStateSuccess()= runTest {
        val testScope = CoroutineScope(testDispatcherRule.testDispatcher)
        val fakeRepository = FakeNetworkBookRepository()

        val fakeBookRepository= FakeBookRepository()
        val fakeFirebaseBookService= FirebaseBookService(fakeBookRepository, FakeTimeProvider())

        val expectedItems = fakeRepository.searchVolume(
            "query",
            10,
            0
        ).getOrNull()

        expectedItems?.book?.forEachIndexed { index, book ->
            if(index%2==0){
                for(i in 1..index){
                    fakeFirebaseBookService.updateLibraryLiked("user${i}", book.id, true)
                }
            }
        }

        val fakeLibrarySyncService= DefaultLibrarySyncService(
            fakeRepository,
            CacheBookService(FakeCacheBookRepository(), FakeTimeProvider()),
            fakeFirebaseBookService
        )
        val libraryViewModel = LibraryViewModel(
            librarySyncService = fakeLibrarySyncService,
            firebaseBookService = fakeFirebaseBookService,
            defaultSessionManager = FakeSessionManager(),
            externalScope = testScope
        )

        Assert.assertEquals(LibraryUiState.Loading, libraryViewModel.libraryUiState)

        testScheduler.advanceUntilIdle()

        val successState = (libraryViewModel.libraryUiState as LibraryUiState.Success)
        val actualItems = successState.list.map { it.library.book }

        Assert.assertEquals(expectedItems?.book, actualItems)

        successState.list.forEachIndexed { index, libraryUiModel ->
            if(index%2==0){
                Assert.assertEquals(index, libraryUiModel.count)
                if(index!=0) Assert.assertTrue(libraryUiModel.isLiked)
            } else{
                Assert.assertFalse(libraryUiModel.isLiked)
            }
        }

        Assert.assertEquals(expectedItems?.totalCount, successState.totalCount)

        testScope.cancel()
    }

    @Test
    fun libraryViewModel_getBookListInformation_verityLibraryUiStateError()= runTest {
        val testScope = CoroutineScope(testDispatcherRule.testDispatcher)
        val fakeFirebaseBookService= FirebaseBookService(FakeBookRepository(), FakeTimeProvider())

        val fakeLibrarySyncService= DefaultLibrarySyncService(
            FakeExceptionNetworkBookRepository(),
            CacheBookService(FakeCacheBookRepository(), FakeTimeProvider()),
            fakeFirebaseBookService
        )

        val viewModel = LibraryViewModel(
            librarySyncService = fakeLibrarySyncService,
            firebaseBookService= fakeFirebaseBookService,
            defaultSessionManager = FakeSessionManager(),
            externalScope = testScope
        )

        Assert.assertTrue(viewModel.libraryUiState is LibraryUiState.Loading)

        testScheduler.advanceUntilIdle()

        Assert.assertTrue(viewModel.libraryUiState is LibraryUiState.Error)

        testScope.cancel()
    }

    @Test
    fun libraryViewModel_toggleLike_verifyCorrectValue()= runTest {
        val testScope = CoroutineScope(testDispatcherRule.testDispatcher)
        val fakeRepository = FakeNetworkBookRepository()

        val fakeBookRepository= FakeBookRepository()
        val fakeFirebaseBookService= FirebaseBookService(fakeBookRepository, FakeTimeProvider())

        val fakeLibrarySyncService= DefaultLibrarySyncService(
            fakeRepository,
            CacheBookService(FakeCacheBookRepository(), FakeTimeProvider()),
            fakeFirebaseBookService
        )
        val libraryViewModel = LibraryViewModel(
            librarySyncService = fakeLibrarySyncService,
            firebaseBookService = fakeFirebaseBookService,
            defaultSessionManager = FakeSessionManager(),
            externalScope = testScope
        )

        testScheduler.advanceUntilIdle()

        var successState = (libraryViewModel.libraryUiState as LibraryUiState.Success)

        successState.list.forEachIndexed { index, libraryUiModel ->
            if(index%2==1){
                libraryViewModel.toggleLike(libraryUiModel.library.book.id, true)
                testScheduler.advanceUntilIdle()
            }
        }

        successState = (libraryViewModel.libraryUiState as LibraryUiState.Success)

        successState.list.forEachIndexed { index, libraryUiModel ->
            if(index%2==1){
                Assert.assertTrue(libraryUiModel.isLiked)
            } else{
                Assert.assertFalse(libraryUiModel.isLiked)
            }
        }

    }

    @Test
    fun libraryViewModel_getLiked_verifyCorrectValue()= runTest {
        val testScope = CoroutineScope(testDispatcherRule.testDispatcher)
        val fakeRepository = FakeNetworkBookRepository()
        val fakeFirebaseBookService= FirebaseBookService(FakeBookRepository(), FakeTimeProvider())

        val fakeLibrarySyncService= DefaultLibrarySyncService(
            fakeRepository,
            CacheBookService(FakeCacheBookRepository(), FakeTimeProvider()),
            fakeFirebaseBookService
        )
        val libraryViewModel = LibraryViewModel(
            librarySyncService = fakeLibrarySyncService,
            firebaseBookService = fakeFirebaseBookService,
            defaultSessionManager = FakeSessionManager(),
            externalScope = testScope
        )

        testScheduler.advanceUntilIdle()

        var successState = (libraryViewModel.libraryUiState as LibraryUiState.Success)

        successState.list.forEachIndexed { index, libraryUiModel ->
            if(index%2==0){
                fakeFirebaseBookService.updateLibraryLiked(UID, libraryUiModel.library.book.id, true)
            }
        }

        libraryViewModel.getLiked()
        testScheduler.advanceUntilIdle()

        successState = (libraryViewModel.libraryUiState as LibraryUiState.Success)

        successState.list.forEachIndexed { index, libraryUiModel ->
            if(index%2==0){
                Assert.assertTrue(libraryUiModel.isLiked)
            }else{
                Assert.assertFalse(libraryUiModel.isLiked)
            }
        }

    }

    @Test
    fun libraryViewModel_resetLiked_verifyCorrectValue()= runTest {
        val testScope = CoroutineScope(testDispatcherRule.testDispatcher)
        val fakeRepository = FakeNetworkBookRepository()

        val fakeBookRepository= FakeBookRepository()
        val fakeFirebaseBookService= FirebaseBookService(fakeBookRepository, FakeTimeProvider())

        val fakeLibrarySyncService= DefaultLibrarySyncService(
            fakeRepository,
            CacheBookService(FakeCacheBookRepository(), FakeTimeProvider()),
            fakeFirebaseBookService
        )
        val libraryViewModel = LibraryViewModel(
            librarySyncService = fakeLibrarySyncService,
            firebaseBookService = fakeFirebaseBookService,
            defaultSessionManager = FakeSessionManager(),
            externalScope = testScope
        )

        testScheduler.advanceUntilIdle()

        var successState = (libraryViewModel.libraryUiState as LibraryUiState.Success)

        successState.list.forEachIndexed { index, libraryUiModel ->
            if(index%2==1){
                fakeFirebaseBookService.updateLibraryLiked(UID, libraryUiModel.library.book.id, true)
            }
        }

        libraryViewModel.resetLiked()
        testScheduler.advanceUntilIdle()

        successState = (libraryViewModel.libraryUiState as LibraryUiState.Success)

        successState.list.forEach { libraryUiModel ->
            Assert.assertFalse(libraryUiModel.isLiked)
        }

    }

    @Test
    fun libraryViewModel_checkMyReservationTurn_verifyCorrectValue()= runTest {
        val testScope = CoroutineScope(testDispatcherRule.testDispatcher)
        val fakeBookRepository= FakeBookRepository()
        fakeBookRepository.addReservationList(
            LibraryReservation(
                userId = "user1",
                bookTitle = "book1",
                status = ReservationStatusType.NOTIFIED.name
            )
        )
        val fakeFirebaseBookService= FirebaseBookService(fakeBookRepository, FakeTimeProvider())

        val fakeLibrarySyncService= DefaultLibrarySyncService(
            FakeNetworkBookRepository(),
            CacheBookService(FakeCacheBookRepository(), FakeTimeProvider()),
            fakeFirebaseBookService
        )
        val libraryViewModel = LibraryViewModel(
            librarySyncService = fakeLibrarySyncService,
            firebaseBookService = fakeFirebaseBookService,
            defaultSessionManager = FakeSessionManager(),
            externalScope = testScope
        )

        testScheduler.advanceUntilIdle()

        assertEquals("book1", libraryViewModel.reservedBookList.value[0])
    }

}
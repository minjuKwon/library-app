package com.example.library

import com.example.library.fake.repository.FakeBookRepository
import com.example.library.fake.repository.FakeCacheBookRepository
import com.example.library.fake.repository.FakeExceptionBookRepository
import com.example.library.fake.repository.FakeNetworkBookRepository
import com.example.library.fake.FakeSessionManager
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

class LibraryViewModelTest {

    @get:Rule
    val testDispatcherRule= TestDispatcherRule()

    @Test
    fun libraryViewModel_getBookListInformation_verifyLibraryUiStateSuccess()= runTest {
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

        Assert.assertEquals(LibraryUiState.Loading, libraryViewModel.libraryUiState)

        testScheduler.advanceUntilIdle()
        testScheduler.advanceUntilIdle()

        val successState = (libraryViewModel.libraryUiState as LibraryUiState.Success)
        val actualItems = successState.list.map { it.library.book }
        val expectedItems = fakeRepository.searchVolume(
            libraryViewModel.textFieldKeyword.value,
            10,
            0
        ).getOrNull()

        Assert.assertEquals(expectedItems?.book, actualItems)
        Assert.assertEquals(expectedItems?.totalCount, successState.totalCount)

        testScope.cancel()

    }

    @Test
    fun libraryViewModel_getBookListInformation_verifyLibraryBookmark()= runTest {

        val fakeRepository = FakeNetworkBookRepository()
        val testScope = CoroutineScope(testDispatcherRule.testDispatcher)
        val libraryViewModel = LibraryViewModel(
            bookRepository = fakeRepository,
            ioDispatcher = testDispatcherRule.testDispatcher,
            externalScope = testScope
        )
        testScheduler.advanceUntilIdle()

        //bookmark 리스트 확인
        val successState = (libraryViewModel.libraryUiState as LibraryUiState.Success)
        val pagingData = successState.list.book.first()
        val differ = AsyncPagingDataDiffer(
            diffCallback = MyDiffCallback(),
            updateCallback = NoopListCallback,
            workerDispatcher = testDispatcherRule.testDispatcher
        )

        launch {
            flowOf(pagingData).collectLatest { differ.submitData(it) }
        }

        testScheduler.advanceUntilIdle()

        val actualItems = differ.snapshot().items
        for (book in actualItems) {
            libraryViewModel.updateBookmarkList(book)
        }
        var expectedItems = FakeBookmarkedBookRepository().searchVolume(
            libraryViewModel.textFieldKeyword.value,
            10,
            0
        ).book

        Assert.assertEquals(expectedItems, actualItems)

        //bookmark 해제된  리스트 확인
        for (book in actualItems) {
            libraryViewModel.updateBookmarkList(book)
        }

        expectedItems = FakeNetworkBookRepository().searchVolume(
            libraryViewModel.textFieldKeyword.value,
            10,
            0
        ).book

        Assert.assertEquals(expectedItems, actualItems)

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

}
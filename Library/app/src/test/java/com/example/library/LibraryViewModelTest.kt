package com.example.library

import androidx.paging.AsyncPagingDataDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.library.data.entity.Book
import com.example.library.fake.FakeBookmarkedBookRepository
import com.example.library.fake.FakeExceptionBookRepository
import com.example.library.fake.FakeNetworkBookRepository
import com.example.library.rules.TestDispatcherRule
import com.example.library.ui.screens.search.LibraryUiState
import com.example.library.ui.screens.search.LibraryViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Rule
import org.junit.Test

class LibraryViewModelTest {

    @get:Rule
    val testDispatcherRule= TestDispatcherRule()

    object NoopListCallback : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) = Unit
        override fun onRemoved(position: Int, count: Int) = Unit
        override fun onMoved(fromPosition: Int, toPosition: Int) = Unit
        override fun onChanged(position: Int, count: Int, payload: Any?) = Unit
    }

    @Test
    fun libraryViewModel_getBookListInformation_verifyLibraryUiStateSuccess()= runTest {

        val fakeRepository = FakeNetworkBookRepository()

        val testScope = CoroutineScope(testDispatcherRule.testDispatcher)
        val libraryViewModel = LibraryViewModel(
            bookRepository = fakeRepository,
            ioDispatcher = testDispatcherRule.testDispatcher,
            externalScope = testScope
        )

        Assert.assertEquals(LibraryUiState.Loading, libraryViewModel.libraryUiState)

        testScheduler.advanceUntilIdle()

        val successState = (libraryViewModel.libraryUiState as LibraryUiState.Success)
        val pagingData = successState.list.book.first()
        val differ = AsyncPagingDataDiffer(
            diffCallback = MyDiffCallback(),
            updateCallback = NoopListCallback,
            workerDispatcher = testDispatcherRule.testDispatcher
        )

        // collectLatest 사용하여 강제 수집 및 완료 처리
        launch {
            flowOf(pagingData).collectLatest { differ.submitData(it) }
        }

        testScheduler.advanceUntilIdle()

        val actualItems = differ.snapshot().items
        val expectedItems = fakeRepository.searchVolume(
            libraryViewModel.textFieldKeyword.value,
            10,
            0
        ).book

        Assert.assertEquals(expectedItems, actualItems)

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

        val fakeRepository = FakeExceptionBookRepository()
        val testScope = CoroutineScope(testDispatcherRule.testDispatcher)
        val viewModel = LibraryViewModel(
            bookRepository = fakeRepository,
            ioDispatcher = testDispatcherRule.testDispatcher,
            externalScope = testScope
        )

        testScheduler.advanceUntilIdle()

        Assert.assertTrue(viewModel.libraryUiState is LibraryUiState.Error)
    }

}

private class MyDiffCallback : DiffUtil.ItemCallback<Book>() {
    override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem == newItem
    }
}
package com.example.library

import androidx.paging.AsyncPagingDataDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.library.fake.FakeNetworkBookshelfRepository
import com.example.library.network.Book
import com.example.library.rules.TestDispatcherRule
import com.example.library.ui.BookshelfUiState
import com.example.library.ui.BookshelfViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOf

class BookshelfViewModelTest {

    @get:Rule
    val testDispatcherRule= TestDispatcherRule()

    object NoopListCallback : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) = Unit
        override fun onRemoved(position: Int, count: Int) = Unit
        override fun onMoved(fromPosition: Int, toPosition: Int) = Unit
        override fun onChanged(position: Int, count: Int, payload: Any?) = Unit
    }

    @Test
    fun bookshelfViewModel_getBookListInformation_verifyBookshelfUiStateSuccess()= runTest{

        val fakeRepository= FakeNetworkBookshelfRepository()

        val testScope = CoroutineScope(testDispatcherRule.testDispatcher)
        val bookshelfViewModel=BookshelfViewModel(
            bookshelfRepository = fakeRepository,
            ioDispatcher = testDispatcherRule.testDispatcher,
            externalScope = testScope
        )

        assertEquals(BookshelfUiState.Loading, bookshelfViewModel.bookshelfUiState)

        testScheduler.advanceUntilIdle()

        val successState  = (bookshelfViewModel.bookshelfUiState as BookshelfUiState.Success)
        val pagingData = successState.list.book.first()
        val differ = AsyncPagingDataDiffer(
            diffCallback = MyDiffCallback(),
            updateCallback = NoopListCallback,
            workerDispatcher = testDispatcherRule.testDispatcher
        )

        launch {
            flowOf(pagingData).collectLatest {
                differ.submitData(it)
            }
        }

        testScheduler.advanceUntilIdle()

        val actualItems = differ.snapshot().items
        val expectedItems=fakeRepository.getBookListInformation(
            bookshelfViewModel.textFieldKeyword.value,
            10,
            0
        ).book

        assertEquals(expectedItems, actualItems)

        testScope.cancel()

        }
}

class MyDiffCallback : DiffUtil.ItemCallback<Book>() {
    override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
        return oldItem == newItem
    }
}
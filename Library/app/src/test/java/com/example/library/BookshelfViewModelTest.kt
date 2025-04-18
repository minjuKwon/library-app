package com.example.library

import com.example.library.fake.FakeNetworkBookshelfRepository
import com.example.library.rules.TestDispatcherRule
import com.example.library.ui.BookshelfUiState
import com.example.library.ui.BookshelfViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class BookshelfViewModelTest {

    @get:Rule
    val testDispatcher = TestDispatcherRule()

    @Test
    fun bookshelfViewModel_getBookListInformation_verifyBookshelfUiStateSuccess()=
        runTest {

            val bookshelfViewModel=BookshelfViewModel(
                bookshelfRepository = FakeNetworkBookshelfRepository()
            )

            assertEquals(BookshelfUiState.Loading, bookshelfViewModel.bookshelfUiState)

        }
}


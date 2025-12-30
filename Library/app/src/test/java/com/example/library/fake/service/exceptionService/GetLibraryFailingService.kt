package com.example.library.fake.service.exceptionService

import com.example.library.data.entity.Library
import com.example.library.data.entity.LibraryHistory
import com.example.library.data.entity.LibraryLiked
import com.example.library.data.entity.LibraryReservation
import com.example.library.data.entity.UserLoanLibrary
import com.example.library.domain.DatabaseService
import com.example.library.domain.DueCheckResult
import com.example.library.fake.service.FakeGetLibraryFailedException
import com.example.library.service.GetLibraryInfoFailedException
import com.google.firebase.firestore.ListenerRegistration

class GetLibraryFailingService: DatabaseService {
    override suspend fun saveLibraryBooks(
        keyword: String,
        page: String,
        list: List<Library>
    ): Result<Unit> {
        throw FakeGetLibraryFailedException()
    }

    override suspend fun getLibraryBooks(keyword: String, page: String): Result<List<Library>> {
        throw GetLibraryInfoFailedException()
    }

    override suspend fun isSavedBook(keyword: String, page: String): Result<Boolean> {
        return Result.success(true)
    }

    override suspend fun updateLibraryLiked(
        userId: String,
        bookId: String,
        isLiked: Boolean
    ): Result<List<LibraryLiked>> {
        throw FakeGetLibraryFailedException()
    }

    override suspend fun getLibraryLikedList(userId: String): Result<List<LibraryLiked>> {
        throw FakeGetLibraryFailedException()
    }

    override fun getLibraryLikedCount(
        bookId: String,
        onUpdate: (Int) -> Unit
    ): ListenerRegistration {
        throw FakeGetLibraryFailedException()
    }

    override fun getLibraryStatus(
        bookId: String,
        callback: (LibraryHistory) -> Unit
    ): ListenerRegistration {
        throw FakeGetLibraryFailedException()
    }

    override suspend fun updateLibraryHistory(
        userId: String,
        libraryId: String,
        bookId: String,
        bookStatus: String,
        bookTitle: String?,
        bookAuthors: List<String>?,
        keyword: String,
        page: String
    ): Result<Unit> {
        throw FakeGetLibraryFailedException()
    }

    override suspend fun getUserLoanBookList(userId: String): Result<List<UserLoanLibrary>> {
        throw FakeGetLibraryFailedException()
    }

    override suspend fun getUserLoanHistoryList(userId: String): Result<List<UserLoanLibrary>> {
        throw FakeGetLibraryFailedException()
    }

    override suspend fun getLoanDueStatus(
        userId: String,
        keyword: String,
        page: String
    ): Result<DueCheckResult> {
        throw FakeGetLibraryFailedException()
    }

    override suspend fun isOverdueBook(userId: String): Result<Boolean> {
        throw FakeGetLibraryFailedException()
    }

    override suspend fun isReservedBook(bookId: String): Result<Boolean> {
        throw FakeGetLibraryFailedException()
    }

    override suspend fun isUserReservedBook(userId: String): Result<Boolean> {
        throw FakeGetLibraryFailedException()
    }

    override suspend fun getLibraryReservationCount(bookId: String): Result<Int> {
        throw FakeGetLibraryFailedException()
    }

    override suspend fun getReservedStatus(userId: String, bookId: String): Result<String> {
        throw FakeGetLibraryFailedException()
    }

    override suspend fun checkMyReservationTurn(userId: String): Result<List<LibraryReservation>> {
        throw FakeGetLibraryFailedException()
    }

    override suspend fun getReservationList(userId: String): Result<List<List<String>>> {
        throw FakeGetLibraryFailedException()
    }

}
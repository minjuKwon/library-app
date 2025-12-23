package com.example.library.fake.repository.exceptionRepository

import com.example.library.data.entity.Library
import com.example.library.data.entity.LibraryHistory
import com.example.library.data.entity.LibraryLiked
import com.example.library.data.entity.LibraryReservation
import com.example.library.data.entity.UserLoanLibrary
import com.example.library.domain.DatabaseRepository
import com.example.library.domain.HistoryRequest
import com.example.library.service.GetReservationsByBookFailedException
import com.google.firebase.firestore.ListenerRegistration

class ReservationByBookExceptionBookRepository: DatabaseRepository {
    override suspend fun addLibraryBook(
        keyword: String,
        page: String,
        list: List<Library>
    ): Result<Unit> {
        throw FakeReservationByBookDatabaseRepositoryFailedException()
    }

    override suspend fun getLibraryBook(keyword: String, page: String): Result<List<Library>> {
        throw FakeReservationByBookDatabaseRepositoryFailedException()
    }

    override suspend fun hasServerBook(keyword: String, page: String): Result<Boolean> {
        throw FakeReservationByBookDatabaseRepositoryFailedException()
    }

    override suspend fun addLibraryLiked(libraryLiked: LibraryLiked): Result<Unit> {
        throw FakeReservationByBookDatabaseRepositoryFailedException()
    }

    override suspend fun updateLibraryLiked(id: String, data: Map<String, Any>): Result<Unit> {
        throw FakeReservationByBookDatabaseRepositoryFailedException()
    }

    override suspend fun getLibraryLikedList(userId: String): Result<List<LibraryLiked>> {
        throw FakeReservationByBookDatabaseRepositoryFailedException()
    }

    override fun getLibraryLikedCount(
        bookId: String,
        onUpdate: (Int) -> Unit
    ): ListenerRegistration {
        throw FakeReservationByBookDatabaseRepositoryFailedException()
    }

    override suspend fun hasLibraryLiked(id: String): Result<Boolean> {
        throw FakeReservationByBookDatabaseRepositoryFailedException()
    }

    override fun getLibraryStatus(
        bookId: String,
        callback: (LibraryHistory) -> Unit
    ): ListenerRegistration {
        throw FakeReservationByBookDatabaseRepositoryFailedException()
    }

    override suspend fun updateLibraryHistory(historyRequest: HistoryRequest): Result<Unit> {
        throw FakeReservationByBookDatabaseRepositoryFailedException()
    }

    override suspend fun getUserLoanBookList(userId: String): Result<List<UserLoanLibrary>?> {
        throw FakeReservationByBookDatabaseRepositoryFailedException()
    }

    override suspend fun getUserLoanHistoryList(userId: String): Result<List<UserLoanLibrary>> {
        throw FakeReservationByBookDatabaseRepositoryFailedException()
    }

    override suspend fun updateUserOverdueBook(
        keyword: String,
        page: String,
        overdueDate: Long,
        book: UserLoanLibrary
    ): Result<Unit> {
        throw FakeReservationByBookDatabaseRepositoryFailedException()
    }

    override suspend fun hasOverdueBook(userId: String): Result<Boolean> {
        throw FakeReservationByBookDatabaseRepositoryFailedException()
    }

    override suspend fun hasReservedBook(bookId: String): Result<Boolean> {
        throw FakeReservationByBookDatabaseRepositoryFailedException()
    }

    override suspend fun hasUserReservedBook(userId: String): Result<Boolean> {
        throw FakeReservationByBookDatabaseRepositoryFailedException()
    }

    override suspend fun getLibraryReservationCount(bookId: String): Result<Int> {
        throw FakeReservationByBookDatabaseRepositoryFailedException()
    }

    override suspend fun getReservedStatus(userId: String, bookId: String): Result<String> {
        throw FakeReservationByBookDatabaseRepositoryFailedException()
    }

    override suspend fun isMyReservationTurn(userId: String): Result<List<LibraryReservation>> {
        throw FakeReservationByBookDatabaseRepositoryFailedException()
    }

    override suspend fun getReservationsByUser(userId: String): Result<List<LibraryReservation>> {
        return Result.success(listOf(LibraryReservation()))
    }

    override suspend fun getReservationsByBook(bookId: String): Result<List<LibraryReservation>> {
        throw GetReservationsByBookFailedException()
    }
}
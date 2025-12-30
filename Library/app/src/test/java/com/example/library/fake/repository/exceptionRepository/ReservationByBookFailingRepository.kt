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

class ReservationByBookFailingRepository: DatabaseRepository {
    override suspend fun addLibraryBook(
        keyword: String,
        page: String,
        list: List<Library>
    ): Result<Unit> {
        throw FakeReservationByBookFailedException()
    }

    override suspend fun getLibraryBook(keyword: String, page: String): Result<List<Library>> {
        throw FakeReservationByBookFailedException()
    }

    override suspend fun hasServerBook(keyword: String, page: String): Result<Boolean> {
        throw FakeReservationByBookFailedException()
    }

    override suspend fun addLibraryLiked(libraryLiked: LibraryLiked): Result<Unit> {
        throw FakeReservationByBookFailedException()
    }

    override suspend fun updateLibraryLiked(id: String, data: Map<String, Any>): Result<Unit> {
        throw FakeReservationByBookFailedException()
    }

    override suspend fun getLibraryLikedList(userId: String): Result<List<LibraryLiked>> {
        throw FakeReservationByBookFailedException()
    }

    override fun getLibraryLikedCount(
        bookId: String,
        onUpdate: (Int) -> Unit
    ): ListenerRegistration {
        throw FakeReservationByBookFailedException()
    }

    override suspend fun hasLibraryLiked(id: String): Result<Boolean> {
        throw FakeReservationByBookFailedException()
    }

    override fun getLibraryStatus(
        bookId: String,
        callback: (LibraryHistory) -> Unit
    ): ListenerRegistration {
        throw FakeReservationByBookFailedException()
    }

    override suspend fun updateLibraryHistory(historyRequest: HistoryRequest): Result<Unit> {
        throw FakeReservationByBookFailedException()
    }

    override suspend fun getUserLoanBookList(userId: String): Result<List<UserLoanLibrary>?> {
        throw FakeReservationByBookFailedException()
    }

    override suspend fun getUserLoanHistoryList(userId: String): Result<List<UserLoanLibrary>> {
        throw FakeReservationByBookFailedException()
    }

    override suspend fun updateUserOverdueBook(
        keyword: String,
        page: String,
        overdueDate: Long,
        book: UserLoanLibrary
    ): Result<Unit> {
        throw FakeReservationByBookFailedException()
    }

    override suspend fun hasOverdueBook(userId: String): Result<Boolean> {
        throw FakeReservationByBookFailedException()
    }

    override suspend fun hasReservedBook(bookId: String): Result<Boolean> {
        throw FakeReservationByBookFailedException()
    }

    override suspend fun hasUserReservedBook(userId: String): Result<Boolean> {
        throw FakeReservationByBookFailedException()
    }

    override suspend fun getLibraryReservationCount(bookId: String): Result<Int> {
        throw FakeReservationByBookFailedException()
    }

    override suspend fun getReservedStatus(userId: String, bookId: String): Result<String> {
        throw FakeReservationByBookFailedException()
    }

    override suspend fun isMyReservationTurn(userId: String): Result<List<LibraryReservation>> {
        throw FakeReservationByBookFailedException()
    }

    override suspend fun getReservationsByUser(userId: String): Result<List<LibraryReservation>> {
        return Result.success(listOf(LibraryReservation()))
    }

    override suspend fun getReservationsByBook(bookId: String): Result<List<LibraryReservation>> {
        throw GetReservationsByBookFailedException()
    }
}
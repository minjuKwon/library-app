package com.example.library.fake.repository.exceptionRepository

import com.example.library.data.entity.Library
import com.example.library.data.entity.LibraryHistory
import com.example.library.data.entity.LibraryLiked
import com.example.library.data.entity.LibraryReservation
import com.example.library.data.entity.UserLoanLibrary
import com.example.library.domain.DatabaseRepository
import com.example.library.domain.HistoryRequest
import com.example.library.service.GetReservationsByUserFailedException
import com.google.firebase.firestore.ListenerRegistration

class ReservationByUserFailingRepository: DatabaseRepository {
    override suspend fun addLibraryBook(
        keyword: String,
        page: String,
        list: List<Library>
    ): Result<Unit> {
        throw FakeReservationByUserFailedException()
    }

    override suspend fun getLibraryBook(keyword: String, page: String): Result<List<Library>> {
        throw FakeReservationByUserFailedException()
    }

    override suspend fun hasServerBook(keyword: String, page: String): Result<Boolean> {
        throw FakeReservationByUserFailedException()
    }

    override suspend fun addLibraryLiked(libraryLiked: LibraryLiked): Result<Unit> {
        throw FakeReservationByUserFailedException()
    }

    override suspend fun updateLibraryLiked(id: String, data: Map<String, Any>): Result<Unit> {
        throw FakeReservationByUserFailedException()
    }

    override suspend fun getLibraryLikedList(userId: String): Result<List<LibraryLiked>> {
        throw FakeReservationByUserFailedException()
    }

    override fun getLibraryLikedCount(
        bookId: String,
        onUpdate: (Int) -> Unit
    ): ListenerRegistration {
        throw FakeReservationByUserFailedException()
    }

    override suspend fun hasLibraryLiked(id: String): Result<Boolean> {
        throw FakeReservationByUserFailedException()
    }

    override fun getLibraryStatus(
        bookId: String,
        callback: (LibraryHistory) -> Unit
    ): ListenerRegistration {
        throw FakeReservationByUserFailedException()
    }

    override suspend fun updateLibraryHistory(historyRequest: HistoryRequest): Result<Unit> {
        throw FakeReservationByUserFailedException()
    }

    override suspend fun getUserLoanBookList(userId: String): Result<List<UserLoanLibrary>?> {
        throw FakeReservationByUserFailedException()
    }

    override suspend fun getUserLoanHistoryList(userId: String): Result<List<UserLoanLibrary>> {
        throw FakeReservationByUserFailedException()
    }

    override suspend fun updateUserOverdueBook(
        keyword: String,
        page: String,
        overdueDate: Long,
        book: UserLoanLibrary
    ): Result<Unit> {
        throw FakeReservationByUserFailedException()
    }

    override suspend fun hasOverdueBook(userId: String): Result<Boolean> {
        throw FakeReservationByUserFailedException()
    }

    override suspend fun hasReservedBook(bookId: String): Result<Boolean> {
        throw FakeReservationByUserFailedException()
    }

    override suspend fun hasUserReservedBook(userId: String): Result<Boolean> {
        throw FakeReservationByUserFailedException()
    }

    override suspend fun getLibraryReservationCount(bookId: String): Result<Int> {
        throw FakeReservationByUserFailedException()
    }

    override suspend fun getReservedStatus(userId: String, bookId: String): Result<String> {
        throw FakeReservationByUserFailedException()
    }

    override suspend fun isMyReservationTurn(userId: String): Result<List<LibraryReservation>> {
        throw FakeReservationByUserFailedException()
    }

    override suspend fun getReservationsByUser(userId: String): Result<List<LibraryReservation>> {
        throw GetReservationsByUserFailedException()
    }

    override suspend fun getReservationsByBook(bookId: String): Result<List<LibraryReservation>> {
        throw FakeReservationByUserFailedException()
    }
}
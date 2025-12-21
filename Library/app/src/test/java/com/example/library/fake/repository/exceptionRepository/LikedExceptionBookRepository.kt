package com.example.library.fake.repository.exceptionRepository

import com.example.library.data.entity.Library
import com.example.library.data.entity.LibraryHistory
import com.example.library.data.entity.LibraryLiked
import com.example.library.data.entity.LibraryReservation
import com.example.library.data.entity.UserLoanLibrary
import com.example.library.domain.DatabaseRepository
import com.example.library.domain.HistoryRequest
import com.example.library.service.CheckLibraryLikeFailedException
import com.google.firebase.firestore.ListenerRegistration

class LikedExceptionBookRepository:DatabaseRepository {

    override suspend fun addLibraryBook(
        keyword: String,
        page: String,
        list: List<Library>
    ): Result<Unit> {
        throw FakeLikedDatabaseRepositoryFailedException()
    }

    override suspend fun getLibraryBook(keyword: String, page: String): Result<List<Library>> {
        throw FakeLikedDatabaseRepositoryFailedException()
    }

    override suspend fun hasServerBook(keyword: String, page: String): Result<Boolean> {
        throw FakeLikedDatabaseRepositoryFailedException()
    }

    override suspend fun addLibraryLiked(libraryLiked: LibraryLiked): Result<Unit> {
        throw FakeLikedDatabaseRepositoryFailedException()
    }

    override suspend fun updateLibraryLiked(id: String, data: Map<String, Any>): Result<Unit> {
        throw FakeLikedDatabaseRepositoryFailedException()
    }

    override suspend fun getLibraryLikedList(userId: String): Result<List<LibraryLiked>> {
        throw FakeLikedDatabaseRepositoryFailedException()
    }

    override fun getLibraryLikedCount(
        bookId: String,
        onUpdate: (Int) -> Unit
    ): ListenerRegistration {
        throw FakeLikedDatabaseRepositoryFailedException()
    }

    override suspend fun hasLibraryLiked(id: String): Result<Boolean> {
        throw CheckLibraryLikeFailedException()
    }

    override fun getLibraryStatus(
        bookId: String,
        callback: (LibraryHistory) -> Unit
    ): ListenerRegistration {
        TODO("Not yet implemented")
    }

    override suspend fun updateLibraryHistory(historyRequest: HistoryRequest): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserLoanBookList(userId: String): Result<List<UserLoanLibrary>> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserLoanHistoryList(userId: String): Result<List<UserLoanLibrary>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserOverdueBook(
        keyword: String,
        page: String,
        overdueDate: Long,
        book: UserLoanLibrary
    ): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun hasOverdueBook(userId: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun hasReservedBook(bookId: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun hasUserReservedBook(userId: String): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun getLibraryReservationCount(bookId: String): Result<Int> {
        TODO("Not yet implemented")
    }

    override suspend fun getReservedStatus(userId: String, bookId: String): Result<String> {
        TODO("Not yet implemented")
    }

    override suspend fun isMyReservationTurn(userId: String): Result<List<LibraryReservation>> {
        TODO("Not yet implemented")
    }

    override suspend fun getReservationsByUser(userId: String): Result<List<LibraryReservation>> {
        TODO("Not yet implemented")
    }

    override suspend fun getReservationsByBook(bookId: String): Result<List<LibraryReservation>> {
        TODO("Not yet implemented")
    }

}
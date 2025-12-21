package com.example.library.fake.service

import com.example.library.data.entity.Library
import com.example.library.data.entity.LibraryHistory
import com.example.library.data.entity.LibraryLiked
import com.example.library.data.entity.LibraryReservation
import com.example.library.data.entity.UserLoanLibrary
import com.example.library.domain.DatabaseService
import com.example.library.domain.DueCheckResult
import com.example.library.service.CheckLibraryInfoFailedException
import com.example.library.service.GetLibraryInfoFailedException
import com.example.library.service.SaveLibraryInfoFailedException
import com.google.firebase.firestore.ListenerRegistration

class FakeSaveDatabaseService:DatabaseService {
    override suspend fun saveLibraryBooks(
        keyword: String,
        page: String,
        list: List<Library>
    ): Result<Unit> {
        throw SaveLibraryInfoFailedException()
    }

    override suspend fun getLibraryBooks(keyword: String, page: String): Result<List<Library>> {
        throw FakeSaveDatabaseServiceFailedException()
    }

    override suspend fun isSavedBook(keyword: String, page: String): Result<Boolean> {
        return Result.success(false)
    }

    override suspend fun updateLibraryLiked(
        userId: String,
        bookId: String,
        isLiked: Boolean
    ): Result<List<LibraryLiked>> {
        throw FakeSaveDatabaseServiceFailedException()
    }

    override suspend fun getLibraryLikedList(userId: String): Result<List<LibraryLiked>> {
        throw FakeSaveDatabaseServiceFailedException()
    }

    override fun getLibraryLikedCount(
        bookId: String,
        onUpdate: (Int) -> Unit
    ): ListenerRegistration {
        throw FakeSaveDatabaseServiceFailedException()
    }

    override fun getLibraryStatus(
        bookId: String,
        callback: (LibraryHistory) -> Unit
    ): ListenerRegistration {
        throw FakeSaveDatabaseServiceFailedException()
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
        throw FakeSaveDatabaseServiceFailedException()
    }

    override suspend fun getUserLoanBookList(userId: String): Result<List<UserLoanLibrary>> {
        throw FakeSaveDatabaseServiceFailedException()
    }

    override suspend fun getUserLoanHistoryList(userId: String): Result<List<UserLoanLibrary>> {
        throw FakeSaveDatabaseServiceFailedException()
    }

    override suspend fun getLoanDueStatus(
        userId: String,
        keyword: String,
        page: String
    ): Result<DueCheckResult> {
        throw FakeSaveDatabaseServiceFailedException()
    }

    override suspend fun isOverdueBook(userId: String): Result<Boolean> {
        throw FakeSaveDatabaseServiceFailedException()
    }

    override suspend fun isReservedBook(bookId: String): Result<Boolean> {
        throw FakeSaveDatabaseServiceFailedException()
    }

    override suspend fun isUserReservedBook(userId: String): Result<Boolean> {
        throw FakeSaveDatabaseServiceFailedException()
    }

    override suspend fun getLibraryReservationCount(bookId: String): Result<Int> {
        throw FakeSaveDatabaseServiceFailedException()
    }

    override suspend fun getReservedStatus(userId: String, bookId: String): Result<String> {
        throw FakeSaveDatabaseServiceFailedException()
    }

    override suspend fun checkMyReservationTurn(userId: String): Result<List<LibraryReservation>> {
        throw FakeSaveDatabaseServiceFailedException()
    }

    override suspend fun getReservationList(userId: String): Result<List<List<String>>> {
        throw FakeSaveDatabaseServiceFailedException()
    }

}

class FakeGetDatabaseService:DatabaseService {
    override suspend fun saveLibraryBooks(
        keyword: String,
        page: String,
        list: List<Library>
    ): Result<Unit> {
        throw FakeGetDatabaseServiceFailedException()
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
        throw FakeGetDatabaseServiceFailedException()
    }

    override suspend fun getLibraryLikedList(userId: String): Result<List<LibraryLiked>> {
        throw FakeGetDatabaseServiceFailedException()
    }

    override fun getLibraryLikedCount(
        bookId: String,
        onUpdate: (Int) -> Unit
    ): ListenerRegistration {
        throw FakeGetDatabaseServiceFailedException()
    }

    override fun getLibraryStatus(
        bookId: String,
        callback: (LibraryHistory) -> Unit
    ): ListenerRegistration {
        throw FakeGetDatabaseServiceFailedException()
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
        throw FakeGetDatabaseServiceFailedException()
    }

    override suspend fun getUserLoanBookList(userId: String): Result<List<UserLoanLibrary>> {
        throw FakeGetDatabaseServiceFailedException()
    }

    override suspend fun getUserLoanHistoryList(userId: String): Result<List<UserLoanLibrary>> {
        throw FakeGetDatabaseServiceFailedException()
    }

    override suspend fun getLoanDueStatus(
        userId: String,
        keyword: String,
        page: String
    ): Result<DueCheckResult> {
        throw FakeGetDatabaseServiceFailedException()
    }

    override suspend fun isOverdueBook(userId: String): Result<Boolean> {
        throw FakeGetDatabaseServiceFailedException()
    }

    override suspend fun isReservedBook(bookId: String): Result<Boolean> {
        throw FakeGetDatabaseServiceFailedException()
    }

    override suspend fun isUserReservedBook(userId: String): Result<Boolean> {
        throw FakeGetDatabaseServiceFailedException()
    }

    override suspend fun getLibraryReservationCount(bookId: String): Result<Int> {
        throw FakeGetDatabaseServiceFailedException()
    }

    override suspend fun getReservedStatus(userId: String, bookId: String): Result<String> {
        throw FakeGetDatabaseServiceFailedException()
    }

    override suspend fun checkMyReservationTurn(userId: String): Result<List<LibraryReservation>> {
        throw FakeGetDatabaseServiceFailedException()
    }

    override suspend fun getReservationList(userId: String): Result<List<List<String>>> {
        throw FakeGetDatabaseServiceFailedException()
    }

}

class FakeCheckDatabaseService:DatabaseService {
    override suspend fun saveLibraryBooks(
        keyword: String,
        page: String,
        list: List<Library>
    ): Result<Unit> {
        throw FakeCheckDatabaseServiceFailedException()
    }

    override suspend fun getLibraryBooks(keyword: String, page: String): Result<List<Library>> {
        throw FakeCheckDatabaseServiceFailedException()
    }

    override suspend fun isSavedBook(keyword: String, page: String): Result<Boolean> {
        throw CheckLibraryInfoFailedException()
    }

    override suspend fun updateLibraryLiked(
        userId: String,
        bookId: String,
        isLiked: Boolean
    ): Result<List<LibraryLiked>> {
        throw FakeCheckDatabaseServiceFailedException()
    }

    override suspend fun getLibraryLikedList(userId: String): Result<List<LibraryLiked>> {
        throw FakeCheckDatabaseServiceFailedException()
    }

    override fun getLibraryLikedCount(
        bookId: String,
        onUpdate: (Int) -> Unit
    ): ListenerRegistration {
        throw FakeCheckDatabaseServiceFailedException()
    }

    override fun getLibraryStatus(
        bookId: String,
        callback: (LibraryHistory) -> Unit
    ): ListenerRegistration {
        throw FakeCheckDatabaseServiceFailedException()
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
        throw FakeCheckDatabaseServiceFailedException()
    }

    override suspend fun getUserLoanBookList(userId: String): Result<List<UserLoanLibrary>> {
        throw FakeCheckDatabaseServiceFailedException()
    }

    override suspend fun getUserLoanHistoryList(userId: String): Result<List<UserLoanLibrary>> {
        throw FakeCheckDatabaseServiceFailedException()
    }

    override suspend fun getLoanDueStatus(
        userId: String,
        keyword: String,
        page: String
    ): Result<DueCheckResult> {
        throw FakeCheckDatabaseServiceFailedException()
    }

    override suspend fun isOverdueBook(userId: String): Result<Boolean> {
        throw FakeCheckDatabaseServiceFailedException()
    }

    override suspend fun isReservedBook(bookId: String): Result<Boolean> {
        throw FakeCheckDatabaseServiceFailedException()
    }

    override suspend fun isUserReservedBook(userId: String): Result<Boolean> {
        throw FakeCheckDatabaseServiceFailedException()
    }

    override suspend fun getLibraryReservationCount(bookId: String): Result<Int> {
        throw FakeCheckDatabaseServiceFailedException()
    }

    override suspend fun getReservedStatus(userId: String, bookId: String): Result<String> {
        throw FakeCheckDatabaseServiceFailedException()
    }

    override suspend fun checkMyReservationTurn(userId: String): Result<List<LibraryReservation>> {
        throw FakeCheckDatabaseServiceFailedException()
    }

    override suspend fun getReservationList(userId: String): Result<List<List<String>>> {
        throw FakeCheckDatabaseServiceFailedException()
    }

}
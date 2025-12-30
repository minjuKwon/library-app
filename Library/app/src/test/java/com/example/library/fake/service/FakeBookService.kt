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

class SaveLibraryFailingService:DatabaseService {
    override suspend fun saveLibraryBooks(
        keyword: String,
        page: String,
        list: List<Library>
    ): Result<Unit> {
        throw SaveLibraryInfoFailedException()
    }

    override suspend fun getLibraryBooks(keyword: String, page: String): Result<List<Library>> {
        throw FakeSaveLibraryFailedException()
    }

    override suspend fun isSavedBook(keyword: String, page: String): Result<Boolean> {
        return Result.success(false)
    }

    override suspend fun updateLibraryLiked(
        userId: String,
        bookId: String,
        isLiked: Boolean
    ): Result<List<LibraryLiked>> {
        throw FakeSaveLibraryFailedException()
    }

    override suspend fun getLibraryLikedList(userId: String): Result<List<LibraryLiked>> {
        throw FakeSaveLibraryFailedException()
    }

    override fun getLibraryLikedCount(
        bookId: String,
        onUpdate: (Int) -> Unit
    ): ListenerRegistration {
        throw FakeSaveLibraryFailedException()
    }

    override fun getLibraryStatus(
        bookId: String,
        callback: (LibraryHistory) -> Unit
    ): ListenerRegistration {
        throw FakeSaveLibraryFailedException()
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
        throw FakeSaveLibraryFailedException()
    }

    override suspend fun getUserLoanBookList(userId: String): Result<List<UserLoanLibrary>> {
        throw FakeSaveLibraryFailedException()
    }

    override suspend fun getUserLoanHistoryList(userId: String): Result<List<UserLoanLibrary>> {
        throw FakeSaveLibraryFailedException()
    }

    override suspend fun getLoanDueStatus(
        userId: String,
        keyword: String,
        page: String
    ): Result<DueCheckResult> {
        throw FakeSaveLibraryFailedException()
    }

    override suspend fun isOverdueBook(userId: String): Result<Boolean> {
        throw FakeSaveLibraryFailedException()
    }

    override suspend fun isReservedBook(bookId: String): Result<Boolean> {
        throw FakeSaveLibraryFailedException()
    }

    override suspend fun isUserReservedBook(userId: String): Result<Boolean> {
        throw FakeSaveLibraryFailedException()
    }

    override suspend fun getLibraryReservationCount(bookId: String): Result<Int> {
        throw FakeSaveLibraryFailedException()
    }

    override suspend fun getReservedStatus(userId: String, bookId: String): Result<String> {
        throw FakeSaveLibraryFailedException()
    }

    override suspend fun checkMyReservationTurn(userId: String): Result<List<LibraryReservation>> {
        throw FakeSaveLibraryFailedException()
    }

    override suspend fun getReservationList(userId: String): Result<List<List<String>>> {
        throw FakeSaveLibraryFailedException()
    }

}

class GetLibraryFailingService:DatabaseService {
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

class CheckLibraryService:DatabaseService {
    override suspend fun saveLibraryBooks(
        keyword: String,
        page: String,
        list: List<Library>
    ): Result<Unit> {
        throw FakeCheckLibraryFailedException()
    }

    override suspend fun getLibraryBooks(keyword: String, page: String): Result<List<Library>> {
        throw FakeCheckLibraryFailedException()
    }

    override suspend fun isSavedBook(keyword: String, page: String): Result<Boolean> {
        throw CheckLibraryInfoFailedException()
    }

    override suspend fun updateLibraryLiked(
        userId: String,
        bookId: String,
        isLiked: Boolean
    ): Result<List<LibraryLiked>> {
        throw FakeCheckLibraryFailedException()
    }

    override suspend fun getLibraryLikedList(userId: String): Result<List<LibraryLiked>> {
        throw FakeCheckLibraryFailedException()
    }

    override fun getLibraryLikedCount(
        bookId: String,
        onUpdate: (Int) -> Unit
    ): ListenerRegistration {
        throw FakeCheckLibraryFailedException()
    }

    override fun getLibraryStatus(
        bookId: String,
        callback: (LibraryHistory) -> Unit
    ): ListenerRegistration {
        throw FakeCheckLibraryFailedException()
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
        throw FakeCheckLibraryFailedException()
    }

    override suspend fun getUserLoanBookList(userId: String): Result<List<UserLoanLibrary>> {
        throw FakeCheckLibraryFailedException()
    }

    override suspend fun getUserLoanHistoryList(userId: String): Result<List<UserLoanLibrary>> {
        throw FakeCheckLibraryFailedException()
    }

    override suspend fun getLoanDueStatus(
        userId: String,
        keyword: String,
        page: String
    ): Result<DueCheckResult> {
        throw FakeCheckLibraryFailedException()
    }

    override suspend fun isOverdueBook(userId: String): Result<Boolean> {
        throw FakeCheckLibraryFailedException()
    }

    override suspend fun isReservedBook(bookId: String): Result<Boolean> {
        throw FakeCheckLibraryFailedException()
    }

    override suspend fun isUserReservedBook(userId: String): Result<Boolean> {
        throw FakeCheckLibraryFailedException()
    }

    override suspend fun getLibraryReservationCount(bookId: String): Result<Int> {
        throw FakeCheckLibraryFailedException()
    }

    override suspend fun getReservedStatus(userId: String, bookId: String): Result<String> {
        throw FakeCheckLibraryFailedException()
    }

    override suspend fun checkMyReservationTurn(userId: String): Result<List<LibraryReservation>> {
        throw FakeCheckLibraryFailedException()
    }

    override suspend fun getReservationList(userId: String): Result<List<List<String>>> {
        throw FakeCheckLibraryFailedException()
    }

}
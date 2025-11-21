package com.example.library.fake.service

import com.example.library.data.entity.Library
import com.example.library.data.entity.LibraryLiked
import com.example.library.domain.DatabaseService
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
        return Result.success(listOf())
    }

    override suspend fun isSavedBook(keyword: String, page: String): Result<Boolean> {
        return Result.success(false)
    }

    override suspend fun updateLibraryLiked(
        userId: String,
        bookId: String,
        isLiked: Boolean
    ): Result<List<LibraryLiked>> {
        return Result.success(listOf())
    }

    override suspend fun getLibraryLikedList(userId: String): Result<List<LibraryLiked>> {
        return Result.success(listOf())
    }

    override fun getLibraryLikedCount(
        bookId: String,
        onUpdate: (Int) -> Unit
    ): ListenerRegistration {
        return ListenerRegistration { }
    }
}

class FakeGetDatabaseService:DatabaseService {
    override suspend fun saveLibraryBooks(
        keyword: String,
        page: String,
        list: List<Library>
    ): Result<Unit> {
        return Result.success(Unit)
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
        return Result.success(listOf())
    }

    override suspend fun getLibraryLikedList(userId: String): Result<List<LibraryLiked>> {
        return Result.success(listOf())
    }

    override fun getLibraryLikedCount(
        bookId: String,
        onUpdate: (Int) -> Unit
    ): ListenerRegistration {
        return ListenerRegistration { }
    }
}

class FakeCheckDatabaseService:DatabaseService {
    override suspend fun saveLibraryBooks(
        keyword: String,
        page: String,
        list: List<Library>
    ): Result<Unit> {
        return Result.success(Unit)
    }

    override suspend fun getLibraryBooks(keyword: String, page: String): Result<List<Library>> {
        return Result.success(listOf())
    }

    override suspend fun isSavedBook(keyword: String, page: String): Result<Boolean> {
        throw CheckLibraryInfoFailedException()
    }

    override suspend fun updateLibraryLiked(
        userId: String,
        bookId: String,
        isLiked: Boolean
    ): Result<List<LibraryLiked>> {
        return Result.success(listOf())
    }

    override suspend fun getLibraryLikedList(userId: String): Result<List<LibraryLiked>> {
        return Result.success(emptyList())
    }

    override fun getLibraryLikedCount(
        bookId: String,
        onUpdate: (Int) -> Unit
    ): ListenerRegistration {
        return ListenerRegistration { }
    }
}
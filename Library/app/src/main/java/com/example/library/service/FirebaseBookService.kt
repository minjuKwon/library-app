package com.example.library.service

import com.example.library.data.entity.Library
import com.example.library.domain.DatabaseRepository
import com.example.library.domain.DatabaseService
import com.example.library.domain.SessionManager
import javax.inject.Inject

class FirebaseBookService@Inject constructor(
    private val databaseRepository: DatabaseRepository
):DatabaseService {

    override suspend fun saveLibraryBooks(keyword: String, page: String, list: List<Library>): Result<Unit> {
        return databaseRepository.addLibraryBook(keyword,page,list)
    }

    override suspend fun getLibraryBooks(keyword: String, page: String): Result<List<Library>> {
        return databaseRepository.getLibraryBook(keyword,page)
    }

    override suspend fun isSavedBook(keyword: String, page: String): Result<Boolean> {
        return databaseRepository.hasServerBook(keyword,page)
    }

}
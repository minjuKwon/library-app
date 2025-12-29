package com.example.library.fake.service

import com.example.library.data.entity.Library
import com.example.library.domain.LibrarySyncService

class FakeLibrarySyncService:LibrarySyncService {
    override suspend fun getSearchBooks(keyword: String, pageNumber: Int): List<Library> =
        emptyList()

    override suspend fun getTotalCntForKeyword(keyword: String): Int = 0
}
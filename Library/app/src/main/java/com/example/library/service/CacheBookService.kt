package com.example.library.service

import com.example.library.core.TimeProvider
import com.example.library.data.entity.Library
import com.example.library.domain.LocalRepository
import javax.inject.Inject

class CacheBookService @Inject constructor(
    private val cacheBookRepository: LocalRepository,
    private val timeProvider: TimeProvider
){

    suspend fun getLibraryBooks(query: String, page: Int): List<Library>
        = cacheBookRepository.searchResultData(query, page)

    suspend fun saveLibraryBooks(
        library: Library,
        query:String,
        page:Int
    ) {
        val currentTime= timeProvider.now()
        cacheBookRepository.cacheLibraryBooks(library, query, page, currentTime, currentTime)
    }

    suspend fun getTotalCountForKeyword(query: String): Int?
        = cacheBookRepository.searchTotalCount(query)

    suspend fun saveTotalCount(query: String, count:Int)
        = cacheBookRepository.cacheTotalCount(query, count)

    suspend fun isKeywordCached(keyword: String, page: Int):Boolean
        = cacheBookRepository.hasCachedKeyword(keyword, page)

}
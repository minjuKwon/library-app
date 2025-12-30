package com.example.library.service

import com.example.library.data.entity.Library
import com.example.library.domain.LocalRepository
import javax.inject.Inject

class CacheBookService @Inject constructor(
    private val cacheBookRepository: LocalRepository
){

    suspend fun getLibraryBooks(
        query: String,
        page: Int,
        now:Long
    ): List<Library>{
        val list= cacheBookRepository.searchResultData(query, page)

        list.forEach { cacheBookRepository.updateAccessTime(it.libraryId, page, now) }

        return list
    }

    suspend fun saveLibraryBooks(
        library: Library,
        query:String,
        page:Int
    ) {
        val currentTime= System.currentTimeMillis()
        cacheBookRepository.cacheLibraryBooks(library, query, page, currentTime, currentTime)
    }

    suspend fun getTotalCountForKeyword(query: String): Int?
        = cacheBookRepository.searchTotalCount(query)

    suspend fun saveTotalCount(query: String, count:Int)
        = cacheBookRepository.cacheTotalCount(query, count)

    suspend fun isKeywordCached(keyword: String, page: Int):Boolean
        = cacheBookRepository.hasCachedKeyword(keyword, page)

}
package com.example.library.service

import com.example.library.data.entity.Library
import com.example.library.domain.LocalRepository
import javax.inject.Inject

class CacheBookService @Inject constructor(
    private val cacheBookRepository: LocalRepository
){

    suspend fun getLibraryBooks(query: String, offset: Int, page: Int): List<Library>
        = cacheBookRepository.searchResultData(query, offset, page)

    suspend fun saveLibraryBooks(
        library: Library,
        query:String,
        page:Int,
        cachedAt:Long,
        accessedAt:Long
    ) = cacheBookRepository.cacheLibraryBooks(library, query, page, cachedAt, accessedAt)

    suspend fun isKeywordCached(keyword: String, page: Int):Boolean
        = cacheBookRepository.hasCachedKeyword(keyword, page)

}
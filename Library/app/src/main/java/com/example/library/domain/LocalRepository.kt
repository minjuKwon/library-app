package com.example.library.domain

import com.example.library.data.entity.Library

interface LocalRepository {
    suspend fun searchResultData(query: String, page: Int): List<Library>
    suspend fun cacheLibraryBooks(library: Library, query:String, page:Int, cachedAt:Long, accessedAt:Long)
    suspend fun searchTotalCount(query: String): Int?
    suspend fun cacheTotalCount(query:String, count:Int)
    suspend fun hasCachedKeyword(keyword: String, page: Int):Boolean
    suspend fun updateAccessTime(libraryId:String, page:Int, now:Long)
}
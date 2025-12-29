package com.example.library.data.repository

import com.example.library.data.QueryNormalizer.normalizeQuery
import com.example.library.data.entity.Library
import com.example.library.data.mapper.toBookEntity
import com.example.library.data.mapper.toBookImageEntity
import com.example.library.data.mapper.toLibraryEntity
import com.example.library.data.room.BookCacheDao
import com.example.library.data.mapper.toListLibrary
import com.example.library.data.mapper.toSearchResultEntity
import com.example.library.data.room.SearchTotalCountEntity
import com.example.library.domain.LocalRepository
import javax.inject.Inject

class CacheBookRepository @Inject constructor(
    private val bookCacheDao: BookCacheDao
) : LocalRepository {

    override suspend fun searchResultData(query: String, page: Int): List<Library> {
        try{
            val normalizeQuery= normalizeQuery(query)
            return bookCacheDao.getBooks(normalizeQuery, page).toListLibrary()
        }catch (e:Exception){
            throw Exception(e.message)
        }
    }

    override suspend fun cacheLibraryBooks(
        library: Library,
        query:String,
        page:Int,
        cachedAt:Long,
        accessedAt:Long
    ){
        try{
            val normalizeQuery= normalizeQuery(query)

            bookCacheDao.insertSearchResultWithLibrary(
                library.toSearchResultEntity(normalizeQuery, page, cachedAt, accessedAt),
                library.toLibraryEntity(),
                library.book.bookInfo.toBookEntity(library.book.id),
                library.book.bookInfo.img.toBookImageEntity(library.book.id)
            )
        }catch (e:Exception){
            throw Exception(e.message)
        }
    }

    override suspend fun searchTotalCount(query: String): Int? {
        try{
            val normalizeQuery= normalizeQuery(query)
            return bookCacheDao.getSearchTotalCount(normalizeQuery)
        }catch (e:Exception){
            throw Exception(e.message)
        }
    }

    override suspend fun cacheTotalCount(query: String, count: Int) {
        try{
            val normalizeQuery= normalizeQuery(query)
            val entity= SearchTotalCountEntity(normalizeQuery, count)

            bookCacheDao.insertSearchTotalCount(entity)
        }catch (e:Exception){
            throw Exception(e.message)
        }
    }

    override suspend fun hasCachedKeyword(keyword: String, page: Int):Boolean{
        try{
            val normalizeQuery= normalizeQuery(keyword)
            return bookCacheDao.hasBooksForKeyword(normalizeQuery, page)
        }catch (e:Exception){
            throw Exception(e.message)
        }
    }

    override suspend fun updateAccessTime(libraryId: String, page: Int, now: Long) {
        try{
            bookCacheDao.updateLastAccess(libraryId, page, now)
        }catch (e:Exception){
            throw Exception(e.message)
        }
    }

}
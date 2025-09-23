package com.example.library.data.repository

import com.example.library.data.entity.Library
import com.example.library.data.mapper.toBookEntity
import com.example.library.data.mapper.toBookImageEntity
import com.example.library.data.mapper.toLibraryEntity
import com.example.library.data.room.BookCacheDao
import com.example.library.data.mapper.toListLibrary
import com.example.library.data.mapper.toSearchResultEntity
import com.example.library.domain.LocalRepository
import javax.inject.Inject

class CacheBookRepository @Inject constructor(
    private val bookCacheDao: BookCacheDao
) : LocalRepository {

    override suspend fun searchResultData(query: String, offset: Int, page: Int): List<Library> =
        bookCacheDao.getBooks(query, page).toListLibrary(offset)

    override suspend fun cacheLibraryBooks(
        library: Library,
        query:String,
        page:Int,
        cachedAt:Long,
        accessedAt:Long
    ){
        bookCacheDao.insertSearchResultWithLibrary(
            library.toSearchResultEntity(query, page, cachedAt, accessedAt),
            library.toLibraryEntity(),
            library.book.bookInfo.toBookEntity(library.book.id),
            library.book.bookInfo.img.toBookImageEntity(library.book.id)
        )
    }

    override suspend fun hasCachedKeyword(keyword: String, page: Int):Boolean{
        return bookCacheDao.hasBooksForKeyword(keyword, page)
    }

}
package com.example.library.fake

import com.example.library.data.QueryNormalizer.normalizeQuery
import com.example.library.data.entity.Library
import com.example.library.domain.LocalRepository

class FakeCacheBookRepository: LocalRepository {

    private data class LocalItem(
        val page:Int,
        val cachedAt: Long,
        val accessedAt: Long,
        val query:String,
        val library: Library
    )

    private data class ItemCount(
        val query: String,
        val count: Int
    )

    private val itemList= mutableListOf<LocalItem>()
    private val countList= mutableListOf<ItemCount>()

    override suspend fun searchResultData(query: String, page: Int): List<Library> {
        val normalizedQuery= normalizeQuery(query)
        val list= itemList
            .filter { it.query==normalizedQuery && it.page==page }
            .sortedBy { it.library.offset }
            .map { it.library }

        return list
    }

    override suspend fun cacheLibraryBooks(
        library: Library,
        query: String,
        page: Int,
        cachedAt: Long,
        accessedAt: Long
    ) {
        val normalizedQuery= normalizeQuery(query)

        itemList.add(LocalItem(page, cachedAt, accessedAt, normalizedQuery, library))
    }

    override suspend fun searchTotalCount(query: String): Int {
        val normalizedQuery= normalizeQuery(query)
        val list= countList.filter { it.query== normalizedQuery }
        return if(list.isNotEmpty()) list[0].count
        else -1
    }

    override suspend fun cacheTotalCount(query: String, count: Int) {
        val normalizedQuery= normalizeQuery(query)
        countList.add(ItemCount(normalizedQuery,count))
    }

    override suspend fun hasCachedKeyword(keyword: String, page: Int): Boolean {
        val normalizedQuery= normalizeQuery(keyword)
        val isSave= itemList.any { it.query==normalizedQuery && it.page==page }

        return isSave
    }
}
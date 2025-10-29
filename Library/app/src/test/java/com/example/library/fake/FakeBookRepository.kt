package com.example.library.fake

import com.example.library.data.QueryNormalizer.normalizeQuery
import com.example.library.data.entity.Library
import com.example.library.domain.DatabaseRepository

class FakeBookRepository:DatabaseRepository {

    private data class DatabaseItem(
        val query:String,
        val page:String,
        val library: Library
    )

    private val itemList= mutableListOf<DatabaseItem>()

    override suspend fun addLibraryBook(
        keyword: String,
        page: String,
        list: List<Library>
    ): Result<Unit> {
        return try {
            val normalizedQuery= normalizeQuery(keyword)
            list.forEach { item ->
                val data= DatabaseItem(normalizedQuery, page, item)
                itemList.add(data)
            }

            Result.success(Unit)
        }catch(e:Exception){
            Result.failure(e)
        }
    }

    override suspend fun getLibraryBook(keyword: String, page: String): Result<List<Library>> {
        return try {
            val normalizedQuery= normalizeQuery(keyword)
            val list= itemList
                .filter { it.query==normalizedQuery && it.page==page }
                .sortedBy { it.library.offset }
                .map { it.library }

            Result.success(list)
        }catch(e:Exception){
            Result.failure(e)
        }
    }

    override suspend fun hasServerBook(keyword: String, page: String): Result<Boolean> {
        return try {
            val normalizedQuery= normalizeQuery(keyword)
            val isSave= itemList.any { it.query==normalizedQuery && it.page==page }

            Result.success(isSave)
        }catch(e:Exception){
            Result.failure(e)
        }
    }

}
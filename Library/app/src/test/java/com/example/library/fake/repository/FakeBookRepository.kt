package com.example.library.fake.repository

import com.example.library.data.QueryNormalizer.normalizeQuery
import com.example.library.data.entity.Library
import com.example.library.data.entity.LibraryLiked
import com.example.library.domain.DatabaseRepository
import com.google.firebase.firestore.ListenerRegistration

class FakeBookRepository:DatabaseRepository {

    private data class DatabaseItem(
        val userId: String="",
        val query:String,
        val page:String,
        val library: Library
    )

    private val itemList= mutableListOf<DatabaseItem>()
    private val likeList= mutableListOf<LibraryLiked>()

    override suspend fun addLibraryBook(
        keyword: String,
        page: String,
        list: List<Library>
    ): Result<Unit> {
        return try {
            val normalizedQuery= normalizeQuery(keyword)
            list.forEach { item ->
                val data= DatabaseItem("",normalizedQuery, page, item)
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

    override suspend fun addLibraryLiked(libraryLiked: LibraryLiked): Result<Unit> {
        return try {
            likeList.add(libraryLiked)

            Result.success(Unit)
        }catch(e:Exception){
            Result.failure(e)
        }
    }

    override suspend fun updateLibraryLiked(id: String, data: Map<String, Any>): Result<Unit> {
        return try {
            val index= likeList.indexOfFirst { it.likedId==id }
            likeList[index]= likeList[index].copy(
                isLiked = (data["isLiked"] as Boolean),
                timestamp = (data["timestamp"] as Number).toLong()
            )
            Result.success(Unit)
        }catch(e:Exception){
            Result.failure(e)
        }
    }

    override suspend fun getLibraryLikedList(userId: String): Result<List<LibraryLiked>> {
        return try {
            Result.success(likeList)
        }catch(e:Exception){
            Result.failure(e)
        }
    }

    override fun getLibraryLikedCount(
        bookId: String,
        onUpdate: (Int) -> Unit
    ): ListenerRegistration {
        val count= likeList.count { it.bookId==bookId }
        onUpdate(count)

        return ListenerRegistration { }
    }

    override suspend fun hasLibraryLiked(id: String): Result<Boolean> {
        return try {
            val hasLiked= likeList.any { it.likedId == id }

            Result.success(hasLiked)
        }catch(e:Exception){
            Result.failure(e)
        }
    }

}
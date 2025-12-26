package com.example.library.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction

@Dao
interface BookCacheDao {

    @Insert
    suspend fun insertSearchResult(result:SearchResultEntity)

    @Insert
    suspend fun insertLibrary(library: LibraryEntity)

    @Insert
    suspend fun insertBook(book:BookEntity)

    @Insert
    suspend fun insertBookImage(image:BookImageEntity)

    @Insert
    suspend fun insertSearchTotalCount(searchTotalCountEntity:SearchTotalCountEntity)

    @Transaction
    suspend fun insertSearchResultWithLibrary(
        result:SearchResultEntity,
        library: LibraryEntity,
        book:BookEntity,
        image:BookImageEntity
    ){
        insertLibrary(library)
        insertBook(book)
        insertBookImage(image)
        insertSearchResult(result)
    }

    @Transaction
    @Query("SELECT * FROM search_result WHERE `query` = :keyword AND page = :page ORDER BY `offset` ASC")
    suspend fun getBooks(keyword:String, page:Int): List<SearchResultWithLibrary>

    @Query("UPDATE search_result SET accessedAt=:now WHERE libraryId = :libraryId AND page = :page")
    suspend fun updateLastAccess(libraryId:String, page:Int, now:Long)

    @Query("SELECT * FROM search_result WHERE cachedAt < :cachedAt AND accessedAt < :accessedAt")
    suspend fun getExpiredSearchResult(cachedAt: Long, accessedAt: Long):List<SearchResultWithLibrary>

    @Query("DELETE FROM search_result WHERE bookId = :bookId")
    suspend fun deleteSearchResult(bookId: String)

    @Query("DELETE FROM library WHERE bookId = :bookId")
    suspend fun deleteLibrary(bookId:String)

    @Query("DELETE FROM book WHERE id = :bookId")
    suspend fun deleteBook(bookId:String)

    @Query("DELETE FROM book_image WHERE id = :bookId")
    suspend fun deleteBookImage(bookId:String)

    @Query("DELETE FROM search_total_count WHERE `query` = :query")
    suspend fun deleteSearchTotalCount(query:String)

    @Transaction
    suspend fun deleteExpiredBooks(cachedAt: Long, accessedAt: Long){
        val list= getExpiredSearchResult(cachedAt, accessedAt)
        list.distinctBy { it.searchResultEntity.query }
            .forEach {
                deleteSearchTotalCount(it.searchResultEntity.query)
            }

        list.forEach {
            val bookId=it.searchResultEntity.bookId
            deleteLibrary(bookId)
            deleteBook(bookId)
            deleteBookImage(bookId)
            deleteSearchResult(bookId)
        }
    }

    @Query("SELECT count FROM SEARCH_TOTAL_COUNT WHERE `query` = :keyword")
    suspend fun getSearchTotalCount(keyword: String): Int?

    @Query("SELECT EXISTS(SELECT 1 FROM search_result WHERE `query` = :keyword AND page=:page)")
    suspend fun hasBooksForKeyword(keyword:String, page:Int):Boolean

}
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
        insertSearchResult(result)
        insertLibrary(library)
        insertBook(book)
        insertBookImage(image)
    }

    @Transaction
    @Query("SELECT * FROM search_result WHERE `query` = :keyword AND page = :page ORDER BY `offset` ASC")
    suspend fun getBooks(keyword:String, page:Int):List<SearchResultWithLibrary>

    @Query("SELECT count FROM SEARCH_TOTAL_COUNT WHERE `query` = :keyword")
    suspend fun getSearchTotalCount(keyword: String): Int?

    @Query("SELECT EXISTS(SELECT 1 FROM search_result WHERE `query` = :keyword AND page=:page)")
    suspend fun hasBooksForKeyword(keyword:String, page:Int):Boolean

}
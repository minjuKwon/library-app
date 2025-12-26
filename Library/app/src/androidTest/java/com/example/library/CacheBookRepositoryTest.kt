package com.example.library

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.library.data.entity.Book
import com.example.library.data.entity.BookInfo
import com.example.library.data.entity.BookStatus
import com.example.library.data.entity.Library
import com.example.library.data.repository.CacheBookRepository
import com.example.library.data.room.BookCacheDao
import com.example.library.data.room.LibraryDatabase
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CacheBookRepositoryTest {

    private lateinit var db: LibraryDatabase
    private lateinit var dao: BookCacheDao
    private lateinit var cacheBookRepository: CacheBookRepository

    private val list= listOf(
        Library(
            "1",
            Book("1", BookInfo("title1")),
            BookStatus.Available,
            "",
            "",
            0
        ),
        Library(
            "2",
            Book("2", BookInfo("title2")),
            BookStatus.Available,
            "",
            "",
            1
        ),
        Library(
            "3",
            Book("3", BookInfo("title3")),
            BookStatus.Available,
            "",
            "",
            2
        )
    )

    @Before
    fun setUp(){
        db= Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            LibraryDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        dao= db.bookCacheDao()
        cacheBookRepository= CacheBookRepository(dao)
    }

    @After
    fun closeResource(){
        db.close()
    }

    @Test
    fun cacheBookRepository_insertAndGetItem_verifyWorkCorrectly() = runTest{
        list.forEach {
            cacheBookRepository.cacheLibraryBooks(it,"title",1,0,0)
        }

        val result= cacheBookRepository.searchResultData("title",1)

        assertEquals(list.size, result.size)
        list.forEachIndexed { index, _ ->
            assertEquals(list[index].book.bookInfo.title, result[index].book.bookInfo.title)
        }
    }

    @Test
    fun cacheBookRepository_insertAndDeleteItem_verifyWorkCorrectly() = runTest{
        list.forEach {
            cacheBookRepository.cacheLibraryBooks(it,"title",1,0,0)
        }

        dao.deleteExpiredBooks(10,10)

        val result= cacheBookRepository.searchResultData("title",1)

        assertEquals(0, result.size)
    }

    @Test
    fun cacheBookRepository_insertAndUpdateItem_verifyWorkCorrectly() = runTest{
        list.forEach {
            cacheBookRepository.cacheLibraryBooks(it,"title",1,0,0)
        }

        list.forEach {
            cacheBookRepository.updateAccessTime(it.libraryId,1, 10)
        }

        val result= dao.getBooks("title",1)

        result.forEach {
            assertEquals(10, it.searchResultEntity.accessedAt)
        }
    }

    @Test
    fun cacheBookRepository_insertAndGetCount_verifyWorkCorrectly()= runTest{
        cacheBookRepository.cacheTotalCount("title", 3)

        val result= cacheBookRepository.searchTotalCount("title")
        assertEquals(3, result)
    }

    @Test
    fun cacheBookRepository_isSaved_verifyReturnTrue()= runTest{
        list.forEach {
            cacheBookRepository.cacheLibraryBooks(it,"title",1,0,0)
        }

        val result= cacheBookRepository.hasCachedKeyword("title",1)
        assertTrue(result)
    }

    @Test
    fun cacheBookRepository_isSaved_verifyReturnFalse()= runTest{
        val result= cacheBookRepository.hasCachedKeyword("title",1)
        assertFalse(result)
    }

}
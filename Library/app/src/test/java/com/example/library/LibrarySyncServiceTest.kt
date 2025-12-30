package com.example.library

import com.example.library.data.entity.Book
import com.example.library.data.entity.BookInfo
import com.example.library.data.entity.BookStatus
import com.example.library.data.entity.Library
import com.example.library.data.mapper.toItem
import com.example.library.fake.FakeDataSource
import com.example.library.fake.FakeTimeProvider
import com.example.library.fake.repository.FakeBookRepository
import com.example.library.fake.repository.FakeCacheBookRepository
import com.example.library.fake.repository.FakeExceptionNetworkBookRepository
import com.example.library.fake.repository.FakeNetworkBookRepository
import com.example.library.fake.service.exceptionService.CheckLibraryFailingService
import com.example.library.fake.service.exceptionService.GetLibraryFailingService
import com.example.library.fake.service.exceptionService.SaveLibraryFailingService
import com.example.library.service.CacheBookService
import com.example.library.service.CheckLibraryInfoFailedException
import com.example.library.service.DefaultLibrarySyncService
import com.example.library.service.FirebaseBookService
import com.example.library.service.GetLibraryInfoFailedException
import com.example.library.service.SaveLibraryInfoFailedException
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.io.IOException
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LibrarySyncServiceTest {

    @Test
    fun librarySyncService_getSearchBooks_verifyCachedDataSuccess()= runTest{
        val library= Library(
            "",
            Book("", BookInfo("cache1")),BookStatus.Available,"","",0
        )

        val fakeCacheBookRepository= FakeCacheBookRepository()
        fakeCacheBookRepository.cacheLibraryBooks(library,"cache",1,0,0)

        val fakeRepository = FakeNetworkBookRepository()
        val fakeLibrarySyncService= DefaultLibrarySyncService(
            fakeRepository,
            CacheBookService(fakeCacheBookRepository, FakeTimeProvider()),
            FirebaseBookService(FakeBookRepository(), FakeTimeProvider())
        )

        val result= fakeLibrarySyncService.getSearchBooks("cache",1)

        assertEquals(result?.get(0) , library)
    }

    @Test
    fun librarySyncService_getSearchBooks_verifyLocalDataSuccess()= runTest{
        val library= Library(
            "",
            Book("", BookInfo("database1")),BookStatus.Available,"","",0
        )

        val fakeBookRepository= FakeBookRepository()
        fakeBookRepository.addLibraryBook("database","1",listOf(library))

        val fakeRepository = FakeNetworkBookRepository()
        val fakeLibrarySyncService= DefaultLibrarySyncService(
            fakeRepository,
            CacheBookService(FakeCacheBookRepository(), FakeTimeProvider()),
            FirebaseBookService(fakeBookRepository, FakeTimeProvider())
        )

        val result= fakeLibrarySyncService.getSearchBooks("database",1)

        assertEquals(result?.get(0), library)
    }

    @Test
    fun librarySyncService_getSearchBooks_verifyRemoteDataSuccess()= runTest{
        val fakeRepository = FakeNetworkBookRepository()
        val fakeLibrarySyncService= DefaultLibrarySyncService(
            fakeRepository,
            CacheBookService(FakeCacheBookRepository(), FakeTimeProvider()),
            FirebaseBookService(FakeBookRepository(), FakeTimeProvider())
        )

        val result= fakeLibrarySyncService.getSearchBooks("android",1)

        assertEquals(result?.get(0)?.book, FakeDataSource.item.toItem().book[0])
    }

    @Test
    fun librarySyncService_getSearchBooks_verifyToCheckDatabaseDataFailure()= runTest{
        val fakeLibrarySyncService= DefaultLibrarySyncService(
            FakeNetworkBookRepository(),
            CacheBookService(FakeCacheBookRepository(), FakeTimeProvider()),
            CheckLibraryFailingService()
        )

        assertFailsWith<CheckLibraryInfoFailedException>{
            fakeLibrarySyncService.getSearchBooks("",1)
        }
    }

    @Test
    fun librarySyncService_getSearchBooks_verifyToGetDatabaseDataFailure()= runTest{
        val fakeLibrarySyncService= DefaultLibrarySyncService(
            FakeNetworkBookRepository(),
            CacheBookService(FakeCacheBookRepository(), FakeTimeProvider()),
            GetLibraryFailingService()
        )

        assertFailsWith<GetLibraryInfoFailedException>{
            fakeLibrarySyncService.getSearchBooks("",1)
        }
    }

    @Test
    fun librarySyncService_getSearchBooks_verifyToSaveDatabaseDataFailure()= runTest{
        val fakeLibrarySyncService= DefaultLibrarySyncService(
            FakeNetworkBookRepository(),
            CacheBookService(FakeCacheBookRepository(), FakeTimeProvider()),
            SaveLibraryFailingService()
        )

        assertFailsWith<SaveLibraryInfoFailedException>{
            fakeLibrarySyncService.getSearchBooks("",1)
        }
    }

    @Test
    fun librarySyncService_getSearchBooks_verifyToGetRemoteDataFailure()= runTest{
        val fakeLibrarySyncService= DefaultLibrarySyncService(
            FakeExceptionNetworkBookRepository(),
            CacheBookService(FakeCacheBookRepository(), FakeTimeProvider()),
            FirebaseBookService(FakeBookRepository(), FakeTimeProvider())
        )

        assertFailsWith<IOException>{
            fakeLibrarySyncService.getSearchBooks("",1)
        }
    }

    @Test
    fun librarySyncService_getTotalCntForKeyword_verifyCachedDataSuccess()= runTest{
        val fakeCacheBookRepository= FakeCacheBookRepository()
        fakeCacheBookRepository.cacheTotalCount("cache",2)

        val fakeRepository = FakeNetworkBookRepository()
        val fakeLibrarySyncService= DefaultLibrarySyncService(
            fakeRepository,
            CacheBookService(fakeCacheBookRepository, FakeTimeProvider()),
            FirebaseBookService(FakeBookRepository(), FakeTimeProvider())
        )

        val result= fakeLibrarySyncService.getTotalCntForKeyword("cache")

        assertEquals(result, 2)
    }

    @Test
    fun librarySyncService_getTotalCntForKeyword_verifyRemoteDataSuccess()= runTest{
        val fakeRepository = FakeNetworkBookRepository()
        val fakeLibrarySyncService= DefaultLibrarySyncService(
            fakeRepository,
            CacheBookService(FakeCacheBookRepository(), FakeTimeProvider()),
            FirebaseBookService(FakeBookRepository(), FakeTimeProvider())
        )

        val result= fakeLibrarySyncService.getTotalCntForKeyword("android")

        assertEquals(result, 0)
    }

}
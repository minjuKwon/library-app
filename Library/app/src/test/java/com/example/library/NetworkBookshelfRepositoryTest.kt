package com.example.library

import com.example.library.data.NetworkBookshelfRepository
import com.example.library.fake.FakeBookshelfApiService
import com.example.library.fake.FakeDataSource
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkBookshelfRepositoryTest {

    @Test
    fun networkBookshelfRepository_getBookListInformation_verityItem()=
        runTest {
            val repository= NetworkBookshelfRepository(
                bookshelfApiService = FakeBookshelfApiService()
            )
            assertEquals(FakeDataSource.item,
                repository.searchVolume("android",0,0)
            )
        }
}
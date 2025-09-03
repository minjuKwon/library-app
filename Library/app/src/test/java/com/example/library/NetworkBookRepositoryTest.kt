package com.example.library

import com.example.library.data.repository.NetworkBookRepository
import com.example.library.data.toItem
import com.example.library.fake.FakeVolumesApiService
import com.example.library.fake.FakeDataSource
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class NetworkBookRepositoryTest {

    @Test
    fun networkBookRepository_getBookListInformation_verityItem()=
        runTest {
            val repository= NetworkBookRepository(
                volumesApiService = FakeVolumesApiService()
            )
            assertEquals(
                FakeDataSource.item.toItem(),
                repository.searchVolume("android",0,0)
            )
        }
}
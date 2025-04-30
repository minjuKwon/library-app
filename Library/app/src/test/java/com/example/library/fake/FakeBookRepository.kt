package com.example.library.fake

import com.example.library.data.BookRepository
import com.example.library.network.Item
import okio.IOException

class FakeNetworkBookRepository :BookRepository {
    override suspend fun searchVolume(query: String, limit: Int, offset: Int): Item {
        return FakeDataSource.item
    }
}

class FakeBookmarkedBookRepository:BookRepository{
    override suspend fun searchVolume(query: String, limit: Int, offset: Int): Item {
        return FakeDataSource.itemBookmarked
    }
}

class FakeExceptionBookRepository:BookRepository{
    override suspend fun searchVolume(query: String, limit: Int, offset: Int): Item {
        throw IOException("fake exception repository")
    }
}
package com.example.library.fake

import com.example.library.data.entity.Item
import com.example.library.domain.BookRepository
import com.example.library.data.mapper.toItem
import okio.IOException

class FakeNetworkBookRepository : BookRepository {
    override suspend fun searchVolume(query: String, limit: Int, offset: Int): Item {
        return FakeDataSource.item.toItem()
    }
}

class FakeBookmarkedBookRepository: BookRepository {
    override suspend fun searchVolume(query: String, limit: Int, offset: Int): Item {
        return FakeDataSource.itemBookmarked.toItem()
    }
}

class FakeExceptionBookRepository: BookRepository {
    override suspend fun searchVolume(query: String, limit: Int, offset: Int): Item {
        throw IOException("fake exception repository")
    }
}
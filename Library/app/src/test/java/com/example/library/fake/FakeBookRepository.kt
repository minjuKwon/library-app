package com.example.library.fake

import com.example.library.data.entity.Item
import com.example.library.domain.RemoteRepository
import com.example.library.data.mapper.toItem
import okio.IOException

class FakeNetworkBookRepository : RemoteRepository {
    override suspend fun searchVolume(query: String, limit: Int, offset: Int): Item {
        return FakeDataSource.item.toItem()
    }
}

class FakeBookmarkedBookRepository: RemoteRepository {
    override suspend fun searchVolume(query: String, limit: Int, offset: Int): Item {
        return FakeDataSource.itemBookmarked.toItem()
    }
}

class FakeExceptionBookRepository: RemoteRepository {
    override suspend fun searchVolume(query: String, limit: Int, offset: Int): Item {
        throw IOException("fake exception repository")
    }
}
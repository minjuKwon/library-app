package com.example.library.fake

import com.example.library.network.VolumesApiService
import com.example.library.network.Item

class FakeVolumesApiService :VolumesApiService {
    override suspend fun searchVolume(query: String, limit: Int, offset: Int): Item {
        return FakeDataSource.item
    }
}
package com.example.library.fake

import com.example.library.data.api.VolumesApiService
import com.example.library.data.api.Item

class FakeVolumesApiService : VolumesApiService {
    override suspend fun searchVolume(query: String, limit: Int, offset: Int): Item {
        return FakeDataSource.item
    }
}
package com.example.library.fake.service

import com.example.library.data.api.ItemDto
import com.example.library.data.api.VolumesApiService
import com.example.library.fake.FakeDataSource

class FakeVolumesApiService : VolumesApiService {
    override suspend fun searchVolume(query: String, limit: Int, offset: Int): ItemDto {
        return FakeDataSource.item
    }
}
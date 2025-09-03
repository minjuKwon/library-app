package com.example.library.fake

import com.example.library.data.api.ItemDto
import com.example.library.data.api.VolumesApiService

class FakeVolumesApiService : VolumesApiService {
    override suspend fun searchVolume(query: String, limit: Int, offset: Int): ItemDto {
        return FakeDataSource.item
    }
}
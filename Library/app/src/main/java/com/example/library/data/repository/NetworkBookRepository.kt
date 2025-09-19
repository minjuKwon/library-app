package com.example.library.data.repository

import com.example.library.data.entity.Item
import com.example.library.domain.BookRepository
import com.example.library.data.api.VolumesApiService
import com.example.library.data.mapper.toItem
import javax.inject.Inject

class NetworkBookRepository @Inject constructor(
    private val volumesApiService: VolumesApiService
): BookRepository<Item> {
    override suspend fun searchVolume(query:String, limit:Int, offset:Int, page:Int): Item {
        return volumesApiService.searchVolume(query, limit, offset).toItem()
    }
}
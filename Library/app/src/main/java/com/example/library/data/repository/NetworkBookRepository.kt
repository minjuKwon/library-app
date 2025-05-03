package com.example.library.data.repository

import com.example.library.domain.BookRepository
import com.example.library.data.api.Item
import com.example.library.data.api.VolumesApiService
import javax.inject.Inject

class NetworkBookRepository @Inject constructor(
    private val volumesApiService: VolumesApiService
): BookRepository {
    override suspend fun searchVolume(query:String, limit:Int, offset:Int): Item
            = volumesApiService.searchVolume(query, limit, offset)
}
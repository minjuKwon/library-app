package com.example.library.data

import com.example.library.network.Item
import com.example.library.network.VolumesApiService

class NetworkBookRepository(
    private val volumesApiService: VolumesApiService
):BookRepository{
    override suspend fun searchVolume(query:String, limit:Int, offset:Int): Item
            = volumesApiService.searchVolume(query, limit, offset)
}
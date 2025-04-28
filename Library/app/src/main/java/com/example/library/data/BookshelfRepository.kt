package com.example.library.data

import com.example.library.network.BookshelfApiService
import com.example.library.network.Item

interface BookshelfRepository {
    suspend fun searchVolume(query:String, limit:Int, offset:Int):Item
}

class NetworkBookshelfRepository(
    private val bookshelfApiService: BookshelfApiService
):BookshelfRepository{
    override suspend fun searchVolume(query:String, limit:Int, offset:Int): Item
    = bookshelfApiService.searchVolume(query, limit, offset)
}
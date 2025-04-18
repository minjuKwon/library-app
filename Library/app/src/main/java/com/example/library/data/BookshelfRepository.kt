package com.example.library.data

import com.example.library.network.BookshelfApiService
import com.example.library.network.Item

interface BookshelfRepository {
    suspend fun getBookListInformation(query:String, count:Int, startIndex:Int):Item
}

class NetworkBookshelfRepository(
    private val bookshelfApiService: BookshelfApiService
):BookshelfRepository{
    override suspend fun getBookListInformation(query:String, count:Int, startIndex:Int): Item
    = bookshelfApiService.getInformation(query, count, startIndex)
}
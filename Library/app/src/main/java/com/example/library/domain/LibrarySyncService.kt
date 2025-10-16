package com.example.library.domain

import com.example.library.data.entity.Library

interface LibrarySyncService {
    suspend fun getSearchBooks(getSearchBooksCommands: GetSearchBooksCommands):List<Library>?
    suspend fun getTotalCntForKeyword(keyword:String):Int?
}

data class GetSearchBooksCommands(
    val keyword:String,
    val pageNumber:Int,
    val cachedAt:Long,
    val accessedAt:Long
)
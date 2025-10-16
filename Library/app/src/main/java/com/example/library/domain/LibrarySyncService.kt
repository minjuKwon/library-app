package com.example.library.domain

import com.example.library.data.entity.Library

interface LibrarySyncService {
    suspend fun getSearchBooks(keyword:String, pageNumber:Int):List<Library>?
    suspend fun getTotalCntForKeyword(keyword:String):Int?
}
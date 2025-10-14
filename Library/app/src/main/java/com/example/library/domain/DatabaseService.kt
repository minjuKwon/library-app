package com.example.library.domain

import com.example.library.data.entity.Library

interface DatabaseService {
    suspend fun saveLibraryBooks(keyword:String, page: String, list:List<Library>):Result<Unit>
    suspend fun getLibraryBooks(keyword:String, page: String):Result<List<Library>>
    suspend fun isSavedBook(keyword:String, page:String):Result<Boolean>
}
package com.example.library.domain

import com.example.library.data.entity.Library

interface DatabaseService {
    suspend fun saveLibraryBooks(keyword:String, page: String, list:List<Library>)
    suspend fun getLibraryBooks(keyword:String, page: String):List<Library>
    suspend fun isSavedBook(keyword:String, page:String):Boolean
}
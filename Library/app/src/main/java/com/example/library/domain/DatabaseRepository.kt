package com.example.library.domain

import com.example.library.data.entity.Library

interface DatabaseRepository {
    suspend fun addLibraryBook(keyword:String, page: String, list:List<Library>)
    suspend fun getLibraryBook(keyword:String, page: String):List<Library>
    suspend fun hasServerBook(keyword:String, page:String):Boolean
}
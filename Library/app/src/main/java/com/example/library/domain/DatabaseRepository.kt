package com.example.library.domain

import com.example.library.data.entity.Library

interface DatabaseRepository {
    suspend fun addLibraryBook(keyword:String, page: String, list:List<Library>):Result<Unit>
    suspend fun getLibraryBook(keyword:String, page: String):Result<List<Library>>
    suspend fun hasServerBook(keyword:String, page:String):Result<Boolean>
}
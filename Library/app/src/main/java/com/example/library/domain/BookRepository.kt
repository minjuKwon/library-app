package com.example.library.domain

interface BookRepository<T> {
    suspend fun searchVolume(query:String, limit:Int, offset:Int): T
}
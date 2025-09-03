package com.example.library.domain

import com.example.library.data.Item

interface BookRepository {
    suspend fun searchVolume(query:String, limit:Int, offset:Int): Item
}
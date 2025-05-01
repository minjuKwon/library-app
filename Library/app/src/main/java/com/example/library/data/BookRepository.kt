package com.example.library.data

import com.example.library.network.Item

interface BookRepository {
    suspend fun searchVolume(query:String, limit:Int, offset:Int):Item
}
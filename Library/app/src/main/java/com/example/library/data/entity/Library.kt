package com.example.library.data.entity

import java.time.Instant

data class Library(
    val book: Book,
    val bookStatus: BookStatus,
    val callNumber:String,
    val location:String,
    val offset:Int
)

sealed class BookStatus {
    object Available: BookStatus()
    data class Borrowed(val userEmail:String, val borrowedAt: Instant): BookStatus()
    data class Reserved(val userEmail:String, val reservedAt: Instant): BookStatus()
}
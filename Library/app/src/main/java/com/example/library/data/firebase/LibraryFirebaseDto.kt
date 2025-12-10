package com.example.library.data.firebase

import com.example.library.data.entity.Book
import com.example.library.data.entity.BookInfo

data class LibraryFirebaseDto(
    val libraryId:String="",
    val book: Book= Book("", BookInfo()),
    val statusType: String="",
    val userId: String? ="",
    val borrowedAt: Long? =0L,
    val dueDate: Long? =0L,
    val overdueDate:Long?= 0L,
    val reservedAt: Long? =0L,
    val callNumber:String="",
    val location:String="",
    val offset:Int=0
)

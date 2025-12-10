package com.example.library.data.entity

import java.time.Instant

data class Library(
    val libraryId:String,
    val book: Book,
    val bookStatus: BookStatus,
    val callNumber:String,
    val location:String,
    val offset:Int
)

sealed class BookStatus {
    object Available: BookStatus()
    object UnAvailable: BookStatus()
    data class Borrowed(val userId:String, val borrowedAt: Instant, val dueDate:Instant): BookStatus()
    data class Reserved(val userId:String, val reservedAt: Instant): BookStatus()
    data class OverDue(val userId:String, val overdueDate: Instant): BookStatus()
}

enum class BookStatusType(val ko:String){
    AVAILABLE("대출 가능"),
    UNAVAILABLE("대출 불가"),
    BORROWED("대출 중"),
    RESERVED("예약 중"),
    RETURNED("반납 완료"),
    OVERDUE("연체")
}
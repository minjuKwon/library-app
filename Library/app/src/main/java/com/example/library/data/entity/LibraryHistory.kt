package com.example.library.data.entity

data class LibraryHistory(
    val loanHistoryId:String="",
    val userId:String="",
    val bookId:String="",
    val status: String="",
    val loanDate: Long=0,
    val dueDate:Long=0,
    val returnDate: Long=0
)

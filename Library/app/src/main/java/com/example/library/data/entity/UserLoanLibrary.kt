package com.example.library.data.entity

data class UserLoanLibrary(
    val userLibraryInfoId:String="",
    val userId:String="",
    val bookId:String="",
    val title:String?=null,
    val authors:List<String>?=null,
    val status: String="",
    val loanDate: Long=0,
    val dueDate:Long=0,
    val returnDate: Long=0
)

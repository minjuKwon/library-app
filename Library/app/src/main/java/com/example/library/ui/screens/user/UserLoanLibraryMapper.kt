package com.example.library.ui.screens.user

import android.util.Log
import com.example.library.data.entity.BookStatusType
import com.example.library.data.entity.UserLoanLibrary
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun List<UserLoanLibrary>.toStringList(): List<List<String>>{

    val contentList:MutableList<List<String>> = mutableListOf()

    this.forEachIndexed { index, userLoanLibrary ->
        Log.d("dd","lazy column index: ${userLoanLibrary.bookId} ")
        val author= userLoanLibrary.authors?.joinToString(",")?:""
        val status= when (userLoanLibrary.status) {
            BookStatusType.AVAILABLE.name -> BookStatusType.AVAILABLE.ko
            BookStatusType.UNAVAILABLE.name -> BookStatusType.UNAVAILABLE.ko
            BookStatusType.BORROWED.name -> BookStatusType.BORROWED.ko
            BookStatusType.RESERVED.name -> BookStatusType.RESERVED.ko
            BookStatusType.RETURNED.name -> BookStatusType.RETURNED.ko
            else -> ""
        }
        val list=listOf(
            (index+1).toString(),
            userLoanLibrary.title?:"",
            author,
            formatDateOnly(userLoanLibrary.loanDate),
            formatDateOnly(userLoanLibrary.dueDate),
            status
        )

        contentList.add(list)
    }
    return contentList.toList()
}

private fun formatDateOnly(millis: Long): String {
    val seoulZone = ZoneId.of("Asia/Seoul")
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    return Instant.ofEpochMilli(millis)
        .atZone(seoulZone)
        .toLocalDate()
        .format(formatter)
}
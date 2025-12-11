package com.example.library.ui.screens.user

import com.example.library.data.entity.BookStatusType
import com.example.library.data.entity.UserLoanLibrary
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun List<UserLoanLibrary>.toLoanStringList(): List<List<String>>{
    val resultList:MutableList<List<String>> = mutableListOf()

    this.forEachIndexed { index, userLoanLibrary ->
        if(userLoanLibrary.status==BookStatusType.BORROWED.name){
            val author= userLoanLibrary.authors?.joinToString(",")?:""
            val status= BookStatusType.BORROWED.ko
            val list=listOf(
                (index+1).toString(),
                userLoanLibrary.title?:"",
                author,
                formatDateOnly(userLoanLibrary.loanDate),
                formatDateOnly(userLoanLibrary.dueDate),
                status
            )

            resultList.add(list)
        }
    }

    return resultList.toList()
}

fun List<UserLoanLibrary>.toOverdueStringList(): List<List<String>>{
    val resultList:MutableList<List<String>> = mutableListOf()

    this.forEachIndexed { index, userLoanLibrary ->
        if(userLoanLibrary.status==BookStatusType.OVERDUE.name){
            val author= userLoanLibrary.authors?.joinToString(",")?:""
            val status= BookStatusType.OVERDUE.ko
            val list=listOf(
                (index+1).toString(),
                userLoanLibrary.title?:"",
                author,
                formatDateOnly(userLoanLibrary.loanDate),
                formatDateOnly(userLoanLibrary.dueDate),
                status
            )

            resultList.add(list)
        }
    }

    return resultList.toList()
}

fun List<UserLoanLibrary>.toUserHistoryStringList(): List<List<String>>{

    val resultList:MutableList<List<String>> = mutableListOf()

    this.forEachIndexed { index, userLoanLibrary ->
        val author= userLoanLibrary.authors?.joinToString(",")?:""
        val list=listOf(
            (this.size-index).toString(),
            userLoanLibrary.title?:"",
            author,
            formatDateOnly(userLoanLibrary.loanDate),
            formatDateOnly(userLoanLibrary.returnDate),
        )

        resultList.add(list)
    }
    return resultList.toList()
}

private fun formatDateOnly(millis: Long): String {
    val seoulZone = ZoneId.of("Asia/Seoul")
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    return Instant.ofEpochMilli(millis)
        .atZone(seoulZone)
        .toLocalDate()
        .format(formatter)
}
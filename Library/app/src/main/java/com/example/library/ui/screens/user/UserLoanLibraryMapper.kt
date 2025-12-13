package com.example.library.ui.screens.user

import com.example.library.core.DateTimeConverter.formatDateOnly
import com.example.library.core.DateTimeConverter.getLocalDate
import com.example.library.data.entity.BookStatusType
import com.example.library.data.entity.UserLoanLibrary
import java.time.temporal.ChronoUnit

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

fun List<UserLoanLibrary>.getSuspensionEndDate(): String{
    var minDate:Long= Long.MAX_VALUE
    this.forEach {userLoanLibrary ->
        if(userLoanLibrary.returnDate>userLoanLibrary.dueDate){
            minDate= minOf(minDate, userLoanLibrary.dueDate)
        }
    }

    return formatSuspensionEndDate(minDate)
}

private fun formatSuspensionEndDate(dueDate: Long): String {
    val dueDateTime = getLocalDate(dueDate)
    val nowTime = getLocalDate(System.currentTimeMillis())

    val diff = ChronoUnit.DAYS.between(dueDateTime, nowTime)
    val suspensionDate= nowTime.plusDays(diff)

    return formatDateOnly(suspensionDate)
}

fun List<UserLoanLibrary>.toUserHistoryStringList(): List<List<String>>{
    val resultList:MutableList<List<String>> = mutableListOf()

    this.forEachIndexed { index, userLoanLibrary ->
        if(userLoanLibrary.status==BookStatusType.RETURNED.name){
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
    }
    return resultList.toList()
}
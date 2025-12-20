package com.example.library.ui.screens.user

import com.example.library.core.DateTimeConverter.formatDateOnly
import com.example.library.core.DateTimeConverter.getLocalDate
import com.example.library.core.DateTimeConverter.toLong
import com.example.library.data.entity.BookStatusType
import com.example.library.data.entity.LibraryReservation
import com.example.library.data.entity.UserLoanLibrary
import com.example.library.ui.common.ReservationStatusUiMapper.toStringName
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

    return if(minDate==Long.MAX_VALUE) "-"
    else formatSuspensionEndDate(minDate)
}

private fun formatSuspensionEndDate(dueDate: Long): String {
    val dueDateTime = getLocalDate(dueDate)
    val nowTime = getLocalDate(System.currentTimeMillis())

    val diff = ChronoUnit.DAYS.between(dueDateTime, nowTime)
    val suspensionDate= nowTime.plusDays(diff)

    return formatDateOnly(suspensionDate)
}

fun List<UserLoanLibrary>.getSuspensionEndDateToLong(): Long{
    var minDate:Long= Long.MAX_VALUE
    this.forEach {userLoanLibrary ->
        if(userLoanLibrary.returnDate>userLoanLibrary.dueDate){
            minDate= minOf(minDate, userLoanLibrary.dueDate)
        }
    }

    return getSuspensionEndDate(minDate)
}

private fun getSuspensionEndDate(dueDate: Long): Long {
    val dueDateTime = getLocalDate(dueDate)
    val nowTime = getLocalDate(System.currentTimeMillis())

    val diff = ChronoUnit.DAYS.between(dueDateTime, nowTime)
    val suspensionDate= nowTime.plusDays(diff)

    return suspensionDate.toLong()
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

fun MutableList<List<String>>.toUserReservationList(
    reservation: LibraryReservation,
    order:Int
):MutableList<List<String>>{
    val list=listOf(
        (this.size+1).toString(),
        reservation.bookTitle,
        formatDateOnly(reservation.reservedAt),
        order.toString(),
        reservation.status.toStringName(),
    )
    this.add(list)
    return this
}
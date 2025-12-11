package com.example.library.core

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object DateTimeConverter {

    private const val ZONE_ID="Asia/Seoul"
    private val seoulZone: ZoneId = ZoneId.of(ZONE_ID)
    private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun calculateDueDate(currentMillis: Long): Long {
        val loanDateTime = getLocalDate(currentMillis)

        val dueDateTime = loanDateTime
            .plusDays(0)
            .atTime(23, 59, 59)
            .atZone(seoulZone)

        return dueDateTime.toInstant().toEpochMilli()
    }

    fun calculateOverDueDateLong(currentMillis: Long): Long {
        return calculateOverDueDate(currentMillis).toEpochMilli()
    }

    fun calculateOverDueDate(currentMillis:Long):Instant{
        val loanDateTime = getLocalDate(currentMillis)

        val dueDateTime = loanDateTime
            .plusDays(1)
            .atStartOfDay(seoulZone)

        return dueDateTime.toInstant()
    }

    fun formatDateOnly(millis: Long): String {
        return Instant.ofEpochMilli(millis)
            .atZone(seoulZone)
            .toLocalDate()
            .format(formatter)
    }

    fun formatDateOnly(date: Instant): String  {
        return date
            .atZone(seoulZone)
            .toLocalDate()
            .format(formatter)
    }

    fun formatDateOnly(date: LocalDate): String {
        return date.format(formatter)
    }

    fun getLocalDate(currentMillis: Long): LocalDate {
        val loanDateTime = Instant.ofEpochMilli(currentMillis).atZone(seoulZone)

        return loanDateTime.toLocalDate()
    }

}
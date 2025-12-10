package com.example.library.core

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class DefaultTimeProvider:TimeProvider {
    override fun now(): Long = System.currentTimeMillis()

    override fun calculateDueDate(currentMillis: Long, zoneId:String): Long {
        val seoulZone = ZoneId.of(zoneId)
        val loanDateTime = getLocalDate(currentMillis, zoneId)

        val dueDateTime = loanDateTime
            .plusDays(0)
            .atTime(23, 59, 59)
            .atZone(seoulZone)

        return dueDateTime.toInstant().toEpochMilli()
    }

    override fun calculateOverDueDate(currentMillis: Long, zoneId: String): Long {
        val seoulZone = ZoneId.of(zoneId)
        val loanDateTime = getLocalDate(currentMillis, zoneId)

        val dueDateTime = loanDateTime
            .plusDays(1)
            .atStartOfDay(seoulZone)

        return dueDateTime.toInstant().toEpochMilli()
    }

    override fun getLocalDate(currentMillis: Long, zoneId:String):LocalDate{
        val seoulZone = ZoneId.of(zoneId)
        val loanDateTime = Instant.ofEpochMilli(currentMillis).atZone(seoulZone)

        return loanDateTime.toLocalDate()
    }

}
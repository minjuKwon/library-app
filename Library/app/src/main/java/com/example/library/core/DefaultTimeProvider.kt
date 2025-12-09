package com.example.library.core

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

class DefaultTimeProvider:TimeProvider {
    override fun now(): Long = System.currentTimeMillis()

    override fun getLocalDate(currentMillis: Long, zoneId:String):LocalDate{
        val seoulZone = ZoneId.of(zoneId)
        val loanDateTime = Instant.ofEpochMilli(currentMillis).atZone(seoulZone)

        return loanDateTime.toLocalDate()
    }

}
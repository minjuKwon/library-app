package com.example.library.fake

import java.time.Instant
import java.time.ZoneId

class DateConverter{

    private val zoneId="Asia/Seoul"
    private val seoulZone: ZoneId = ZoneId.of(zoneId)

    val now= System.currentTimeMillis()

    fun before():Long{
        val loanDateTime = Instant.ofEpochMilli(now).atZone(seoulZone)
        return loanDateTime.plusDays(-1).toInstant().toEpochMilli()
    }

    fun overdue():Long{
        val loanDateTime = Instant.ofEpochMilli(now).atZone(seoulZone)
        return loanDateTime.plusDays(1).toInstant().toEpochMilli()
    }
}
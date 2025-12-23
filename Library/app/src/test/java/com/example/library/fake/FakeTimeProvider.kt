package com.example.library.fake

import com.example.library.core.TimeProvider
import java.time.Instant
import java.time.ZoneId

class FakeTimeProvider:TimeProvider {

    private val zoneId="Asia/Seoul"
    private val seoulZone: ZoneId = ZoneId.of(zoneId)

    override fun now(): Long = 1_700_000_000_000L//2023년

    fun before():Long{
        val loanDateTime = Instant.ofEpochMilli(now()).atZone(seoulZone)
        return loanDateTime.plusDays(-1).toInstant().toEpochMilli()
    }

    fun overdue():Long{
        val loanDateTime = Instant.ofEpochMilli(now()).atZone(seoulZone)
        return loanDateTime.plusDays(1).toInstant().toEpochMilli()
    }
}
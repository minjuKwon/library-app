package com.example.library.core

import java.time.LocalDate

interface TimeProvider {
    fun now():Long
    fun getLocalDate(currentMillis: Long, zoneId:String="Asia/Seoul"): LocalDate
}
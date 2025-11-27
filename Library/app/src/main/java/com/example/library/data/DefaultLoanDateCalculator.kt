package com.example.library.data

import com.example.library.domain.LoanDateCalculator
import java.time.Instant
import java.time.ZoneId

class DefaultLoanDateCalculator:LoanDateCalculator {
    override fun calculateDueDate(currentMillis: Long): Long {
        val seoulZone = ZoneId.of("Asia/Seoul")
        val loanDateTime = Instant.ofEpochMilli(currentMillis).atZone(seoulZone)

        val dueDateTime = loanDateTime.toLocalDate()
            .plusDays(15)
            .atTime(23, 59, 59)
            .atZone(seoulZone)

        return dueDateTime.toInstant().toEpochMilli()
    }
}
package com.example.library.domain

interface LoanDateCalculator {
    fun calculateDueDate(currentMillis: Long): Long
}
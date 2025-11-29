package com.example.library.ui.common

import com.example.library.data.entity.BookStatus
import com.example.library.data.entity.BookStatusType

object BookStatusUiMapper {
    fun BookStatus.toStringName() = when(this){
        is BookStatus.Available -> BookStatusType.AVAILABLE.ko
        is BookStatus.UnAvailable -> BookStatusType.UNAVAILABLE.ko
        is BookStatus.Borrowed -> BookStatusType.BORROWED.ko
        is BookStatus.Reserved -> BookStatusType.RESERVED.ko
    }
}
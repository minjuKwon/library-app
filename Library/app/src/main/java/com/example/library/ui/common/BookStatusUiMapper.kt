package com.example.library.ui.common

import com.example.library.data.entity.BookStatus

object BookStatusUiMapper {
    fun BookStatus.toStringName() = when(this){
        is BookStatus.Available -> "대출 가능"
        is BookStatus.Borrowed -> "대출 불가능"
        is BookStatus.Reserved -> "예약 중"
    }
}
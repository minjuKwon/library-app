package com.example.library.ui.common

import com.example.library.data.entity.ReservationStatusType

object ReservationStatusUiMapper {
    fun String.toStringName():String= when(this){
        "WAITING" -> ReservationStatusType.WAITING.ko
        "NOTIFIED" -> ReservationStatusType.NOTIFIED.ko
        "CANCELLED" -> ReservationStatusType.CANCELLED.ko
        else -> "-"
    }
}
package com.example.library.data.entity

data class LibraryReservation(
    val libraryReservationId:String="",
    val userId:String="",
    val bookId:String="",
    val bookTitle:String="",
    val reservedAt:Long=0,
    val status:String=""
)

enum class ReservationStatusType(val ko:String){
    WAITING("예약 대기 중"),
    NOTIFIED("예약 안내 완료"),
    CANCELLED("예약 취소"),
}
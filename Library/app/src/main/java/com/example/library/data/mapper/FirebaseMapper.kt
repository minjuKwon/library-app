package com.example.library.data.mapper

import com.example.library.data.entity.BookStatus
import com.example.library.data.entity.Library
import com.example.library.data.firebase.LibraryFirebaseDto
import java.time.Instant

fun Library.toFirebaseDto():LibraryFirebaseDto= LibraryFirebaseDto(
    libraryId= this.libraryId,
    book = this.book,
    statusType= this.bookStatus.toStringType(),
    userEmail= this.bookStatus.toStringEmail(),
    borrowedAt= this.bookStatus.toLongBorrowedAt(),
    reservedAt= this.bookStatus.toLongReservedAt(),
    callNumber= this.callNumber,
    location = this.location,
    offset = this.offset
)

fun LibraryFirebaseDto.toLibrary(): Library= Library(
    libraryId= this.libraryId,
    book = this.book,
    bookStatus = toBookStatus(this),
    callNumber= this.callNumber,
    location = this.location,
    offset = this.offset
)

fun toBookStatus(libraryFirebaseDto: LibraryFirebaseDto) = when(libraryFirebaseDto.statusType){
    "Available" -> BookStatus.Available
    "Borrowed" -> BookStatus.Borrowed(
        libraryFirebaseDto.userEmail!!,
        Instant.ofEpochMilli(libraryFirebaseDto.borrowedAt!!),
        Instant.ofEpochMilli(libraryFirebaseDto.dueDate!!)
    )
    "Reserved" -> BookStatus.Reserved(
        libraryFirebaseDto.userEmail!!,
        Instant.ofEpochMilli(libraryFirebaseDto.reservedAt!!)
    )
    else -> throw IllegalArgumentException("Unknown bookStatus: ${libraryFirebaseDto.statusType}")
}
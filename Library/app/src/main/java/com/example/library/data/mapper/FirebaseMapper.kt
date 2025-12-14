package com.example.library.data.mapper

import com.example.library.data.entity.BookStatus
import com.example.library.data.entity.BookStatusType
import com.example.library.data.entity.Library
import com.example.library.data.firebase.LibraryFirebaseDto
import java.time.Instant

fun Library.toFirebaseDto():LibraryFirebaseDto= LibraryFirebaseDto(
    libraryId= this.libraryId,
    book = this.book,
    statusType= this.bookStatus.toStringType(),
    userId= this.bookStatus.toStringUserId(),
    borrowedAt= this.bookStatus.toLongBorrowedAt(),
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
    BookStatusType.AVAILABLE.name -> BookStatus.Available
    BookStatusType.UNAVAILABLE.name -> BookStatus.UnAvailable
    BookStatusType.BORROWED.name -> BookStatus.Borrowed(
        libraryFirebaseDto.userId!!,
        Instant.ofEpochMilli(libraryFirebaseDto.borrowedAt!!),
        Instant.ofEpochMilli(libraryFirebaseDto.dueDate!!)
    )
    BookStatusType.RESERVED.name -> BookStatus.Reserved
    BookStatusType.OVERDUE.name -> BookStatus.OverDue(
        libraryFirebaseDto.userId!!,
        Instant.ofEpochMilli(libraryFirebaseDto.overdueDate!!)
    )
    else -> throw IllegalArgumentException("Unknown bookStatus: ${libraryFirebaseDto.statusType}")
}
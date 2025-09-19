package com.example.library.data.mapper

import com.example.library.data.entity.Book
import com.example.library.data.entity.BookImage
import com.example.library.data.entity.BookInfo
import com.example.library.data.entity.BookStatus
import com.example.library.data.entity.Library
import com.example.library.data.room.BookImageEntity
import com.example.library.data.room.BookWitImage
import com.example.library.data.room.LibraryEntity
import com.example.library.data.room.LibraryWithBook
import com.example.library.data.room.SearchResultWithLibrary
import java.time.Instant

fun List<SearchResultWithLibrary>.toListLibrary(offset: Int) = map{it.toLibrary(offset)}

fun SearchResultWithLibrary.toLibrary(offset:Int) = Library(
    libraryId = this.libraryBook.libraryEntity.libraryId,
    book = this.libraryBook.toBook(),
    bookStatus = toBookStatus(this.libraryBook.libraryEntity),
    callNumber = this.libraryBook.libraryEntity.callNumber,
    location = this.libraryBook.libraryEntity.location,
    offset = offset
)

fun LibraryWithBook.toBook() = Book(
    id = this.book.bookEntity.id,
    bookInfo = this.book.toBookInfo()
)

fun BookWitImage.toBookInfo() = BookInfo(
    title = this.bookEntity.title,
    authors = this.bookEntity.authors,
    publisher = this.bookEntity.publisher,
    publishedDate = this.bookEntity.publishedDate,
    description = this.bookEntity.description,
    img = this.image.toBookImage()
)

fun BookImageEntity.toBookImage() = BookImage(
    thumbnail = this.thumbnail,
    small = this.small,
    medium = this.medium,
    large = this.large,
    smallThumbnail = this.smallThumbnail
)

fun toBookStatus(libraryEntity: LibraryEntity) = when(libraryEntity.statusType){
    "Available" -> BookStatus.Available
    "Borrowed" -> BookStatus.Borrowed(
        libraryEntity.userEmail!!,
        Instant.ofEpochMilli(libraryEntity.borrowedAt!!)
    )
    "Reserved" -> BookStatus.Reserved(
        libraryEntity.userEmail!!,
        Instant.ofEpochMilli(libraryEntity.reservedAt!!)
    )
    else -> throw IllegalArgumentException("Unknown bookStatus: ${libraryEntity.statusType}")
}
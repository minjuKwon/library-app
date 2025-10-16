package com.example.library.data.mapper

import com.example.library.data.entity.Book
import com.example.library.data.entity.BookImage
import com.example.library.data.entity.BookInfo
import com.example.library.data.entity.BookStatus
import com.example.library.data.entity.Library
import com.example.library.data.room.BookEntity
import com.example.library.data.room.BookImageEntity
import com.example.library.data.room.BookWitImage
import com.example.library.data.room.LibraryEntity
import com.example.library.data.room.LibraryWithBook
import com.example.library.data.room.SearchResultEntity
import com.example.library.data.room.SearchResultWithLibrary
import java.time.Instant

fun List<SearchResultWithLibrary>.toListLibrary() = map{it.toLibrary()}

fun SearchResultWithLibrary.toLibrary() = Library(
    libraryId = this.libraryBook.libraryEntity.libraryId,
    book = this.libraryBook.toBook(),
    bookStatus = toBookStatus(this.libraryBook.libraryEntity),
    callNumber = this.libraryBook.libraryEntity.callNumber,
    location = this.libraryBook.libraryEntity.location,
    offset = this.searchResultEntity.offset
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

fun Library.toSearchResultEntity(
    query:String,
    page:Int,
    cachedAt:Long,
    accessedAt:Long
) = SearchResultEntity(
    libraryId = this.libraryId,
    query = query,
    page = page,
    offset = this.offset,
    cachedAt= cachedAt,
    accessedAt= accessedAt
)

fun Library.toLibraryEntity() = LibraryEntity(
    libraryId = this.libraryId,
    statusType= this.bookStatus.toStringType(),
    userEmail= this.bookStatus.toStringEmail(),
    borrowedAt= this.bookStatus.toLongBorrowedAt(),
    reservedAt= this.bookStatus.toLongReservedAt(),
    callNumber= this.callNumber,
    location = this.location
)

fun BookStatus.toStringType()  = when(this) {
    is BookStatus.Available -> "Available"
    is BookStatus.Borrowed -> "Borrowed"
    is BookStatus.Reserved -> "Reserved"
}

fun BookStatus.toStringEmail()  = when(this) {
    is BookStatus.Available -> null
    is BookStatus.Borrowed -> this.userEmail
    is BookStatus.Reserved -> this.userEmail
}

fun BookStatus.toLongBorrowedAt()  = when(this) {
    is BookStatus.Available -> null
    is BookStatus.Borrowed -> this.borrowedAt.toEpochMilli()
    is BookStatus.Reserved -> null
}

fun BookStatus.toLongReservedAt()  = when(this) {
    is BookStatus.Available -> null
    is BookStatus.Borrowed -> null
    is BookStatus.Reserved -> this.reservedAt.toEpochMilli()
}

fun BookInfo.toBookEntity(id:String) = BookEntity(
    id=id,
    title= this.title,
    authors= this.authors,
    publisher= this.publisher,
    publishedDate= this.publishedDate,
    description= this.description
)

fun BookImage?.toBookImageEntity(id:String) = BookImageEntity(
    id=id,
    thumbnail= this?.thumbnail,
    small= this?.small,
    medium= this?.medium,
    large= this?.large,
    smallThumbnail= this?.smallThumbnail
)
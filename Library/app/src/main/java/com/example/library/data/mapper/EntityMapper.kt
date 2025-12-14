package com.example.library.data.mapper

import com.example.library.data.entity.Book
import com.example.library.data.entity.BookImage
import com.example.library.data.entity.BookInfo
import com.example.library.data.entity.BookStatus
import com.example.library.data.entity.BookStatusType
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
    BookStatusType.AVAILABLE.name -> BookStatus.Available
    BookStatusType.UNAVAILABLE.name -> BookStatus.UnAvailable
    BookStatusType.BORROWED.name -> BookStatus.Borrowed(
        libraryEntity.userId!!,
        Instant.ofEpochMilli(libraryEntity.borrowedAt!!),
        Instant.ofEpochMilli(libraryEntity.dueDate!!)
    )
    BookStatusType.RESERVED.name -> BookStatus.Reserved
    BookStatusType.OVERDUE.name -> BookStatus.OverDue(
        libraryEntity.userId!!,
        Instant.ofEpochMilli(libraryEntity.overdueDate!!)
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
    bookId = this.book.id,
    query = query,
    page = page,
    offset = this.offset,
    cachedAt= cachedAt,
    accessedAt= accessedAt
)

fun Library.toLibraryEntity() = LibraryEntity(
    libraryId = this.libraryId,
    bookId= this.book.id,
    statusType= this.bookStatus.toStringType(),
    userId= this.bookStatus.toStringUserId(),
    borrowedAt= this.bookStatus.toLongBorrowedAt(),
    dueDate = this.bookStatus.toLongDueDate(),
    overdueDate = this.bookStatus.toLongOverdueDate(),
    callNumber= this.callNumber,
    location = this.location
)

fun BookStatus.toStringType()  = when(this) {
    is BookStatus.Available -> BookStatusType.AVAILABLE.name
    is BookStatus.UnAvailable -> BookStatusType.UNAVAILABLE.name
    is BookStatus.Borrowed -> BookStatusType.BORROWED.name
    is BookStatus.Reserved -> BookStatusType.RESERVED.name
    is BookStatus.OverDue -> BookStatusType.OVERDUE.name
}

fun BookStatus.toStringUserId()  = when(this) {
    is BookStatus.Available -> null
    is BookStatus.UnAvailable -> null
    is BookStatus.Borrowed -> this.userId
    is BookStatus.Reserved -> null
    is BookStatus.OverDue -> this.userId
}

fun BookStatus.toLongBorrowedAt()  = when(this) {
    is BookStatus.Available -> null
    is BookStatus.UnAvailable -> null
    is BookStatus.Borrowed -> this.borrowedAt.toEpochMilli()
    is BookStatus.Reserved -> null
    is BookStatus.OverDue -> null
}

fun BookStatus.toLongDueDate()  = when(this) {
    is BookStatus.Available -> null
    is BookStatus.UnAvailable -> null
    is BookStatus.Borrowed -> this.dueDate.toEpochMilli()
    is BookStatus.Reserved -> null
    is BookStatus.OverDue -> null
}

fun BookStatus.toLongOverdueDate()  = when(this) {
    is BookStatus.Available -> null
    is BookStatus.UnAvailable -> null
    is BookStatus.Borrowed -> null
    is BookStatus.Reserved -> null
    is BookStatus.OverDue -> this.overdueDate.toEpochMilli()
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
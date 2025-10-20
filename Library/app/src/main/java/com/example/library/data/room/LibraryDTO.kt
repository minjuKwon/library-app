package com.example.library.data.room

import androidx.room.Embedded
import androidx.room.Relation

data class SearchResultWithLibrary(
    @Embedded val searchResultEntity: SearchResultEntity,
    @Relation(
        entity = LibraryEntity::class,
        parentColumn = "libraryId",
        entityColumn = "libraryId"
    )
    val libraryBook: LibraryWithBook
)

data class LibraryWithBook(
    @Embedded val libraryEntity:LibraryEntity,
    @Relation(
        entity = BookEntity::class,
        parentColumn = "bookId",
        entityColumn = "id"
    )
    val book:BookWitImage
)

data class BookWitImage(
    @Embedded val bookEntity:BookEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id"
    )
    val image:BookImageEntity
)
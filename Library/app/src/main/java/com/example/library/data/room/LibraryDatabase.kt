package com.example.library.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [LibraryEntity::class, BookEntity::class, BookImageEntity::class,
        SearchResultEntity::class, SearchTotalCountEntity::class],
    version = 1
)
@TypeConverters(DatabaseConverters::class)
abstract class LibraryDatabase:RoomDatabase() {
    abstract fun bookCacheDao():BookCacheDao
}
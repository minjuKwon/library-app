package com.example.library.data.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [LibraryEntity::class, BookEntity::class, BookImageEntity::class,
        SearchResultEntity::class, SearchTotalCountEntity::class],
    version = 2,
    autoMigrations = [AutoMigration(1,2)]
)
@TypeConverters(DatabaseConverters::class)
abstract class LibraryDatabase:RoomDatabase() {
    abstract fun bookCacheDao():BookCacheDao
}